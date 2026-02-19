/*
 * Copyright (c) 2019 NIBIO <http://www.nibio.no/>. 
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

package no.nibio.vips.gis;

/**
 * @copyright 2019 <a href="http://www.nibio.no/">NIBIO</a>
 * @author Tor-Einar Skog <tor-einar.skog@nibio.no>
 */
public class CoordinateProxy extends org.locationtech.jts.geom.Coordinate{
    
    public CoordinateProxy()
    {
        super();
    }
    
    public CoordinateProxy(Double longitude, Double latitude)
    {
        super(longitude,latitude);
    }
    
    public CoordinateProxy(Double longitude, Double latitude, Double altitude)
    {
        super(longitude,latitude,altitude);
    }
    
    /**
     * We needed to override this in order to not break deserializing with Jackson. Phew....
     * @param m 
     */
    @Override 
    public void setM(double m)
    {
        
    }

}
