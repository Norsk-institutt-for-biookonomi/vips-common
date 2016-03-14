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

package no.nibio.vips.observation;

import java.util.Date;

/**
 * @copyright 2015 <a href="http://www.nibio.no/">NIBIO</a>
 * @author Tor-Einar Skog <tor-einar.skog@nibio.no>
 */
public class ObservationImpl implements Observation{
    
    private Date timeOfObservation;
    private String geoInfo;
    private String observationData;
    private String name;

    @Override
    public Date getTimeOfObservation() {
        return this.timeOfObservation;
    }

    @Override
    public String getObservationData(){
        return this.observationData;
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

    public void setObservationData(String observationData){
        this.observationData = observationData;
    }

    
    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getGeoinfo() {
        return this.geoInfo;
    }
    
    public void setGeoinfo(String geoInfo) {
        this.geoInfo = geoInfo;
    }

}
