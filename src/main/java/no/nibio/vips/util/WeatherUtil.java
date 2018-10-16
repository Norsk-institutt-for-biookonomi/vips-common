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
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.TimeZone;
import no.nibio.vips.entity.WeatherObservation;
import no.nibio.vips.model.ConfigValidationException;

/**
 * Weather related utility methods
 * @author Tor-Einar Skog <tor-einar.skog@nibio.no>
 */
public class WeatherUtil {
    
    public final static int AGGREGATION_TYPE_AVERAGE = 1;
    public final static int AGGREGATION_TYPE_SUM = 2;
    public final static int AGGREGATION_TYPE_MINIMUM = 3;
    public final static int AGGREGATION_TYPE_MAXIMUM = 4;
    public final static int AGGREGATION_TYPE_SUM_GLOBAL_RADIATION = 5;

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
            return new ArrayList<>(hourlyValues.subList(0,dateShiftIndex));
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
        // Just to be sure!
        Collections.sort(temperature);
        Collections.sort(rainfall);
        Collections.sort(relativeHumidity);
        
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
            //System.out.println("humObs timestamp=" + humObs.getTimeMeasured());
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
     * Aggregates daily values. No tolerance for duplicates
     * @param observations
     * @param timeZone
     * @param minimumObservationsPerDay
     * @param typeOfAggregation
     * @return
     * @throws WeatherObservationListException
     * @throws InvalidAggregationTypeException 
     */
    public List<WeatherObservation> getAggregatedDailyValues(
            List<WeatherObservation> observations, TimeZone timeZone, Integer minimumObservationsPerDay, Integer typeOfAggregation) 
            throws WeatherObservationListException,
            InvalidAggregationTypeException
    {
        return this.getAggregatedDailyValues(observations, timeZone, minimumObservationsPerDay, typeOfAggregation, 0);
    }
    
    /**
     * Aggregates daily values. Can be set to tolerate duplicates
     * @param observations the value of observations
     * @param timeZone the value of timeZone
     * @param minimumObservationsPerDay the value of minimumObservationsPerDay
     * @param typeOfAggregation the value of typeOfAggregation
     * @param maxDuplicates the value of maxDuplicates
     * @return the java.util.List<no.nibio.vips.entity.WeatherObservation>
     */
    public List<WeatherObservation> getAggregatedDailyValues(
            List<WeatherObservation> observations, TimeZone timeZone, Integer minimumObservationsPerDay, Integer typeOfAggregation, Integer maxDuplicates) 
            throws WeatherObservationListException,
            InvalidAggregationTypeException
    {
        if(observations == null || observations.isEmpty())
        {
            return null;
        }
        // First we organize the hourly values into one bucket per day
        Map<Date,Map> dateBucket = new HashMap<>();
        String expectedParameter = observations.get(0).getElementMeasurementTypeId();
        Date lastDate = null;
        Integer numberOfDuplicates = 0;
        List<Date> duplicateTimestamps = new ArrayList<>();
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
                hourValuesForDate = new HashMap<>();
                dateBucket.put(theDate, hourValuesForDate);
            }
            
            // Check for double entries
            // If found, we have no idea what to do with it. For now we discard it
            Double possibleDuplicate = hourValuesForDate.get(observation.getTimeMeasured());
            if(possibleDuplicate != null)
            {
                numberOfDuplicates++;
                duplicateTimestamps.add(observation.getTimeMeasured());
                if(numberOfDuplicates > maxDuplicates)
                {
                    String errorMessage =  "Found duplicate weatherObservations for parameter " +
                            observation.getElementMeasurementTypeId() + " at time(s) ";
                    for(Date duplicateTimestamp : duplicateTimestamps)
                    {
                        errorMessage += duplicateTimestamp.toString() + ", ";
                    }
                    throw new WeatherObservationListException(errorMessage);
                }
                continue;
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
                case WeatherUtil.AGGREGATION_TYPE_SUM_GLOBAL_RADIATION:
                    aggregateValue = getSum(hourValuesForADay.values()) * 0.0036; break;
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
    
    public List<Double> getObservationValues(List<WeatherObservation> observations)
    {
        List<Double> values = new ArrayList<>();
        for(WeatherObservation obs:observations)
        {
            values.add(obs.getValue());
        }
        
        return values;
    }
    
    public Double getAverage(Collection<Double> values)
    {
        return this.getSum(values)/values.size();
    }

    public Double getSum(Collection<Double> values)
    {
       Double sum = 0d;
        for(Double value:values)
        {
            sum += value;
        }
        return sum;
    }
    
    public Double getMinimum(Collection<Double> values)
    {
        Double minimum = null;
        for(Double value:values)
        {
            minimum = minimum == null ? value : Math.min(minimum, value);
        }
        return minimum;
    }
    
    public Double getMaximum(Collection<Double> values)
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
     * Slicing off minutes, seconds and milliseconds
     * @param timeStamp
     * @param timeZone
     * @return 
     */
     public Date normalizeToExactHour(Date timeStamp, TimeZone timeZone)
    {
        Calendar cal = Calendar.getInstance(timeZone);
        cal.setTime(timeStamp);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND,0);
        cal.set(Calendar.MILLISECOND,0);
        return cal.getTime();
    }
    
