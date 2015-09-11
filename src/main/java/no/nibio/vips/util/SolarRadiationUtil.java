/*
 * Copyright (c) 2015 NIBIO <http://www.nibio.no/>. 
 * 
 * This file is part of VIPSCommon.
 * VIPSCommon is free software: you can redistribute it and/or modify
 * it under the terms of the NIBIO Open Source License as published by 
 * NIBIO, either version 1 of the License, or (at your option) any
 * later version.
 * 
 * VIPSCommon is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * NIBIO Open Source License for more details.
 * 
 * You should have received a copy of the NIBIO Open Source License
 * along with VIPSCommon.  If not, see <http://www.nibio.no/licenses/>.
 * 
 */

package no.nibio.vips.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import no.nibio.vips.entity.WeatherObservation;

/**
 * Helping with calculating stuff related to solar radiation
 * @copyright 2015 <a href="http://www.nibio.no/">NIBIO</a>
 * @author Tor-Einar Skog <tor-einar.skog@nibio.no>
 */
public class SolarRadiationUtil {
    
    public List<WeatherObservation> getCalculatedSolarRadiation(Double latitude, Double longitude, Date timeStart, Date timeEnd, TimeZone timeZone, Integer logIntervalId)
    {
        Calendar cal = Calendar.getInstance(timeZone);
        
        Date currentTime = timeStart;
        List<WeatherObservation> radiationValues = new ArrayList<>();
        while(currentTime.compareTo(timeEnd) <= 0)
        {
            Double value = this.calculateSolarRadiationAtLocationAndTime(latitude, longitude, currentTime, timeZone);
            WeatherObservation radiationValue = new WeatherObservation();
            radiationValue.setLogIntervalId(logIntervalId);
            radiationValue.setElementMeasurementTypeId(WeatherElements.GLOBAL_RADIATION_CLEAR_SKY_CALCULATED);
            radiationValue.setTimeMeasured(currentTime);
            radiationValue.setValue(value);
            radiationValues.add(radiationValue);
            cal.setTime(currentTime);
            
            if(logIntervalId.equals(WeatherObservation.LOG_INTERVAL_ID_1H))
            {
                cal.add(Calendar.HOUR_OF_DAY, 1);
            }
            else if(logIntervalId.equals(WeatherObservation.LOG_INTERVAL_ID_30M))
            {
                cal.add(Calendar.MINUTE, 30);
            }
            else if(logIntervalId.equals(WeatherObservation.LOG_INTERVAL_ID_15M))
            {
                cal.add(Calendar.MINUTE, 15);
            }
            else if(logIntervalId.equals(WeatherObservation.LOG_INTERVAL_ID_10M))
            {
                cal.add(Calendar.MINUTE, 10);
            }
            else
            {
                cal.add(Calendar.HOUR_OF_DAY, 1);
            }
            
            currentTime = cal.getTime();
        }
        
        return radiationValues;
    }
    
    /**
     *  
     * @param latitude
     * @param longitude
     * @param date
     * @param timeZone
     * @return 
     */
    public Double calculateSolarRadiationAtLocationAndTime(Double latitude, Double longitude, Date date, TimeZone timeZone)
    {
        // We must adjust the hour to reflect the deviation from the LSTM (Local Standard Time Meridian)
        Calendar cal = Calendar.getInstance(timeZone);
        cal.setTime(date);
        int dayOfYear = cal.get(Calendar.DAY_OF_YEAR);
        Double hour =   cal.get(Calendar.HOUR_OF_DAY) 
                        + cal.get(Calendar.MINUTE) / 60.0
                        + cal.get(Calendar.SECOND) / 3600
                        + this.getSolarTimeAdjustment(longitude, date, timeZone) / 60;
        
        //System.out.println("Hour=" + hour);
        
        return this.getSolarRadiationAtLatitude(latitude, dayOfYear, hour);
        
    }
    
    /**
     * Calculates the time of sunset for a given location and date. Does NOT 
     * take into account local topography
     * @param latitude
     * @param longitude
     * @param date
     * @param timeZone
     * @return 
     */
    public Date getSunsetTimeAtDate(Double latitude, Double longitude, Date date, TimeZone timeZone)
    {
        // We try a new approach: Getting sun hours and adjusting them
        Calendar cal = Calendar.getInstance(timeZone);
        cal.setTime(date);
        Double sunHoursAfterMidday = this.getSunHoursFromMidday(latitude, cal.get(Calendar.DAY_OF_YEAR));
        
        Double solarTimeAdjustment = this.getSolarTimeAdjustment(longitude, date, timeZone);
        
        Double sunsetTime= 12 - (solarTimeAdjustment/60.0) + sunHoursAfterMidday;
        if(sunsetTime > 24.0)
        {
            return null;
        }
        
        Integer sunsetHour = new Double(Math.floor(sunsetTime)).intValue();
        Integer sunsetMinute = new Double(Math.round((sunsetTime - sunsetHour) * 60.0)).intValue();
        /*
        System.out.println("sunHoursAfterMidday=" + sunHoursAfterMidday);
        System.out.println("solarTimeAdjustment=" + solarTimeAdjustment);
        System.out.println("sunSetTime=" + sunsetTime);
        System.out.println("sunsetHour=" + sunsetHour + ", sunsetMinute= " + sunsetMinute
        */
        cal.set(Calendar.HOUR_OF_DAY, sunsetHour);
        cal.set(Calendar.MINUTE, sunsetMinute);
        return cal.getTime();
    }
    
