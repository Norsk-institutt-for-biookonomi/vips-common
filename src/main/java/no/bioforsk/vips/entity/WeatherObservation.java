/*
 * Copyright (c) 2014 Bioforsk <http://www.bioforsk.no/>. 
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

package no.bioforsk.vips.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Date;

/**
 * Data object that represents a weather observation. Could be measured (historical
 * and forecasted (future).
 * @copyright 2013 <a href="http://www.bioforsk.no/">Bioforsk</a>
 * @author  Tor-Einar Skog <tor-einar.skog@bioforsk.no>
 */
public class WeatherObservation implements Comparable{
    
    public final static Integer LOG_INTERVAL_ID_30M = 4;
    public final static Integer LOG_INTERVAL_ID_1H = 1;
    public final static Integer LOG_INTERVAL_ID_3H = 3;
    public final static Integer LOG_INTERVAL_ID_6H = 6;
    public final static Integer LOG_INTERVAL_ID_1D = 2;

    private String elementMeasurementTypeId;
    private Integer logIntervalId;
    private Date timeMeasured;
    private double value;
   
    /** Creates a new instance of WeatherObservation */
    public WeatherObservation() {
    }
    
    /**
     * Constructs a new observation with same properties as original
     * @param original 
     */
    public WeatherObservation(WeatherObservation original)
    {
        this.setElementMeasurementTypeId(original.getElementMeasurementTypeId());
        this.setLogIntervalId(original.getLogIntervalId());
        this.setTimeMeasured(original.getTimeMeasured());
        this.setValue(original.getValue());
    }
    
    /**
     * 
     * @param timeMeasured
     * @param elementMeasurementTypeId
     * @param logIntervalId
     * @param value 
     */
    public WeatherObservation(Date timeMeasured, String elementMeasurementTypeId, Integer logIntervalId, Double value){
        this.timeMeasured = timeMeasured;
        this.elementMeasurementTypeId = elementMeasurementTypeId;
        this.logIntervalId = logIntervalId;
        this.value = value;
    }
        
    public final void setTimeMeasured(Date timeMeasured) { this.timeMeasured = timeMeasured; }
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ssXXX", timezone="UTC")
    public Date getTimeMeasured() { return this.timeMeasured; }
    
    public final void setElementMeasurementTypeId(String elementMeasurementTypeId) { 
        this.elementMeasurementTypeId = elementMeasurementTypeId; 
    }
    public String getElementMeasurementTypeId() { 
        return this.elementMeasurementTypeId; 
    }
   
    public final void setLogIntervalId(Integer id) { this.logIntervalId = id.intValue(); }
    public Integer getLogIntervalId() { return this.logIntervalId; }
    
    /**
     * Compares by time measured in ascending order
     */
    @Override
    public int compareTo(Object o) {
        WeatherObservation other = (WeatherObservation) o;
        return this.compareTo(other);
        
    }
    
    /**
     * Compares by time measured in ascending order
     * @param other the observation to compare to
     * @return
     */
    public int compareTo(WeatherObservation other)
    {
    	if(this.getTimeMeasured() == null && other.getTimeMeasured() == null)
            return 0;
        else if(this.getTimeMeasured() == null)
            return -1;
        else if(other.getTimeMeasured() == null)
            return 1;
        else
            return this.getTimeMeasured().compareTo(other.getTimeMeasured());
    }
    
    @Override
    public String toString()
    {
        return "Observation \n" +
                "elementMeasurementTypeId=" + this.getElementMeasurementTypeId() + "\n" +
                "timeMeasured=" + this.getTimeMeasured().toString() + "\n" +
                "logIntervalId=" + this.getLogIntervalId().toString() + "\n" +
                "value=" + this.getValue();
    }

    /**
     * @return the value
     */
    public double getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public final void setValue(double value) {
        this.value = value;
    }
    
    @JsonIgnore
    public long getValiditySignature()
    {
        long result = 17;
        result = result * this.getLogIntervalId();
        result = result + this.getTimeMeasured().getTime();
        for(char c:this.getElementMeasurementTypeId().toCharArray())
        {
            result = result * c;
        }
        return result;
    }
    
    
    /**
     * Test if the provided WeatherObservation is for the same time, resolution and parameter
     * @param other
     * @return 
     */
    public boolean isDuplicate(WeatherObservation other)
    {
        if(other == null)
        {
            return false;
        }
        return this.getValiditySignature() == other.getValiditySignature();
    }
}