    /**
     * Adjusts the date so that it is a Date with the same time (hours, minutes etc.) in the new timezone
     * @param theDate
     * @param timeZone
     * @return 
     */
    public Date changeDateTimeZone(Date theDate, TimeZone oldTimeZone, TimeZone newTimeZone)
    {
        Calendar oldCal = Calendar.getInstance(oldTimeZone);
        Calendar newCal = Calendar.getInstance(newTimeZone);
        
        oldCal.setTime(theDate);
        newCal.set(oldCal.get(Calendar.YEAR), 
                oldCal.get(Calendar.MONTH), 
                oldCal.get(Calendar.DATE), 
                oldCal.get(Calendar.HOUR_OF_DAY), 
                oldCal.get(Calendar.MINUTE), 
                oldCal.get(Calendar.SECOND)
        );
        newCal.set(Calendar.MILLISECOND, oldCal.get(Calendar.MILLISECOND));
        return newCal.getTime();
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
     * <p>Requires that at least TM, RR, and UM are complete.</p>
     * 
     * @param BT Leaf wetness (minutes/hour)
     * @param TM Mean temperature (hourly mean, degrees Celcius)
     * @param RR Rainfall (mm, hourly aggregate)
     * @param FM2 Wind speed at 2m height (average, m/s)
     * @param Q0 Global radiation (unit??)
     * @param UM Relative humidity (average, %)
     * @return 
     */
    public List<WeatherObservation> calculateLeafWetnessHourSeriesBestEffort(
            List<WeatherObservation> BT, 
            List<WeatherObservation> TM, 
            List<WeatherObservation> RR, 
            List<WeatherObservation> Q0, 
            List<WeatherObservation> FM2, 
            List<WeatherObservation> UM
    ) throws ConfigValidationException {
        
        Collections.sort(BT);
        Collections.sort(TM);
        Collections.sort(RR);
        Collections.sort(Q0);
        Collections.sort(FM2);
        Collections.sort(UM);
        
        
        // If BT is same length as TM, return BT unmodified
        if(BT.size() == TM.size())
        {
            return BT;
        }
        
        // Checks all holes except missing BT at the end of the period
        List<Date[]> periodsWithMissingBT = new ArrayList<>();
        Integer BTCounter = 0;
        for(int i=0; i< TM.size() && BTCounter < BT.size();i++)
        {
            
            if(BT.get(BTCounter).getTimeMeasured().after(TM.get(i).getTimeMeasured()))
            {
                Date[] period = new Date[2];
                period[0] = TM.get(i).getTimeMeasured();
                period[1] = new XDate(BT.get(BTCounter).getTimeMeasured()).get1HourBefore();
                //System.out.println("Found missing BT at beginning. Period = " + period[0] + " - " + period[1]);
                periodsWithMissingBT.add(period);
                // Fast forward to parallell timestamps
                while(BT.get(BTCounter).getTimeMeasured().after(TM.get(i).getTimeMeasured()) && i < TM.size())
                {
                    i++;
                }
            }
            BTCounter++;
        }
        // Check if there's a gap at the end of the BT array
        if(BT.size() > 0 && BT.get(BT.size()-1).getTimeMeasured().before(TM.get(TM.size()-1).getTimeMeasured()))
        {
            //System.out.println("Found gap at end");
            Date[] period = new Date[2];
            period[0] = new XDate(BT.get(BT.size()-1).getTimeMeasured()).get1HourAfter();
            period[1] = TM.get(TM.size()-1).getTimeMeasured();
            periodsWithMissingBT.add(period);
        }
        // Check if the BT array is completely empty
        if(BT.isEmpty())
        {
            //System.out.println("Found empty BT list");
            Date[] period = new Date[2];
            period[0] = TM.get(0).getTimeMeasured();
            period[1] = TM.get(TM.size()-1).getTimeMeasured();
            periodsWithMissingBT.add(period);
        }
        
        
        // Possible TODO: Validate that TM==RR==UM
        // First: Find time and index for when to start calculating
        
        /**Date startTime, lastBTTime;
        if(BT.size() > 0)
        {
            System.out.println("Measured BT is present. Starts at: " + BT.get(0).getTimeMeasured());
            Collections.sort(BT);
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
        }*/
        
        for(Date[] period:periodsWithMissingBT)
        {
            // If all parameters are complete, we can use Nærstad's algorithm. Otherwise, if we only have UM,
            // we must resort to Trapman's algorithm
            List<WeatherObservation> calculatedBT = null;
            if(     TM != null && RR != null && Q0 != null && FM2 != null && UM != null
                    && TM.size() == RR.size() && TM.size() == Q0.size() && TM.size() == FM2.size() && TM.size() == UM.size()
                    )
            {
                // The algorithm uses the first hour of input values as "startup"
                // So if we have some leaf wetness, we can provide the first hour.
                // Otherwise, create a "dry" hour
                Date lastHourBeforePeriod = new XDate(period[0]).get1HourBefore();
                Optional<WeatherObservation> lastBTBeforePeriod = BT.stream().filter(o->o.getTimeMeasured().equals(lastHourBeforePeriod)).findFirst();
                calculatedBT = new ArrayList<>();
                if(!lastBTBeforePeriod.isPresent())
                {
                    calculatedBT.add(new WeatherObservation(
                                                    period[0],
                                                    WeatherElements.LEAF_WETNESS,
                                                    WeatherObservation.LOG_INTERVAL_ID_1H,
                                                    0d
                                                    )
                    );
                }
                
                
                calculatedBT.addAll(this.calculateLeafWetnessHourSeriesNaerstad(
                            this.getWeatherObservationsInPeriod(TM, lastHourBeforePeriod, period[1]),
                            this.getWeatherObservationsInPeriod(RR, lastHourBeforePeriod, period[1]),
                            this.getWeatherObservationsInPeriod(Q0, lastHourBeforePeriod, period[1]),
                            this.getWeatherObservationsInPeriod(FM2, lastHourBeforePeriod, period[1]),
                            this.getWeatherObservationsInPeriod(UM, lastHourBeforePeriod, period[1]),
                            lastBTBeforePeriod.isPresent() ? lastBTBeforePeriod.get() : calculatedBT.get(0) 
                        )
                );
                
            }
            else if( TM != null && RR != null && UM != null
                    && TM.size() == RR.size() && TM.size() == UM.size()
                    )
            {
                /*
                System.out.println("Trapman BT: startTime=" + startTime);
                if(BT.size() > 0)
                {
                    System.out.println("Last BT timestamp = " + BT.get(BT.size()-1).getTimeMeasured());
                }
                */
                calculatedBT = this.calculateLeafWetnessHourSeriesSimple(
                        this.getWeatherObservationsInPeriod(TM, period[0], period[1]),
                        this.getWeatherObservationsInPeriod(RR, period[0], period[1]),
                        this.getWeatherObservationsInPeriod(UM, period[0], period[1])
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
        }
        
        
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
        List<WeatherObservation> retVal = new ArrayList<>();
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
     * USpest.org's leaf wetness values range from 0 to 10. Based on explanation from
     * Len Coop, associate director of IPPC and documentation found here: http://uspest.org/wea/weanew.html#LWDOC
     * ... we simplify this to denote all USPest.org values > 2 to be considered wet hours
     * @param value
     * @return 
     */
    public Double getLeafWetnessValueFromUSPest(Double value)
    {
        return value >= 2.0 ? 60.0 : 0.0;
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
        return this.checkForAndFixHourlyTimeSeriesHoles(obsList, 3);
    }
    
    /**
     * Fixes by 
     * <ul>
     * <li>Simple interpolation for continuous parameters (temperature, humidity)</li>
     * <li>Setting values to 0 for non continuous parameters (rainfall)</li>
     * </ul>
     * Maximum number of missing values in hole: maximumMissingValues (hours)
     * @param obsList list of observations of same type (parameter)
     * @param maximumMissingValues 
     * @return 
     */
    public List<WeatherObservation> checkForAndFixHourlyTimeSeriesHoles(List<WeatherObservation> obsList, Integer maximumMissingValues) throws WeatherObservationListException {
        if(obsList == null || obsList.isEmpty())
        {
            return obsList;
        }
        //System.out.println("Number of observations before fix = " + obsList.size());
        // First we sort the list
        Collections.sort(obsList);
        Date currentTime = obsList.get(0).getTimeMeasured();
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentTime);
        //System.out.println("checkForAndFixHourlyTimeSeriesHoles " + obsList.get(0).getElementMeasurementTypeId() + ",start time= " + currentTime );
        for(int i=0;i<obsList.size();i++)
        {
            
            /*WeatherObservation debugObs = obsList.get(i);
            if(debugObs.getElementMeasurementTypeId().equals("UM"))
            {
                System.out.println(debugObs.getTimeMeasured() + ": " + debugObs.getValue() + ", currentTime=" + currentTime);
            }*/
            
            int missingValues = 0;
            while(obsList.get(i).getTimeMeasured().compareTo(currentTime) != 0 
                    && obsList.get(i).getTimeMeasured().compareTo(currentTime) > 0)
            {
                missingValues++;
                // We have a hole
                //System.out.println("Hole found! For " + obsList.get(i).getElementMeasurementTypeId() + " at " + currentTime);
                
                // Must advance time until we find a matching time
                cal.add(Calendar.HOUR_OF_DAY, 1);
                currentTime = cal.getTime();
            }
            //System.out.println("Missing values=" + missingValues);
            if(missingValues > maximumMissingValues)
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
                        //System.out.println(calculatedObservation);
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
        //System.out.println("Number of observations after fix = " + obsList.size());
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
        mixedParameterList = this.removeDuplicateWeatherObservations(mixedParameterList, null);
        
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
                separatedParameters.put(obs.getElementMeasurementTypeId(), new ArrayList<>());
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
                    + "Expected first timestamp: " + firstTimestamp + ", expected last timestamp: " + lastTimestamp + ". "
                    + "List sizes for the different parameters are: ";
            for(String parameterName:separatedParameters.keySet())
            {
                errorMessage += parameterName + "[Starts at " + separatedParameters.get(parameterName).get(0).getTimeMeasured() + "]: " + separatedParameters.get(parameterName).size() + "\n ";
            }
            throw new WeatherObservationListException(errorMessage);
        }
    }
    
    /**
     * This method simply copies values from last actual obs into holes
     * Except for aggregation values like precip, which is set to 0.0
     * @param shitIn
     * @param typeOfParameter
     * @param startDate
     * @param endDate
     * @return 
     */
    public List<WeatherObservation> fillHourlyHolesBruteForce(List<WeatherObservation> shitIn, Integer typeOfParameter, Date startDate, Date endDate)
    {
        Calendar cal = Calendar.getInstance();
        Collections.sort(shitIn);
        List<WeatherObservation> shitOut = new ArrayList<>();
        for(Integer i=0;i< shitIn.size();i++){
            WeatherObservation currentObs = shitIn.get(i);
            if(! currentObs.getTimeMeasured().after(startDate))
            {
                continue;
            }
            shitOut.add(currentObs);
            cal.setTime(currentObs.getTimeMeasured());
            cal.add(Calendar.HOUR_OF_DAY, 1);
            Date currentTime = cal.getTime();
            Date nextObsTimeMeasured = i+1 < shitIn.size() ? shitIn.get(i+1).getTimeMeasured() : endDate;
            while(currentTime.before(nextObsTimeMeasured))
            {
                
                WeatherObservation fakeObs = new WeatherObservation(currentObs);
                fakeObs.setTimeMeasured(currentTime);
                // Make sure we don't add any extra e.g. for rain
                if(typeOfParameter == WeatherUtil.AGGREGATION_TYPE_SUM)
                {
                    fakeObs.setValue(0.0);
                }
                shitOut.add(fakeObs);
                cal.setTime(currentTime);
                cal.add(Calendar.HOUR_OF_DAY, 1);
                currentTime = cal.getTime();
            }
            
        }
        
        return shitOut;
    }
    
    /**
     * Checks for duplicates, attempts to tidy up. TODO: Criteria for selection??
     * @param observations List with possible duplicates (same parameter, logInterval and timestamp)
     * @param maximumDuplicateRatio Between 0 and 1. 0 = No duplicates allowed. 1 = 100% duplicates. (Which is impossible) Default is 0.05 (5%)
     * @return List with unique observations, unsorted
     */
    public List<WeatherObservation> removeDuplicateWeatherObservations(List<WeatherObservation> observations, Double maximumDuplicateRatio) throws WeatherObservationListException
    {
        
        if(maximumDuplicateRatio == null)
        {
            maximumDuplicateRatio = 0.05;
        }
        HashMap<Long,WeatherObservation> uniqueMap = new HashMap<>();
        observations.forEach((observation) -> {
            uniqueMap.put(observation.getValiditySignature(), observation);
        });
        List<WeatherObservation> retVal = new ArrayList<>(uniqueMap.values());
        Double numberOfDuplicates = new Double(observations.size() - retVal.size());
        //System.out.println(numberOfDuplicates/observations.size());
        if(numberOfDuplicates/observations.size() > maximumDuplicateRatio)
        {
            throw new WeatherObservationListException("Too many duplicates for " + observations.get(0).getElementMeasurementTypeId() + ": " + numberOfDuplicates + "(" + (numberOfDuplicates/observations.size()) + "%)");
        }
        return retVal;
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
    
    /**
     * Finds the first and last timestamp for which all parameters are present, and 
     * removes all observations before and after that, respectively
     * @param observations
     * @return 
     */
    public List<WeatherObservation> truncateToFirstAndLastCommonObservation(List<WeatherObservation> observations) {
        // Find the earliest and latest date for each parameter
        Map<String, Date> latestObservations = new HashMap<>();
        Map<String, Date> earliestObservations = new HashMap<>();
        for(WeatherObservation obs:observations)
        {
            Date latestDateForParameter = latestObservations.get(obs.getElementMeasurementTypeId());
            latestDateForParameter = latestDateForParameter == null ? obs.getTimeMeasured() : 
                    obs.getTimeMeasured().compareTo(latestDateForParameter) > 0 ? obs.getTimeMeasured() : latestDateForParameter;
            latestObservations.put(obs.getElementMeasurementTypeId(), latestDateForParameter);
            Date earliestDateForParameter = earliestObservations.get(obs.getElementMeasurementTypeId());
            earliestDateForParameter = earliestDateForParameter == null ? obs.getTimeMeasured() :
                    obs.getTimeMeasured().compareTo(earliestDateForParameter) < 0 ? obs.getTimeMeasured() : earliestDateForParameter;
            earliestObservations.put(obs.getElementMeasurementTypeId(), earliestDateForParameter);
        }
        
        // Finding first common date
        // The latest date is the first common date
        Date earliestCommonDate = null;
        for(Date date:earliestObservations.values())
        {
            earliestCommonDate = earliestCommonDate == null ? date :
                    date.compareTo(earliestCommonDate) > 0 ? date : earliestCommonDate;
        }
        
        // Finding latest common date
        // The earliest date is the latest common date
        Date latestCommonDate = null;
        for(Date date:latestObservations.values())
        {
            latestCommonDate = latestCommonDate == null ? date:
                    date.compareTo(latestCommonDate) < 0 ? date : latestCommonDate;
        }
        // Then we filter out all the observations before the earliestCommonDate and after the latestCommonDate
        List<WeatherObservation> retVal = new ArrayList<>();
        for(WeatherObservation obs:observations)
        {
            if(obs.getTimeMeasured().compareTo(earliestCommonDate) >= 0 || obs.getTimeMeasured().compareTo(latestCommonDate) <= 0)
            {
                retVal.add(obs);
            }
        }
        
        return retVal;
    }
    
    /**
     * Extracts only the parameters that you want
     * @param observations
     * @param parameters
     * @return 
     */
    public List<WeatherObservation> filterWeatherObservationsByParameter(List<WeatherObservation> observations, Set<String> parameters){
        List<WeatherObservation> retVal = new ArrayList<>();
        for(WeatherObservation obs:observations)
        {
            if(parameters.contains(obs.getElementMeasurementTypeId()))
            {
                retVal.add(obs);
            }
        }
        return retVal;
    }
    
    /**
     * 
     * @param observations
     * @param timeZone
     * @param logintervalId
     * @param typeOfAggregation
     * @return
     * @throws WeatherObservationListException
     * @throws InvalidAggregationTypeException 
     */
    public List<WeatherObservation> getAggregateHourlyValues(List<WeatherObservation> observations, TimeZone timeZone, Integer logintervalId, Integer typeOfAggregation) throws WeatherObservationListException, InvalidAggregationTypeException{
        if(observations == null || observations.isEmpty())
        {
            return null;
        }
        // First we organize the less-than-hourly values into one bucket per hour
        Map<Date,Map> hourBucket = new HashMap<>();
        String expectedParameter = observations.get(0).getElementMeasurementTypeId();
        Date lastDate = null;
        for(WeatherObservation observation:observations)
        {
            if(!observation.getElementMeasurementTypeId().equals(expectedParameter))
            {
                throw new WeatherObservationListException("Found multiple parameters: " + observation.getElementMeasurementTypeId() + " and " + expectedParameter);
            }
            Date theDate = normalizeToExactHour(observation.getTimeMeasured(), timeZone);
            lastDate = lastDate == null ? theDate : (lastDate.compareTo(theDate) < 0 ? theDate : lastDate);
            Map<Date, Double> hourValuesForDate = hourBucket.get(theDate);
            if(hourValuesForDate == null)
            {
                hourValuesForDate = new HashMap<>();
                hourBucket.put(theDate, hourValuesForDate);
            }
            
            // Check for double entries
            // TODO: Handle DST change with double entries at 03:00
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
        for(Date anHour:hourBucket.keySet())
        {
            //System.out.println("date=" + aDay);
            Map valuesForAnHour = hourBucket.get(anHour);
            
            switch(typeOfAggregation){
                case WeatherUtil.AGGREGATION_TYPE_AVERAGE:
                    aggregateValue = getAverage(valuesForAnHour.values()); break;
                case WeatherUtil.AGGREGATION_TYPE_SUM:
                    aggregateValue = getSum(valuesForAnHour.values()); break;
                case WeatherUtil.AGGREGATION_TYPE_MINIMUM:
                    aggregateValue = getMinimum(valuesForAnHour.values()); break;
                case WeatherUtil.AGGREGATION_TYPE_MAXIMUM:
                    aggregateValue = getMaximum(valuesForAnHour.values()); break;
                default:
                    throw new InvalidAggregationTypeException(
                            "No aggregation method with id= " + typeOfAggregation  + " exists."
                            );
            }
            WeatherObservation aggregatedObservation = new WeatherObservation();
            aggregatedObservation.setElementMeasurementTypeId(templateObservation.getElementMeasurementTypeId());
            aggregatedObservation.setLogIntervalId(WeatherObservation.LOG_INTERVAL_ID_1H);
            aggregatedObservation.setTimeMeasured(anHour);
            aggregatedObservation.setValue(aggregateValue);
            aggregatedObservations.add(aggregatedObservation);
        }
        return aggregatedObservations;
    }
    
    /**
     * Adds the correct amount of time for the next time, given log interval
     * Attempts to correct if starting point is a bit off
     * @param startingPoint The timestamp to start with
     * @param logIntervalId The log frequency (see VIPSCommon's WeatherObservation entity for details)
     * @param timeZone The time zone
     * @return 
     */
    public Date getNextLogTimestamp(Date startingPoint, Integer logIntervalId, TimeZone timeZone)
    {
        Calendar cal = Calendar.getInstance(timeZone);
        cal.setTime(startingPoint);
        
        if(logIntervalId.equals(WeatherObservation.LOG_INTERVAL_ID_15M))
        {
            cal.add(Calendar.MINUTE, 15);
            cal.set(Calendar.MINUTE, cal.get(Calendar.MINUTE) - (cal.get(Calendar.MINUTE) % 15));
        }
        else if(logIntervalId.equals(WeatherObservation.LOG_INTERVAL_ID_30M))
        {
            cal.add(Calendar.MINUTE, 30);
            cal.set(Calendar.MINUTE, cal.get(Calendar.MINUTE) - (cal.get(Calendar.MINUTE) % 30));
        }
        else if(logIntervalId.equals(WeatherObservation.LOG_INTERVAL_ID_1H))
        {
            cal.add(Calendar.HOUR_OF_DAY, 1);
            cal.set(Calendar.MINUTE, 0);
        }
        else if(logIntervalId.equals(WeatherObservation.LOG_INTERVAL_ID_3H))
        {
            cal.add(Calendar.HOUR_OF_DAY, 3);
            cal.set(Calendar.MINUTE, 0);
        }
        else if(logIntervalId.equals(WeatherObservation.LOG_INTERVAL_ID_6H))
        {
            cal.add(Calendar.HOUR_OF_DAY, 6);
            cal.set(Calendar.MINUTE, 0);
        }
        else if(logIntervalId.equals(WeatherObservation.LOG_INTERVAL_ID_1D))
        {
            cal.add(Calendar.DATE,1);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
        }
        
        return cal.getTime();
    }
    
    public List<WeatherObservation> getIncrementalValuesFromAccumulated(List<WeatherObservation> accumulatedValues, TimeZone timeZone, Integer incrementalLogIntervalId)
    {
        List<WeatherObservation> retVal = new ArrayList<>();
        // We always start on the whole hour
        Calendar cal = Calendar.getInstance(timeZone);
        Collections.sort(accumulatedValues);
        Date nextIncrementalTimestamp = null;
        Double lastAccumulatedValue = null;
        for(WeatherObservation obs : accumulatedValues)
        {
            
            //System.out.println("nextIncrementalTimestamp=" + nextIncrementalTimestamp + ", lastAccumulatedValue=" + lastAccumulatedValue);
            if(lastAccumulatedValue != null)
            {
                if(nextIncrementalTimestamp.compareTo(obs.getTimeMeasured()) == 0)
                {
                    // Ensure that reset of [XX]ACC counter doesn't give negative values
                    Double incrementalValue = Math.max(obs.getValue() - lastAccumulatedValue, 0.0);
                    WeatherObservation incrementObs = new WeatherObservation();
                    incrementObs.setElementMeasurementTypeId(obs.getElementMeasurementTypeId().replace("ACC", ""));
                    incrementObs.setLogIntervalId(incrementalLogIntervalId);
                    incrementObs.setTimeMeasured(obs.getTimeMeasured());
                    incrementObs.setValue(incrementalValue);
                    retVal.add(incrementObs);
                    lastAccumulatedValue = obs.getValue();
                    nextIncrementalTimestamp = this.getNextLogTimestamp(obs.getTimeMeasured(), incrementalLogIntervalId, timeZone);
                }
                // CASE: Hole in the raw data
                else if(nextIncrementalTimestamp.compareTo(obs.getTimeMeasured()) < 0)
                {
                    nextIncrementalTimestamp = this.getNextLogTimestamp(obs.getTimeMeasured(), incrementalLogIntervalId, timeZone);
                }
            }
            // We always start on the whole hour, and in case of daily values,
            // on the hour = 00
            else
            {
                cal.setTime(obs.getTimeMeasured());
                if(
                        cal.get(Calendar.MINUTE) == 0
                        &&
                            (!incrementalLogIntervalId.equals(WeatherObservation.LOG_INTERVAL_ID_1D)
                            || cal.get(Calendar.HOUR_OF_DAY) == 0
                            )
                        )

                {
                    lastAccumulatedValue = obs.getValue();
                    nextIncrementalTimestamp = this.getNextLogTimestamp(obs.getTimeMeasured(), incrementalLogIntervalId, timeZone);
                }
            }
        }
        return retVal;
    }
    
    /**
     * Mining in list, picking the numberOfObservations last observations of the requested weatherElement
     * @param allObservations
     * @param weatherElement
     * @param numberOfObservations
     * @return 
     */
    public List<WeatherObservation> getLastObservations(List<WeatherObservation> allObservations, String weatherElement, int numberOfObservations)
    {
        PriorityQueue<WeatherObservation> lastObs = new PriorityQueue<>();
        for(WeatherObservation obs:allObservations)
        {
            if(obs.getElementMeasurementTypeId().equals(weatherElement))
            {
                lastObs.add(obs);
                if(lastObs.size() > numberOfObservations)
                {
                    lastObs.poll();
                }
            }
        }
        return new ArrayList(lastObs);
    }
    
    public List<WeatherObservation> getFirstObservations(List<WeatherObservation> allObservations, String weatherElement, int numberOfObservations)
    {
        PriorityQueue<WeatherObservation> firstObs =  new PriorityQueue<>(numberOfObservations, Collections.reverseOrder());
        for(WeatherObservation obs:allObservations)
        {
            if(obs.getElementMeasurementTypeId().equals(weatherElement))
            {
                firstObs.add(obs);
                if(firstObs.size() > numberOfObservations)
                {
                    firstObs.poll();
                }
            }
        }
        List<WeatherObservation> retVal = new ArrayList(firstObs);
        Collections.sort(retVal);
        return retVal;
    }

    public List<WeatherObservation> checkForAndFixHourlyTimeSeriesHolesMultiParameter(List<WeatherObservation> observations) throws WeatherObservationListException{
        return this.checkForAndFixHourlyTimeSeriesHolesMultiParameter(observations, 3);
    }
    
    public List<WeatherObservation> checkForAndFixHourlyTimeSeriesHolesMultiParameter(List<WeatherObservation> observations, Integer maximumMissingValues) throws WeatherObservationListException{
        List<WeatherObservation> retVal = new ArrayList<>();
        Map<String,List<WeatherObservation>> obsMap = new HashMap<>();
        for(WeatherObservation obs:observations)
        {
            List<WeatherObservation> elementObs = obsMap.get(obs.getElementMeasurementTypeId());
            if(elementObs == null)
            {
                elementObs = new ArrayList<>();
                obsMap.put(obs.getElementMeasurementTypeId(), elementObs);
            }
            
            elementObs.add( obs);
        }
        for(String elm:obsMap.keySet())
        {
            retVal.addAll(this.checkForAndFixHourlyTimeSeriesHoles(obsMap.get(elm), maximumMissingValues));
        }
        //System.out.println("fixed elements=" + retVal.size());
        return retVal;
    }
    
    /**
     * Simple renaming method. Modifies the given list and returns it
     * @param observations
     * @param oldName
     * @param newName
     * @return 
     */
    public List<WeatherObservation> renameParameter(List<WeatherObservation> observations, String oldName, String newName)
    {
        for(WeatherObservation obs : observations)
        {
            if(obs.getElementMeasurementTypeId().equals(oldName))
            {
                obs.setElementMeasurementTypeId(newName);
            }
        }
        
        return observations;
    }
    
    /**
     * Useful for conversion of e.g. solar radiation values
     * @param joulePerSquareCentimeter
     * @return 
     */
    public Double calculateAverageHourWattsPerSquareMeterFromJoulePerSquareCentimeter(Double joulePerSquareCentimeter)
    {
        // There are s10 000 square centimeters in a square meter
        // Joule is watts * seconds so average output is hourly joule / 3600 seconds
        return joulePerSquareCentimeter * 10000 / 3600;
    }
    
    /**
     * Convert from Fahrenheit to Celcius
     * 
     * @param fahrenheitTemp
     * @return 
     */
    public Double getCelciusFromFahrenheit(Double fahrenheitTemp)
    {
        return (fahrenheitTemp - 32) / 1.8;
    }
    
    /**
     * Convert from Celcius to Fahrenheit
     * 
     * @param celciusTemp
     * @return 
     */
    public Double getFahrenheitFromCelcius(Double celciusTemp)
    {
        return (celciusTemp * 1.8) + 32;
    }
    
    /**
     * 
     * @param kelvinTemp
     * @return 
     */
    public double getCelciusFromKelvin(Double kelvinTemp)
    {
        return kelvinTemp - 273.15;
    }
    
    /**
     * Convert from inches to mm
     * @param inches
     * @return 
     */
    public Double getMillimetersFromInches(Double inches)
    {
        return inches * 25.4;
    }
    
    /**
     * Convert from mm to inches
     * @param millimeters
     * @return 
     */
    public Double getInchesFromMillimeters(Double millimeters)
    {
        return millimeters / 25.4;
    }
}