    /**
     * Calculates the time of sunrise for a given location and date. Does NOT 
     * take into account local topography
     * @param latitude
     * @param longitude
     * @param date
     * @param timeZone
     * @return 
     */
    public Date getSunriseTimeAtDate(Double latitude, Double longitude, Date date, TimeZone timeZone)
    {
        Calendar cal = Calendar.getInstance(timeZone);
        cal.setTime(date);
        Double sunHoursBeforeMidday = this.getSunHoursFromMidday(latitude, cal.get(Calendar.DAY_OF_YEAR));
        
        Double solarTimeAdjustment = this.getSolarTimeAdjustment(longitude, date, timeZone);
        
        Double sunriseTime= 12 - (solarTimeAdjustment/60.0) - sunHoursBeforeMidday;
        if(sunriseTime < 0)
        {
            return null;
        }
        
        Integer sunsetHour = new Double(Math.floor(sunriseTime)).intValue();
        Integer sunsetMinute = new Double(Math.round((sunriseTime - sunsetHour) * 60.0)).intValue();
        /*
        System.out.println("sunHoursAfterMidday=" + sunHoursAfterMidday);
        System.out.println("solarTimeAdjustment=" + solarTimeAdjustment);
        System.out.println("sunSetTime=" + sunsetTime);
        System.out.println("sunsetHour=" + sunsetHour + ", sunsetMinute= " + sunsetMinute
        */
        cal.set(Calendar.HOUR_OF_DAY, sunsetHour);
        cal.set(Calendar.MINUTE, sunsetMinute);
        return cal.getTime();
    }
    
    
    /**
     * Returns the amount of time (in minutes) to add 
     * to current local time to get the correct solar time. This includes DST 
     * offset
     * 
     * @param longitude
     * @param date
     * @param timeZone
     * @return 
     */
    public Double getSolarTimeAdjustment(Double longitude, Date date, TimeZone timeZone)
    {
        Calendar cal = Calendar.getInstance(timeZone);
        cal.setTime(date);
        int dayOfYear = cal.get(Calendar.DAY_OF_YEAR);
        Double timeCorrection = this.getTimeCorrectionFactor(longitude, dayOfYear, timeZone);
        //System.out.println("timeCorrection=" + timeCorrection);
        // We must factor in DST change as well
        int dstOffsetInMillis = timeZone.getRawOffset() - timeZone.getOffset(date.getTime());
        //System.out.println("dstOffsetInMillis=" + dstOffsetInMillis);
        return timeCorrection + dstOffsetInMillis / 60000;
    }
    
    /**
     * 
     * <p>The net Time Correction Factor (in minutes) accounts for the variation of 
     * the Local Solar Time (LST) within a given time zone due to the longitude 
     * variations within the time zone and also incorporates the EoT above.</p>
     * 
     * <p>TC = 4(Longitude - LSTM) + EoT</p>
     * 
     * <p>DST corrections are NOT done.</p>
     * 
     * @param longitude in decimal degrees
     * @param dayOfYear
     * @param timeZone
     * @return 
     */
    public Double getTimeCorrectionFactor(Double longitude, Integer dayOfYear, TimeZone timeZone)
    {
        //System.out.println("longitude - this.getLSTM(timeZone)=" + (longitude - this.getLSTM(timeZone)));
        //System.out.println("EoT(" + dayOfYear +") = " + this.getEquationOfTime(dayOfYear));
        return 4 * (longitude - this.getLSTM(timeZone)) + this.getEquationOfTime(dayOfYear);
    }
    
    /**
     * <p>Get the longitude for the Local Standard Time Meridian for the given time zone</p>
     * <p>Source: <a href="http://www.pveducation.org/pvcdrom/properties-of-sunlight/solar-time">http://www.pveducation.org/pvcdrom/properties-of-sunlight/solar-time</a></p>
     * @param timeZone
     * @return LSTM in <a href="https://en.wikipedia.org/wiki/Decimal_degrees">decimal degrees</a>
     */
    public Double getLSTM(TimeZone timeZone)
    {
        return (timeZone.getRawOffset() / 3600000.0) * 15.0;
    }
       
    /**
     * <p>The equation of time (EoT) (in minutes) is an empirical equation that 
     * corrects for the eccentricity of the Earth's orbit and the Earth's axial 
     * tilt.<p>
     * <p>Source: <a href="http://www.pveducation.org/pvcdrom/properties-of-sunlight/solar-time">http://www.pveducation.org/pvcdrom/properties-of-sunlight/solar-time</a></p>
     * @param dayOfYear (1-365)
     * @return 
     */
    public Double getEquationOfTime(int dayOfYear)
    {
        Double b = Math.toRadians(360.0 / 365.0 * (dayOfYear - 81));
        return 9.87 * Math.sin(2 * b) - 7.53 * Math.cos(b) - 1.5 * Math.sin(b);
    }
    
