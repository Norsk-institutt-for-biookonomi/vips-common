/*
 * Copyright (c) 2015 NIBIO <http://www.nibio.no/>. 
 * 
 * This file is part of VIPSCommon.
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
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

    @Override
    public int compareTo(Object t) {
        return this.getTimeOfObservation().compareTo(((Observation)t).getTimeOfObservation());
    }

}
