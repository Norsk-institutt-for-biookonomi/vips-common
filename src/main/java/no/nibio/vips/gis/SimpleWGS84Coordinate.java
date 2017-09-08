/*
 * Copyright (c) 2017 NIBIO <http://www.nibio.no/>. 
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

package no.nibio.vips.gis;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This is for multi point data serialization purposes
 * @copyright 2017 <a href="http://www.nibio.no/">NIBIO</a>
 * @author Tor-Einar Skog <tor-einar.skog@nibio.no>
 */

public class SimpleWGS84Coordinate {
    
    private Double latitude, longitude, altitude;
    @JsonCreator
    public SimpleWGS84Coordinate(
            @JsonProperty("latitude") Double latitude, 
            @JsonProperty("longitude") Double longitude, 
            @JsonProperty("altitude") Double altitude
    )
    {
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
    }
    
    public SimpleWGS84Coordinate(Double latitude, Double longitude)
    {
        this(latitude, longitude, null);
    }

    /**
     * @return the latitude
     */
    public Double getLatitude() {
        return latitude;
    }

    /**
     * @param latitude the latitude to set
     */
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    /**
     * @return the longitude
     */
    public Double getLongitude() {
        return longitude;
    }

    /**
     * @param longitude the longitude to set
     */
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    /**
     * @return the altitude
     */
    public Double getAltitude() {
        return altitude;
    }

    /**
     * @param altitude the altitude to set
     */
    public void setAltitude(Double altitude) {
        this.altitude = altitude;
    }
    
    @Override
    public String toString()
    {
        return this.getClass().getName() + " lat=" + this.getLatitude() + ", lon=" + this.getLongitude() + ", alt=" + this.getAltitude();
    }
    
}
