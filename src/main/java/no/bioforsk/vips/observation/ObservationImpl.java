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

package no.bioforsk.vips.observation;

import java.util.Date;

/**
 * @copyright 2015 <a href="http://www.nibio.no/">NIBIO</a>
 * @author Tor-Einar Skog <tor-einar.skog@nibio.no>
 */
public class ObservationImpl implements Observation{
    
    private Date timeOfObservation;
    private Double latitude;
    private Double longitude;
    private Double observedValue;
    private Integer denominator;
    private String name;

    @Override
    public Date getTimeOfObservation() {
        return this.timeOfObservation;
    }

    @Override
    public Double getLatitude() {
        return this.latitude;
    }

    @Override
    public Double getLongitude() {
        return this.longitude;
    }

    @Override
    public Double getObservedValue() {
        return this.observedValue;
    }

    @Override
    public Integer getDenominator() {
        return this.denominator;
    }

    @Override
    public String getName() {
        return this.name;
    }

    /**
     * @param timeOfObservation the timeOfObservation to set
     */
    public void setTimeOfObservation(Date timeOfObservation) {
        this.timeOfObservation = timeOfObservation;
    }

    /**
     * @param latitude the latitude to set
     */
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    /**
     * @param longitude the longitude to set
     */
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    /**
     * @param observedValue the observedValue to set
     */
    public void setObservedValue(Double observedValue) {
        this.observedValue = observedValue;
    }

    /**
     * @param denominator the denominator to set
     */
    public void setDenominator(Integer denominator) {
        this.denominator = denominator;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

}
