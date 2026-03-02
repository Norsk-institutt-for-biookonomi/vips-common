/*
 * Copyright (c) 2017 NIBIO <http://www.nibio.no/>. 
 *
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

package no.nibio.vips.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
//import org.locationtech.jts.geom.Coordinate;
import no.nibio.vips.gis.CoordinateProxy;
import java.util.List;

/**
 * @copyright 2017 <a href="http://www.nibio.no/">NIBIO</a>
 * @author Tor-Einar Skog <tor-einar.skog@nibio.no>
 */
public class PointWeatherObservationList {
    private CoordinateProxy coordinate;
    private List<WeatherObservation> observations;
    private String timeZone;
    
    public PointWeatherObservationList(){
        
    }
    
    @JsonCreator
    public PointWeatherObservationList(
            @JsonProperty("coordinate") CoordinateProxy coordinate, 
            @JsonProperty("observations") List<WeatherObservation> observations,
            @JsonProperty("timeZone") String timeZone
    )
    {
        this.coordinate = coordinate;
        this.observations = observations;
        this.timeZone = timeZone;
    }

    /**
     * @return the coordinate
     */
    public CoordinateProxy getCoordinate() {
        return coordinate;
    }

    /**
     * @param coordinate the coordinate to set
     */
    public void setCoordinate(CoordinateProxy coordinate) {
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

    /**
     * @return the timeZone
     */
    public String getTimeZone() {
        return timeZone;
    }

    /**
     * @param timeZone the timeZone to set
     */
    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }
}
