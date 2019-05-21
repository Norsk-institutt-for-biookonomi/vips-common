/*
 * Copyright (c) 2019 NIBIO <http://www.nibio.no/>. 
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
