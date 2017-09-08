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

package no.nibio.vips.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import no.nibio.vips.gis.SimpleWGS84Coordinate;

/**
 * @copyright 2017 <a href="http://www.nibio.no/">NIBIO</a>
 * @author Tor-Einar Skog <tor-einar.skog@nibio.no>
 */
public class MultiPointWeatherObservationList {
    private SimpleWGS84Coordinate coordinate;
    private List<WeatherObservation> observations;
    
    @JsonCreator
    public MultiPointWeatherObservationList(
            @JsonProperty("coordinate") SimpleWGS84Coordinate coordinate, 
            @JsonProperty("observations") List<WeatherObservation> observations
    )
    {
        this.coordinate = coordinate;
        this.observations = observations;
    }

    /**
     * @return the coordinate
     */
    public SimpleWGS84Coordinate getCoordinate() {
        return coordinate;
    }

    /**
     * @param coordinate the coordinate to set
     */
    public void setCoordinate(SimpleWGS84Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    /**
     * @return the observations
     */
    public List<WeatherObservation> getObservations() {
        return observations;
    }

    /**
     * @param observations the observations to set
     */
    public void setObservations(List<WeatherObservation> observations) {
        this.observations = observations;
    }
    
}
