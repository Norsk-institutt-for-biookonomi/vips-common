/*
 * Copyright (c) 2014 Bioforsk <http://www.bioforsk.no/>. 
 * 
 * This file is part of VIPSCommon.
 * VIPSCommon is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * VIPSCommon is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with VIPSCommon.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package no.bioforsk.vips.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import no.bioforsk.vips.entity.WeatherObservation;
import no.bioforsk.vips.model.ConfigValidationException;

/**
 * Weather related utility methods
 * @copyright 2013 <a href="http://www.bioforsk.no/">Bioforsk</a>
 * @author Tor-Einar Skog <tor-einar.skog@bioforsk.no>
 */
public class WeatherUtil {
    
    public final static int AGGREGATION_TYPE_AVERAGE = 1;
    public final static int AGGREGATION_TYPE_SUM = 2;
    public final static int AGGREGATION_TYPE_MINIMUM = 3;
    public final static int AGGREGATION_TYPE_MAXIMUM = 4;

    /**
     * 
     * @param temperature
     * @param relativeHumidity
     * @return 
     */
    public List<WeatherObservation> calculateLeafWetnessHourSeriesSimple(List<WeatherObservation> temperature, List<WeatherObservation> rainfall, List<WeatherObservation> relativeHumidity) throws ConfigValidationException
    {
        List<WeatherObservation> retVal = new ArrayList<>();
        Iterator<WeatherObservation> humIt = relativeHumidity.iterator();
        WeatherObservation humObs;
        Iterator<WeatherObservation> rainIt = rainfall.iterator();
        WeatherObservation rainObs;
        
        for(WeatherObservation tempObs:temperature)
        {
            if(humIt.hasNext() && rainIt.hasNext())
            {
                humObs = humIt.next();
                rainObs = rainIt.next();
            }
            else
            {
                throw new ConfigValidationException("When calculating leaf wetness: "
                        + "Differing lengths of lists with input weather data. "
                        + "Temperature list has " + temperature.size() + " elements, "
                        + "rainfall list has " + rainfall.size() + " elements, "
                        + "relative humidity list has " + relativeHumidity.size() + " elements.");
            }
            if(humObs.getTimeMeasured().compareTo(tempObs.getTimeMeasured()) != 0 || humObs.getTimeMeasured().compareTo(rainObs.getTimeMeasured()) != 0)
            {
                throw new ConfigValidationException("When calculating leaf wetness: "
                        + "Timestamps of weather observations are not parallel. "
                        + "Temperature has timestamp " + tempObs.getTimeMeasured() + ", "
                        + "rainfall has timestamp " + rainObs.getTimeMeasured() + ", "
                        + "relative humidity has timestamp " + humObs.getTimeMeasured() + ".");
            }
            
            WeatherObservation leafWetness = new WeatherObservation();
            leafWetness.setElementMeasurementTypeId(WeatherElements.LEAF_WETNESS);
            leafWetness.setLogIntervalId(WeatherObservation.LOG_INTERVAL_ID_1H);
            leafWetness.setTimeMeasured(humObs.getTimeMeasured());
            leafWetness.setValue(this.calculateLeafWetnessHourSimple(tempObs.getValue(), rainObs.getValue(), humObs.getValue()));
            retVal.add(leafWetness);
        }
        return retVal;
    }

    /**
     * Calculate leaf wetness from a limited set of weather parameters.
     * Based on the method used by Marc Trapman in RIMPro (version 2014)
     * @param temperature degrees Celcius, average for 1 hour
     * @param rainfall millimeters, aggregate for 1 hour
     * @param relativeHumidity percent, average for 1 hour
     * @return minutes with leaf wetness during one hour. In this simple method,
     * it returns either 60 or 0 (all or nothing!)
     */
    public Double calculateLeafWetnessHourSimple(Double temperature, Double rainfall, Double relativeHumidity)
    {
        return (rainfall > 0 || this.getVaporPressureDeficit(temperature, relativeHumidity) < 2) ? 60d : 0;
    }
    
    /**
     * This is after Bolton 1980
     * @param relativeHumidity
     * @param temperature
     * @return Vapor Pressure Deficit in kPa (kiloPascal)
     */
    public double getVaporPressureDeficit(Double temperature, Double relativeHumidity)
    {
        return ((100-relativeHumidity) / 100) * 6.112 * Math.exp(17.67 * temperature / (temperature + 243.5));
    }
    
    /**
     * Calculates Water Vapor Deficiency in Pa
     * @param temperature
     * @param relative humidity
     * @return Water Vapor Deficiency in Pa
     */
    public double getWVD(double temperature, double relativeHumidity) {
        double saturationPressure = this.getSaturationPressure(temperature);
        double partialPressure = this.getPartialPressure(saturationPressure, relativeHumidity);
        /*
        if(this.DEBUG)
        System.out.println("[BlightModelActivity] DEBUG: saturationPressure=" + saturationPressure + ", partialPressure=" + partialPressure + ", WVD=" + ((saturationPressure - partialPressure) * 1000));
         * */
        return (saturationPressure - partialPressure) * 1000;
    }