    /**
     * <p>Based on the equation of the sun's position in the sky throughout the 
     * year, the maximum amount of solar insolation on a surface at a particular 
     * tilt angle can be calculated as a function of latitude and day of the year. </p>
     * <h3>Sources</h3> 
     * <ul>
     *  <li><a href="http://pveducation.org/pvcdrom/properties-of-sunlight/calculation-of-solar-insolation">Demo of JavaScript implementation</a></p></li>
     *  <li><a href="http://pveducation.org/sites/default/files/PVCDROM/Graphs/Hourly.js">JavaScript 1</a></li>
     *  <li><a href="http://pveducation.org/sites/default/files/PVCDROM/Admin/pvcdrom.js">JavaScript 2</a></li>
     * </ul>
     * @param latitude Decimal degrees
     * @param date the time
     * @return the estimated solar radiation at a given latitude and time in kW/m<sup>2</sup>, assuming clear sky
     * 
     */
    public Double getSolarRadiationAtLatitude(Double latitude, Integer dayOfYear, Double hour)
    {
        Double H = this.getSunHoursFromMidday(latitude, dayOfYear);
        Double sunrise = 12.0 - H;
        Double sunset = 12.0 + H;
        Double latitudeRad = Math.toRadians(latitude);
        Double retVal;
        if(hour > sunrise && hour < sunset)
        {
            double am = this.getAirMass(hour, dayOfYear, latitudeRad);
            double x1 = Math.pow(0.7, am);
            retVal = 1.353 * Math.pow(x1, 0.678);
            
            if(retVal > 1.1)
            {
                retVal = 0.0;
            }
        }
        else 
        {
            retVal = 0.0;
        }
        return retVal;
    }

    /**
     * Calculates day length, or more precisely duration between sunrise or 
     * sunset and midday
     * 
     * @param latitude in decimal degrees
     * @param dayOfYear (1-365)
     * @return 
     */
    public Double getSunHoursFromMidday(Double latitude, Integer dayOfYear)
    {
        Double declinationRad = Math.toRadians(this.getDeclination(dayOfYear));
        Double latitudeRad = Math.toRadians(latitude);
        
        Double x = - (Math.sin(latitudeRad) * Math.sin(declinationRad));
        x = x / (Math.cos(latitudeRad) * Math.cos(declinationRad));
        if(x > 1.0)
        {
            x = 1.0;
        }
        if(x < -1.0)
        {
            x = -1.0;
        }
        Double f = Math.acos(x);
        return Math.toDegrees(f * 1 / 15.0);
    }
    
    /**
     * <p>Get the declination angle of the sun.</p>
     * <p>The declination angle, denoted by δ, varies seasonally due to the tilt 
     * of the Earth on its axis of rotation and the rotation of the Earth around 
     * the sun. If the Earth were not tilted on its axis of rotation, the 
     * declination would always be 0°. However, the Earth is tilted by 23.45° 
     * and the declination angle varies plus or minus this amount. Only at the 
     * spring and fall equinoxes is the declination angle equal to 0°. </p>
     * <h2>JavaScript implementation</h2>
     * <pre>
     * function Declination(c)
     * {
     *  return 23.45 * Math.sin(ToRad(360/365*(c-81)));
     * }
     * </pre>
     * <p>Source: <a href="http://www.pveducation.org/pvcdrom/properties-of-sunlight/declination-angle">http://www.pveducation.org/pvcdrom/properties-of-sunlight/declination-angle</a></p>
     * @param dayOfYear
     * @return 
     */
    public Double getDeclination(Integer dayOfYear) {
        return 23.45 * Math.sin(Math.toRadians(360.0/365.0*(dayOfYear-81)));
    }
    
   
    /**
     * <p>
     * The Air Mass is the path length which light takes through the 
     * atmosphere normalized to the shortest possible path length (that is, when 
     * the sun is directly overhead). The Air Mass quantifies the reduction in 
     * the power of light as it passes through the atmosphere and is absorbed by 
     * air and dust.
     * </p>
     * <p>Source: <a href="http://pveducation.org/pvcdrom/properties-of-sunlight/air-mass">http://pveducation.org/pvcdrom/properties-of-sunlight/air-mass</a>
     * @param hour
     * @param dayOfYear
     * @param latitudeRad
     * @return 
     */
    private Double getAirMass(Double hour, int dayOfYear, Double latitudeRad) {
        Double declinationRad = Math.toRadians(this.getDeclination(dayOfYear));
        Double hourAngle = Math.toRadians(15 * (hour - 12));
        Double elevation = Math.asin(Math.sin(declinationRad) * Math.sin(latitudeRad) + Math.cos(declinationRad) * Math.cos(latitudeRad) * Math.cos(hourAngle));
        Double zenithAngle = Math.toRadians(90.0) - elevation;
        return 1 / (0.0001  + Math.cos(zenithAngle));
    }

}
