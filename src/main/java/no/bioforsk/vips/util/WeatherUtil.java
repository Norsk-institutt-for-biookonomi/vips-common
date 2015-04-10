/*
 * Copyright (c) 2015 Bioforsk <http://www.bioforsk.no/>. 
 * 
 * This file is part of VIPSCommon.
 * VIPSCommon is free software: you can redistribute it and/or modify
 * it under the terms of the Bioforsk Open Source License as published by 
 * Bioforsk, either version 1 of the License, or (at your option) any
 * later version.
 * 
 * VIPSCommon is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * Bioforsk Open Source License for more details.
 * 
 * You should have received a copy of the Bioforsk Open Source License
 * along with VIPSCommon.  If not, see <http://www.bioforsk.no/licenses/>.
 * 
 */

package no.bioforsk.vips.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import no.bioforsk.vips.entity.WeatherObservation;
import no.bioforsk.vips.model.ConfigValidationException;

/**
 * Weather related utility methods
 * @author Tor-Einar Skog <tor-einar.skog@bioforsk.no>
 */
public class WeatherUtil {
    
    public final static int AGGREGATION_TYPE_AVERAGE = 1;
    public final static int AGGREGATION_TYPE_SUM = 2;
    public final static int AGGREGATION_TYPE_MINIMUM = 3;
    public final static int AGGREGATION_TYPE_MAXIMUM = 4;

    /**
     * Ensures that hourly values at the end of series that exceed
     * the day before is cut of
     * @param hourlyValues
     * @return 
     */
    public List<WeatherObservation> cutTrailingHourlyValues(List<WeatherObservation> hourlyValues, TimeZone timeZone) {
        // Special case: List has less than 24 hourly values, return empty list
        if(hourlyValues.size() < 24)
        {
            return new ArrayList<>();
        }
        // Sort the list 
        Collections.sort(hourlyValues);
        // Get the last 24 values, see if they're in the same day
        Integer dateShiftIndex = 0;
        for(int i=1;i<24;i++)
        {
            if(!this.isSameDate(hourlyValues.get(hourlyValues.size()-i),hourlyValues.get(hourlyValues.size()-i-1),timeZone))
            {
                dateShiftIndex = hourlyValues.size()-i;
                break;
            }
        }
        if(dateShiftIndex == 0)
        {
            return hourlyValues;
        }
        else
        {
            return new ArrayList<WeatherObservation>(hourlyValues.subList(0,dateShiftIndex));
        }
    }

