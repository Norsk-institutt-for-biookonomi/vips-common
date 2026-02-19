/*
 * Copyright (c) 2020 NIBIO <http://www.nibio.no/>. 
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

package net.ipmdecisions.model.entity;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @copyright 2020 <a href="http://www.nibio.no/">NIBIO</a>
 * @author Tor-Einar Skog <tor-einar.skog@nibio.no>
 */
public class ModelOutput {
    private Instant timeStart;
    private Instant timeEnd;
    private Integer interval; 
    private String[] resultParameters;
    private List<LocationResult> locationResult;

    /**
     * @return the timeStart
     */
    public Instant getTimeStart() {
        return timeStart;
    }
    
    
    /**
     * @param timeStart the timeStart to set
     */
    public void setTimeStart(Instant timeStart) {
        this.timeStart = timeStart;
    }

    /**
     * @return the timeEnd
     */
    public Instant getTimeEnd() {
        return timeEnd;
    }
    

    /**
     * @param timeEnd the timeEnd to set
     */
    public void setTimeEnd(Instant timeEnd) {
        this.timeEnd = timeEnd;
    }

    /**
     * @return the interval
     */
    public Integer getInterval() {
        return interval;
    }

    /**
     * @param interval the interval to set
     */
    public void setInterval(Integer interval) {
        this.interval = interval;
    }

    /**
     * @return the resultParameters
     */
    public String[] getResultParameters() {
        return resultParameters;
    }

    /**
     * @param resultParameters the resultParameters to set
     */
    public void setResultParameters(String[] resultParameters) {
        this.resultParameters = resultParameters;
    }

    /**
     * @return the locationResult
     */
    public List<LocationResult> getLocationResult() {
        return locationResult;
    }

    /**
     * @param locationResult the locationResult to set
     */
    public void setLocationResult(List<LocationResult> locationResult) {
        this.locationResult = locationResult;
    }
    
    public void addLocationResult(LocationResult locationResult) {
        if (this.locationResult == null)
        {
            this.locationResult = new ArrayList<>();
        }
        this.locationResult.add(locationResult);
    }
}
