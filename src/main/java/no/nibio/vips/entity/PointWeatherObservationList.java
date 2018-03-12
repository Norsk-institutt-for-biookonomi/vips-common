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
import com.vividsolutions.jts.geom.Coordinate;
import java.util.List;

/**
 * @copyright 2017 <a href="http://www.nibio.no/">NIBIO</a>
 * @author Tor-Einar Skog <tor-einar.skog@nibio.no>
 */
public class PointWeatherObservationList {
    private Coordinate coordinate;
    private List<WeatherObservation> observations;
    
    public PointWeatherObservationList(){
        
    }
    
    @JsonCreator
    public PointWeatherObservationList(
            @JsonProperty("coordinate") Coordinate coordinate, 
            @JsonProperty("observations") List<WeatherObservation> observations
    )
    {
        this.coordinate = coordinate;
        this.observations = observations;
    }

    /**
     * @return the coordinate
     */
    public Coordinate getCoordinate() {
        return coordinate;
    }

    /**
     * @param coordinate the coordinate to set
     */
    public void setCoordinate(Coordinate coordinate) {
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
    
    @Override
    public String toString()
    {
        String retVal = this.getClass().getName() + ":" + this.getCoordinate() + "\n";
        if(this.getObservations() != null)
        {
            //retVal += "Observations: \n" + this.getObservations().stream().sorted().map(obs->obs.toString()).collect(Collectors.joining("\n"));
        }
        else
        {
            retVal += "No observations";
        }
        return retVal;
    }
}