    /**
     * 
     * @param obs1
     * @param obs2
     * @param timeZone
     * @return true if the two dates are the same date, given the time zone
     */
    private boolean isSameDate(WeatherObservation obs1, WeatherObservation obs2, TimeZone timeZone) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        format.setTimeZone(timeZone);
        return format.format(obs1.getTimeMeasured()).equals(format.format(obs2.getTimeMeasured()));
    }

    /**
     * 
     * @param temperature
     * @param relativeHumidity
     * @return 
     */
    public List<WeatherObservation> calculateLeafWetnessHourSeriesSimple(List<WeatherObservation> temperature, List<WeatherObservation> rainfall, List<WeatherObservation> relativeHumidity) throws ConfigValidationException
    {
        List<WeatherObservation> retVal = new ArrayList<WeatherObservation>();
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
     * @return aggregated daily values (logInterval 24h/1d) or NULL if no observations are given
     */
    public List<WeatherObservation> getAggregatedDailyValues(
            List<WeatherObservation> observations, 
            TimeZone timeZone, 
            Integer minimumObservationsPerDay, 
            Integer typeOfAggregation) 
            throws WeatherObservationListException,
            InvalidAggregationTypeException
    {
        if(observations == null || observations.isEmpty())
        {
            return null;
        }
        // First we organize the hourly values into one bucket per day
        Map<Date,Map> dateBucket = new HashMap<Date,Map>();
        String expectedParameter = observations.get(0).getElementMeasurementTypeId();
        Date lastDate = null;
        for(WeatherObservation observation:observations)
        {
            if(!observation.getElementMeasurementTypeId().equals(expectedParameter))
            {
                throw new WeatherObservationListException("Found multiple parameters: " + observation.getElementMeasurementTypeId() + " and " + expectedParameter);
            }
            Date theDate = normalizeToExactDate(observation.getTimeMeasured(), timeZone);
            lastDate = lastDate == null ? theDate : (lastDate.compareTo(theDate) < 0 ? theDate : lastDate);
            Map<Date, Double> hourValuesForDate = dateBucket.get(theDate);
            if(hourValuesForDate == null)
            {
                hourValuesForDate = new HashMap<Date,Double>();
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
            // We accept less than minimum values for the last day (we don't throw an error)
            if(hourValuesForADay.size() < minimumObservationsPerDay && aDay.compareTo(lastDate) < 0)
            {
                throw new WeatherObservationListException(
                        "Too few observations to aggregate for parameter " +
                        templateObservation.getElementMeasurementTypeId() +
                        " at date " + aDay +". Found " + hourValuesForADay.size() +
                        ", expected minimum " + minimumObservationsPerDay
                        );
            }
            // If last day and too few values: Skip it
            else if(hourValuesForADay.size() < minimumObservationsPerDay)
            {
                continue;
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
    
    /**
     * Sets hours, minutes, seconds and milliseconds to ZERO
     * @param timeStamp
     * @param timeZone
     * @return 
     */
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
    
    /**
     * Assuming that this is a midnight timestamp that has been skewed 
     * due to DST changes, the method attempts to return a correct midnight
     * time stamp in the requested time zone
     * @param timeStamp
     * @param timeZone
     * @return 
     */
    public Date pragmaticAdjustmentToMidnight(Date timeStamp, TimeZone timeZone)
    {
        Calendar cal = Calendar.getInstance(timeZone);
        cal.setTime(timeStamp);
        // If we're close to BEFORE midnight, add one day
        if(cal.get(Calendar.HOUR_OF_DAY) >= 22)
        {
            cal.add(Calendar.DATE, 1);
        }
        // Then set hours, minutes etc. to zero
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND,0);
        cal.set(Calendar.MILLISECOND,0);
        return cal.getTime();
    }

    /**
     * Given input data, attempts to calculate leaf wetness. Does not overwrite provided leaf wetness data.
     * Priority (depending on what data are provided):
     * <ol>
     * <li>Provided leaf wetness data</li>
     * <li>Nærstad's leaf wetness calculation algorithm</li>
     * <li>Trapman's simple leaf wetness calculation algorithm</li>
     * </ol>
     * <p>Requires that at least TM, RR, and UM are complete. And that all lists are sorted!</p>
     * 
     * @param BT Leaf wetness (minutes/hour)
     * @param TM Mean temperature (hourly mean, degrees Celcius)
     * @param RR Rainfall (mm, hourly aggregate)
     * @param FM2 Wind speed at 2m height (average, m/s)
     * @param Q0 Global radiation (unit??)
     * @param UM Relative humidity (average, %)
     * @return 
     */
    public List<WeatherObservation> calculateLeafWetnessHourSeriesBestEffort(List<WeatherObservation> BT, List<WeatherObservation> TM, List<WeatherObservation> RR, List<WeatherObservation> Q0, List<WeatherObservation> FM2, List<WeatherObservation> UM) throws ConfigValidationException {
        // If BT is same length as TM, return BT unmodified
        if(BT.size() == TM.size())
        {
            return BT;
        }
        
        // Possible TODO: Validate that TM==RR==UM
        // First: Find time and index for when to start calculating
        
        Date startTime, lastBTTime;
        if(BT.size() > 0)
        {
            lastBTTime = BT.get(BT.size()-1).getTimeMeasured();
            Calendar cal = Calendar.getInstance();
            cal.setTime(lastBTTime);
            cal.add(Calendar.HOUR_OF_DAY, 1);
            startTime = cal.getTime();
        }
        else
        {
            startTime = TM.get(0).getTimeMeasured();
            lastBTTime = null;
        }
        
        
        
        // If all parameters are complete, we can use Nærstad's algorithm. Otherwise, if we only have UM,
        // we must resort to Trapman's algorithm
        List<WeatherObservation> calculatedBT;
        if(TM.size() == RR.size() && TM.size() == Q0.size() && TM.size() == FM2.size() && TM.size() == UM.size())
        {
            // The algorithm uses the first hour of input values as "startup"
            // So if we have some leaf wetness, we can provide the first hour.
            if(BT.size() > 0)
            {
                //System.out.println("Nærstad BT with BT measured values: startTime=" + startTime);
                //System.out.println("Last BT timestamp = " + BT.get(BT.size()-1).getTimeMeasured());
                calculatedBT = this.calculateLeafWetnessHourSeriesNaerstad(
                        this.getWeatherObservationsInPeriod(TM, lastBTTime, null),
                        this.getWeatherObservationsInPeriod(RR, lastBTTime, null),
                        this.getWeatherObservationsInPeriod(Q0, lastBTTime, null),
                        this.getWeatherObservationsInPeriod(FM2, lastBTTime, null),
                        this.getWeatherObservationsInPeriod(UM, lastBTTime, null),
                        BT.get(BT.size()-1)
                );
            }
            // Otherwise, we have to create a dry hour as the first hour
            else
            {
                //System.out.println("Nærstad BT without BT measured values: startTime=" + startTime);
                WeatherObservation emptyObs = new WeatherObservation(
                                                startTime,
                                                WeatherElements.LEAF_WETNESS,
                                                WeatherObservation.LOG_INTERVAL_ID_1H,
                                                0d
                                                );
                calculatedBT = new ArrayList<WeatherObservation>();
                calculatedBT.add(emptyObs);
                calculatedBT.addAll(this.calculateLeafWetnessHourSeriesNaerstad(TM, RR, Q0, FM2, UM, null));
            }
        }
        else if(TM.size() == RR.size() && TM.size() == UM.size())
        {
            /*
            System.out.println("Trapman BT: startTime=" + startTime);
            if(BT.size() > 0)
            {
                System.out.println("Last BT timestamp = " + BT.get(BT.size()-1).getTimeMeasured());
            }
            */
            calculatedBT = this.calculateLeafWetnessHourSeriesSimple(
                    this.getWeatherObservationsInPeriod(TM, startTime, null),
                    this.getWeatherObservationsInPeriod(RR, startTime, null),
                    this.getWeatherObservationsInPeriod(UM, startTime, null)
            );
        }
        else
        {
            throw new ConfigValidationException("Missing weather data. Number of _found_ observations per parameter (deviating number indicates missing data for that parameter): "
                    + "TM=" + TM.size()
                    + ",RR=" + RR.size()
                    + ",UM=" + UM.size()
            );
        }
        //System.out.println("First new obs timestamp = " + calculatedBT.get(0).getTimeMeasured());
        BT.addAll(calculatedBT);
        return BT;
    }
    
    /**
     * Extract all observations in the given period from given list
     * @param allObservations
     * @param dateStart INCLUDING start date. May be null 
     * @param dateEnd INCLUDING end time. May be null (if both dateStart AND dateEnd == null, the list is the same as the one sent in)
     * @return 
     */
    public List<WeatherObservation> getWeatherObservationsInPeriod(List<WeatherObservation> allObservations, Date dateStart, Date dateEnd)
    {
        List<WeatherObservation> retVal = new ArrayList<WeatherObservation>();
        for(WeatherObservation obs:allObservations)
        {
            if(
                    (dateStart == null || obs.getTimeMeasured().compareTo(dateStart) >= 0) 
                    && (dateEnd == null || obs.getTimeMeasured().compareTo(dateEnd) <= 0)
                    )
            {
                retVal.add(obs);
            }
        }
        return retVal;
    }

    /**
     * 
     * @param temperature
     * @param precipitation
     * @param globalRadiation
     * @param wind2m
     * @param relativeHumidity
     * @param precedingLeafWetness
     * @return Leaf wetness for the same time period as provided by input data, 
     * EXCEPT for the first hour, as that is used for "startup" of the algorithm.
     * So the size of the returned list is 1 less than the input lists.
     */
    private List<WeatherObservation> calculateLeafWetnessHourSeriesNaerstad(
            List<WeatherObservation> temperature, 
            List<WeatherObservation> precipitation, 
            List<WeatherObservation> globalRadiation, 
            List<WeatherObservation> wind2m, 
            List<WeatherObservation> relativeHumidity,
            WeatherObservation precedingLeafWetness) 
    {
        // TODO Validation
        
        List<WeatherObservation> calculatedLeafWetnessSeries = new ArrayList<>();
        Double lastLatentHeatFlux = this.calculateLatentHeatFlux(
                temperature.get(0).getValue(), 
                relativeHumidity.get(0).getValue(), 
                wind2m.get(0).getValue(), 
                globalRadiation.get(0).getValue()
        );
        Integer precedingLeafWetnessValue = precedingLeafWetness != null ?  (int) precedingLeafWetness.getValue() : 0;
        // Iterating list with index (instead of using Iterator pattern) has 
        // performance gains on very long lists
        for(int i=1;i<temperature.size();i++)
        {
            WeatherObservation calculatedLeafWetness = new WeatherObservation(
                    temperature.get(i).getTimeMeasured(),
                    WeatherElements.LEAF_WETNESS,
                    WeatherObservation.LOG_INTERVAL_ID_1H,
                    (double) this.calculateLeafWetnessNaerstad(lastLatentHeatFlux, precedingLeafWetnessValue, precipitation.get(i).getValue())
            );
            calculatedLeafWetnessSeries.add(calculatedLeafWetness);
        }
        return calculatedLeafWetnessSeries;
    }
    
    /**
     * Nærstad's model of leaf wetness calculation
     * @param lastLatentHeatFlux
     * @param lastLeafWetness
     * @param precipitation
     * @return
     */
    public Long calculateLeafWetnessNaerstad(Double lastLatentHeatFlux, Integer lastLeafWetness, Double precipitation) {

        Long DLeafWetness = Math.round(lastLeafWetness - lastLatentHeatFlux);
        if (DLeafWetness < 0) {
            DLeafWetness = 0l;
        } else if (DLeafWetness > 60) {
            DLeafWetness = 60l;
        }


        Long leafWetness = Math.round(DLeafWetness + precipitation * 150);
        if (leafWetness < 0) {
            leafWetness = 0l;
        } else if (leafWetness > 60) {
            leafWetness = 60l;
        }

        return leafWetness;
    }
    
    /**
     * Part of the Nærstad leaf wetness calculation
     * @return
     */
    public Double calculateLatentHeatFlux(Double temperature, Double relativeHumidity, Double wind2m, Double globalRadiation) {
        // s(t) = 0.61078*EKSP(17.269* T(t)/( T(t)+237.3))*4.097*10000/(T(t)+237.3)2
        Double s = 0.61078 * Math.exp(17.269 * temperature / (temperature + 237.3)) * 4.097 * 10000 / Math.pow(temperature + 237.3, 2);
        // resistance(t) = 307*(0.07/( (windFF(t)   + 0.1) / 2 ))0.5
        Double resistance = 307 * Math.pow(0.07 / ((wind2m + 0.1) / 2), 0.5);
        // LE(t)  = -( (s(t) * Rn(t)  + (12(WVD (t) )/( resistance(t)))/(s(t) +0.64)
        Double LE = (s * globalRadiation + (12 * this.getWVD(temperature, relativeHumidity) / resistance)) / (s + 0.64);

        return LE;
    }

    /**
     * Fixes by 
     * <ul>
     * <li>Simple interpolation for continuous parameters (temperature, humidity)</li>
     * <li>Setting values to 0 for non continuous parameters (rainfall)</li>
     * </ul>
     * Maximum number of missing values in hole: 3 (hours)
     * @param obsList list of observations of same type (parameter)
     * @return 
     */
    public List<WeatherObservation> checkForAndFixHourlyTimeSeriesHoles(List<WeatherObservation> obsList) throws WeatherObservationListException {
        if(obsList == null || obsList.isEmpty())
        {
            return obsList;
        }
        // First we sort the list
        Collections.sort(obsList);
        Date currentTime = obsList.get(0).getTimeMeasured();
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentTime);
        //System.out.println("checkForAndFixHourlyTimeSeriesHoles " + obsList.get(0).getElementMeasurementTypeId() + ",start time= " + currentTime );
        for(int i=0;i<obsList.size();i++)
        {
            /*
            WeatherObservation debugObs = obsList.get(i);
            if(debugObs.getElementMeasurementTypeId().equals("TM"))
            {
                System.out.println(debugObs.getTimeMeasured() + ": " + debugObs.getValue());
            }*/
            
            int missingValues = 0;
            while(obsList.get(i).getTimeMeasured().compareTo(currentTime) != 0 
                    && obsList.get(i).getTimeMeasured().compareTo(currentTime) > 0)
            {
                missingValues++;
                // We have a hole
                //System.out.println("Hole found! At " + currentTime);
                
                // Must advance time until we find a matching time
                cal.add(Calendar.HOUR, 1);
                currentTime = cal.getTime();
            }
            //System.out.println("Missing values=" + missingValues);
            if(missingValues > 3)
            {
                throw new WeatherObservationListException("More than three missing values for "
                        + "parameter " + obsList.get(0).getElementMeasurementTypeId() 
                        + " between " + obsList.get(i-1).getTimeMeasured() + " and "
                        + obsList.get(i).getTimeMeasured() + ". Can't fix this!");
            }
            else if(missingValues > 0)
            {
                List<WeatherObservation> calculatedValues = new ArrayList<>();
                String elementMeasurementTypeId = obsList.get(i).getElementMeasurementTypeId();
                String fixingStrategy = this.getFixingStrategy(elementMeasurementTypeId);
                Date lastTimestampBeforeHole = obsList.get(i-1).getTimeMeasured();
                Calendar cal2 = Calendar.getInstance();
                cal2.setTime(lastTimestampBeforeHole);
                cal2.add(Calendar.HOUR_OF_DAY, 1);
                if(fixingStrategy.equals(WeatherElements.FIXING_STRATEGY_INTERPOLATE))
                {
                    Double lastValueBeforeHole = obsList.get(i-1).getValue();
                    Double valueDifference = obsList.get(i).getValue() - lastValueBeforeHole;
                    Double step = valueDifference / (missingValues + 1);
                    
                    for(int j=1;j<=missingValues;j++)
                    {
                        Double calculatedValue = lastValueBeforeHole + (step * j);
                        //System.out.println("CalculatedValue=" + calculatedValue);
                        WeatherObservation calculatedObservation = new WeatherObservation(
                                cal2.getTime(), 
                                elementMeasurementTypeId,
                                WeatherObservation.LOG_INTERVAL_ID_1H,
                                calculatedValue
                        );
                        cal2.add(Calendar.HOUR_OF_DAY, 1);
                        calculatedValues.add(calculatedObservation);
                    }
                }
                else if(fixingStrategy.equals(WeatherElements.FIXING_STRATEGY_SET_ZERO))
                {
                    for(int j=1;j<=missingValues;j++)
                    {
                        //System.out.println("CalculatedValue=" + calculatedValue);
                        WeatherObservation calculatedObservation = new WeatherObservation(
                                cal2.getTime(), // TODO!!!
                                elementMeasurementTypeId,
                                WeatherObservation.LOG_INTERVAL_ID_1H,
                                0d
                        );
                        cal2.add(Calendar.HOUR_OF_DAY, 1);
                        calculatedValues.add(calculatedObservation);
                    }
                }
                obsList.addAll(i, calculatedValues);
                i = i + missingValues;
            }
            
            // Advance time, move on
            cal.add(Calendar.HOUR, 1);
            currentTime = cal.getTime();
        }
        return obsList;
    }
    
    public String getFixingStrategy(String elementMeasurementCode)
    {
        return elementMeasurementCode.equals(WeatherElements.PRECIPITATION) ? WeatherElements.FIXING_STRATEGY_SET_ZERO : WeatherElements.FIXING_STRATEGY_INTERPOLATE;
    }
    
    /**
     * Attempts to return a list with complete series in same period for different parameters
     * @param mixedParameterList
     * @param parameterNames 
     * @return 
     */
    public List<WeatherObservation> fixHourlyValuesForParameters(List<WeatherObservation> mixedParameterList, Set parameterNames, Date firstTimestamp, Date lastTimestamp) throws WeatherObservationListException
    {
        // Removing duplicates first 
        mixedParameterList = this.removeDuplicateWeatherObservations(mixedParameterList);
        
        Map<String, List<WeatherObservation>> separatedParameters = new HashMap<>();
        
        Date estimatedLastTimestamp = lastTimestamp;
        // Separating the parameters
        for(WeatherObservation obs : mixedParameterList)
        {
            // Skip the irrelevant observations
            // Check for parameter name and timestamp
            if( 
                    !parameterNames.contains(obs.getElementMeasurementTypeId())
                    || obs.getTimeMeasured().compareTo(firstTimestamp) < 0
                    || (lastTimestamp != null && obs.getTimeMeasured().compareTo(lastTimestamp) > 0)
            )                
            {
                continue;
            }
            if(separatedParameters.get(obs.getElementMeasurementTypeId()) == null)
            {
                separatedParameters.put(obs.getElementMeasurementTypeId(), new ArrayList<WeatherObservation>());
            }
            
            separatedParameters.get(obs.getElementMeasurementTypeId()).add(obs);
            if(estimatedLastTimestamp == null || estimatedLastTimestamp.compareTo(obs.getTimeMeasured()) < 0)
            {
                estimatedLastTimestamp = obs.getTimeMeasured();
            }
            
        }
        // Correcting last timestamp if necessary
        lastTimestamp = lastTimestamp != null ? lastTimestamp : estimatedLastTimestamp;
            
        // Filling holes
        for(String parameterName:separatedParameters.keySet())
        {
            separatedParameters.put(parameterName,this.checkForAndFixHourlyTimeSeriesHoles(separatedParameters.get(parameterName)));
        }
        // Comparing stuff, trying to fix if not fixed
        // For precipitation, set artificial values to 0
        // For all other values, set to same as copying value
        // Maximum number of missing values at beginning or end: 3
        Integer maxMissing = 3;
        if(!areListsComplete(separatedParameters, firstTimestamp, lastTimestamp))
        {
            Calendar cal = Calendar.getInstance();
            for(String parameterName:separatedParameters.keySet())
            {
                //System.out.println("Checking " + parameterName);
                
                List<WeatherObservation> list = separatedParameters.get(parameterName);
                //System.out.println("First = " + list.get(0).getTimeMeasured());
                //System.out.println("Last = " + list.get(list.size()-1).getTimeMeasured());
                Integer counter = 0;
                while(list.get(0).getTimeMeasured().compareTo(firstTimestamp) != 0 && counter++ < maxMissing)
                {
                    cal.setTime(list.get(0).getTimeMeasured());
                    cal.add(Calendar.HOUR_OF_DAY, -1);
                    Date hourBefore = cal.getTime();
                    WeatherObservation newObs = new WeatherObservation(list.get(0));
                    newObs.setTimeMeasured(hourBefore);
                    if(newObs.getElementMeasurementTypeId().equals(WeatherElements.PRECIPITATION))
                    {
                        newObs.setValue(0.0);
                    }
                    //System.out.println("Prepending this: " + newObs);
                    list.add(0, newObs);
                }

                counter = 0;
                while(list.get(list.size()-1).getTimeMeasured().compareTo(lastTimestamp) != 0 && counter++ < maxMissing)
                {
                    cal.setTime(list.get(list.size()-1).getTimeMeasured());
                    cal.add(Calendar.HOUR_OF_DAY, 1);
                    Date hourAfter = cal.getTime();
                    WeatherObservation newObs = new WeatherObservation(list.get(list.size()-1));
                    newObs.setTimeMeasured(hourAfter);
                    if(newObs.getElementMeasurementTypeId().equals(WeatherElements.PRECIPITATION))
                    {
                        newObs.setValue(0.0);
                    }
                    //System.out.println("Appending this: " + newObs);
                    list.add(newObs);
                }
            }
        }
        
        // Last check, give up if not fixed
        if(areListsComplete(separatedParameters, firstTimestamp, lastTimestamp))
        {
            List<WeatherObservation> retVal = new ArrayList<>();
            for(String parameterName:separatedParameters.keySet())
            {
                retVal.addAll(separatedParameters.get(parameterName));
            }
            return retVal;
        }
        else
        {
            String errorMessage = "Cant' repair the weather data. Giving up now. "
                    + "Either this algorithm needs improvement, or the dataset is too incomplete. "
                    + "List sizes for the different parameters are: ";
            for(String parameterName:separatedParameters.keySet())
            {
                errorMessage += parameterName + "[Starts at " + separatedParameters.get(parameterName).get(0).getTimeMeasured() + "]: " + separatedParameters.get(parameterName).size() + "\n ";
            }
            throw new WeatherObservationListException(errorMessage);
        }
    }
    
    /**
     * Checks for duplicates, attempts to tidy up. TODO: Criteria for selection??
     * @param observations List with possible duplicates (same parameter, logInterval and timestamp)
     * @return List with unique observations, unsorted
     */
    public List<WeatherObservation> removeDuplicateWeatherObservations(List<WeatherObservation> observations)
    {
        HashMap<Long,WeatherObservation> uniqueMap = new HashMap<>();
        for(WeatherObservation observation:observations)
        {
            uniqueMap.put(observation.getValiditySignature(), observation);
        }
        return new ArrayList<WeatherObservation>(uniqueMap.values());
    }
    
    /**
     * Similar to isHomogenousTimeseries, but adds extra restriction with regards to period
     * @param separatedParameters
     * @param firstTimestamp
     * @param lastTimestamp
     * @return 
     */
    private Boolean areListsComplete(Map<String, List<WeatherObservation>> separatedParameters, Date firstTimestamp, Date lastTimestamp)
    {
        Integer size = null;
        for(String parameterName:separatedParameters.keySet())
        {
            List<WeatherObservation> currentList = separatedParameters.get(parameterName);
            Collections.sort(currentList);
            // Check for size
            if(size == null)
            {
                size = currentList.size();
            }
            if(size != currentList.size())
            {
                return Boolean.FALSE;
            }
            // Check for period
            if(!currentList.isEmpty())
            {
                if(currentList.get(0).getTimeMeasured().compareTo(firstTimestamp) != 0)
                {
                    return Boolean.FALSE;
                }
                if(lastTimestamp == null)
                {
                    lastTimestamp = currentList.get(currentList.size()-1).getTimeMeasured();
                }
                if(currentList.get(currentList.size()-1).getTimeMeasured().compareTo(lastTimestamp) != 0)
                {
                    return Boolean.FALSE;
                }
            }
        }
        return Boolean.TRUE;
    }
    
    public String dumpWeatherObservationList(List<WeatherObservation> list)
    {
        Collections.sort(list);
        StringBuilder retVal = new StringBuilder();
        for(WeatherObservation obs:list)
        {
            retVal.append(obs.getElementMeasurementTypeId()).append("[").append(obs.getTimeMeasured()).append("] : ").append(obs.getValue()).append("\n");
        }
        return retVal.toString();
    }
    
    /**
     * Searches a time series of one parameter for holes
     * @param observations
     * @param logInterval @see WeatherObservation
     * @return 
     */
    public Date findFirstHoleInObservationSeries(List<WeatherObservation> observations, Integer logInterval, TimeZone timeZone)
    {
        Collections.sort(observations);
        Date currentTime = observations.get(0).getTimeMeasured();
        Calendar cal = Calendar.getInstance(timeZone);
        for(WeatherObservation obs:observations)
        {
            if(obs.getTimeMeasured().compareTo(currentTime) != 0)
            {
                return currentTime;
            }
            cal.setTime(currentTime);
            int interval = logInterval.equals(WeatherObservation.LOG_INTERVAL_ID_1H) ? Calendar.HOUR_OF_DAY : Calendar.DATE;
            cal.add(interval, 1);
            currentTime = cal.getTime();
        }
        return null;
    }
    
    /**
     * Basic weather data time series health check. Same length? Starts and
     * ends at same time?
     * @param series
     * @return true if passed all tests
     * @throws WeatherObservationListException if at least one test fails
     */
    public boolean isHomogenousTimeSeries(List<WeatherObservation>... series) throws WeatherObservationListException
    {
        // Must be of same length
        for(List<WeatherObservation> list:series)
        {
            // If a list has no elements, it's not a time series
            if(list == null || list.isEmpty())
            {
                throw new WeatherObservationListException("At least one list is empty or NULL");
            }
            Collections.sort(list);
            // Must be of same length
            if(series[0].size() != list.size())
            {
                StringBuilder msg = new StringBuilder("Unequal length of lists.");
                for (List<WeatherObservation> serie : series) {
                    msg     .append("List with ")
                            .append(serie.get(0).getElementMeasurementTypeId())
                            .append(" length=")
                            .append(serie.size())
                            .append(".\n");
                }
                throw new WeatherObservationListException(msg.toString());
            }
            // Must be starting at same time
            if(series[0].get(0).getTimeMeasured().compareTo(list.get(0).getTimeMeasured()) != 0)
            {
                StringBuilder msg = new StringBuilder("Weather parameter lists do not start at same time");
                for (List<WeatherObservation> serie : series) {
                    msg     .append("List with ")
                            .append(serie.get(0).getElementMeasurementTypeId())
                            .append(" starts at ")
                            .append(serie.get(0).getTimeMeasured())
                            .append(".\n");
                }
                throw new WeatherObservationListException(msg.toString());
            }
            // Must be ending at same time
            int lastIndex = series[0].size() - 1;
            if(series[0].get(lastIndex).getTimeMeasured().compareTo(list.get(lastIndex).getTimeMeasured()) != 0)
            {
                StringBuilder msg = new StringBuilder("Weather parameter lists do not end at same time");
                for (List<WeatherObservation> serie : series) {
                    msg     .append("List with ")
                            .append(serie.get(lastIndex).getElementMeasurementTypeId())
                            .append(" ends at ")
                            .append(serie.get(lastIndex).getTimeMeasured())
                            .append(".\n");
                }
                throw new WeatherObservationListException(msg.toString());
            }
        }
        return true;
    }

    /**
     * Finds the last timestamp for which all parameters are present, and 
     * removes all observations after that
     * @param observations
     * @return 
     */
    public List<WeatherObservation> truncateToLastCommonObservation(List<WeatherObservation> observations) {
        // Find the latest date for each parameter
        Map<String, Date> latestObservations = new HashMap<>();
        for(WeatherObservation obs:observations)
        {
            Date latestDateForParameter = latestObservations.get(obs.getElementMeasurementTypeId());
            latestDateForParameter = latestDateForParameter == null ? obs.getTimeMeasured() : 
                    obs.getTimeMeasured().compareTo(latestDateForParameter) > 0 ? obs.getTimeMeasured() : latestDateForParameter;
            latestObservations.put(obs.getElementMeasurementTypeId(), latestDateForParameter);
        }
        
        // The earliest date is the latest common date
        Date latestCommonDate = null;
        for(Date date:latestObservations.values())
        {
            latestCommonDate = latestCommonDate == null ? date:
                    date.compareTo(latestCommonDate) < 0 ? date : latestCommonDate;
        }
        // Then we filter out all the observations after the latestCommonDate
        List<WeatherObservation> retVal = new ArrayList<>();
        for(WeatherObservation obs:observations)
        {
            if(obs.getTimeMeasured().compareTo(latestCommonDate) <= 0)
            {
                retVal.add(obs);
            }
        }
        
        return retVal;
    }
}