    /**
     * Calculates the partial pressure
     * @param saturationPressure in kPa
     * @param RH
     * @return partial pressure in kPa
     */
    public double getPartialPressure(double saturationPressure, double relativeHumidity) {
        return relativeHumidity * saturationPressure / 100;
    }

    /**
     * Calculates the saturation pressure, based on temperature
     * @param temperature
     * @return saturation pressure in kPa
     */
    public double getSaturationPressure(double temperature) {
        return 0.61078 * Math.exp(17.269 * temperature / (temperature + 237.3));
    }
    
    /**
     * 
     * @param observation hourly Values (logInterval 1h)
     * @return aggregated daily values (logInterval 24h/1d)
     */
    public List<WeatherObservation> getAggregatedDailyValues(
            List<WeatherObservation> observations, 
            TimeZone timeZone, 
            Integer minimumObservationsPerDay, 
            Integer typeOfAggregation) 
            throws WeatherObservationListException,
            InvalidAggregationTypeException
    {
        // First we organize the hourly values into one bucket per day
        Map<Date,Map> dateBucket = new HashMap<>();
        Calendar cal = Calendar.getInstance(timeZone);
        String expectedParameter = observations.get(0).getElementMeasurementTypeId();
        for(WeatherObservation observation:observations)
        {
            if(!observation.getElementMeasurementTypeId().equals(expectedParameter))
            {
                throw new WeatherObservationListException("Found multiple parameters: " + observation.getElementMeasurementTypeId() + " and " + expectedParameter);
            }
            Date theDate = normalizeToExactDate(observation.getTimeMeasured(), timeZone);
            Map<Date, Double> hourValuesForDate = dateBucket.get(theDate);
            if(hourValuesForDate == null)
            {
                hourValuesForDate = new HashMap<>();
                dateBucket.put(theDate, hourValuesForDate);
            }
            
            // Check for double entries
            Double possibleDuplicate = hourValuesForDate.get(observation.getTimeMeasured());
            if(possibleDuplicate != null)
            {
                throw new WeatherObservationListException(
                        "Found duplicate weatherObservations for parameter " +
                        observation.getElementMeasurementTypeId() + " at time " +
                        observation.getTimeMeasured()
                );
            }
            hourValuesForDate.put(observation.getTimeMeasured(), observation.getValue());
        }
        
        // Then we iterate the buckets, do the aggregation and create return values
        List<WeatherObservation> aggregatedObservations = new ArrayList<>();
        WeatherObservation templateObservation = observations.get(0);
        Double aggregateValue;
        for(Date aDay:dateBucket.keySet())
        {
            //System.out.println("date=" + aDay);
            Map hourValuesForADay = dateBucket.get(aDay);
            if(hourValuesForADay.size() < minimumObservationsPerDay)
            {
                throw new WeatherObservationListException(
                        "Too few observations to aggregate for parameter " +
                        templateObservation.getElementMeasurementTypeId() +
                        " at date " + aDay +". Found " + hourValuesForADay.size() +
                        ", expected minimum " + minimumObservationsPerDay
                        );
            }
            switch(typeOfAggregation){
                case WeatherUtil.AGGREGATION_TYPE_AVERAGE:
                    aggregateValue = getAverage(hourValuesForADay.values()); break;
                case WeatherUtil.AGGREGATION_TYPE_SUM:
                    aggregateValue = getSum(hourValuesForADay.values()); break;
                case WeatherUtil.AGGREGATION_TYPE_MINIMUM:
                    aggregateValue = getMinimum(hourValuesForADay.values()); break;
                case WeatherUtil.AGGREGATION_TYPE_MAXIMUM:
                    aggregateValue = getMaximum(hourValuesForADay.values()); break;
                default:
                    throw new InvalidAggregationTypeException(
                            "No aggregation method with id= " + typeOfAggregation  + " exists."
                            );
            }
            WeatherObservation aggregatedObservation = new WeatherObservation();
            aggregatedObservation.setElementMeasurementTypeId(templateObservation.getElementMeasurementTypeId());
            aggregatedObservation.setLogIntervalId(WeatherObservation.LOG_INTERVAL_ID_1D);
            aggregatedObservation.setTimeMeasured(aDay);
            aggregatedObservation.setValue(aggregateValue);
            aggregatedObservations.add(aggregatedObservation);
        }
        return aggregatedObservations;
    }
    
    private Double getAverage(Collection<Double> values)
    {
        return this.getSum(values)/values.size();
    }

    private Double getSum(Collection<Double> values)
    {
       Double sum = 0d;
        for(Double value:values)
        {
            sum += value;
        }
        return sum;
    }
    
    private Double getMinimum(Collection<Double> values)
    {
        Double minimum = null;
        for(Double value:values)
        {
            minimum = minimum == null ? value : Math.min(minimum, value);
        }
        return minimum;
    }
    
    private Double getMaximum(Collection<Double> values)
    {
        Double maximum = null;
        for(Double value:values)
        {
            maximum = maximum == null ? value : Math.max(maximum, value);
        }
        return maximum;
    }
    
    public Date normalizeToExactDate(Date timeStamp, TimeZone timeZone)
    {
        Calendar cal = Calendar.getInstance(timeZone);
        cal.setTime(timeStamp);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND,0);
        cal.set(Calendar.MILLISECOND,0);
        return cal.getTime();
    }
}
