/*
 * Copyright (c) 2014 Bioforsk <http://www.bioforsk.no/>. 
 * 
 * This file is part of VIPSCommon.
 * VIPSCommon is free software: you can redistribute it and/or modify
 * it under the terms of the Bioforsk Open Source License as published by 
 * Bioforsk, either version 1 of the License, or (at your option) any
 * later version.
 * 
 * VIPSCommon is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * Bioforsk Open Source License for more details.
 * 
 * You should have received a copy of the Bioforsk Open Source License
 * along with VIPSCommon.  If not, see <http://www.bioforsk.no/licenses/>.
 * 
 */

package no.bioforsk.vips.util;

/**
 * (Probably) temporary definitions of weather elements
 * @copyright 2013 <a href="http://www.bioforsk.no/">Bioforsk</a>
 * @author Tor-Einar Skog <tor-einar.skog@bioforsk.no>
 */
public class WeatherElements {
    
    /**
     * Mean temperature (Celcius)
     */
    public static final String TEMPERATURE_MEAN = "TM";
    /**
     * Minimum temperature (Celcius)
     */
    public static final String TEMPERATURE_MINIMUM = "TN";
    /**
     * Maximum temperature (Celcius)
     */
    public static final String TEMPERATURE_MAXIMUM = "TX";
    /**
     * Aggregated rainfall (millimeters)
     */
    public static final String PRECIPITATION = "RR";
    /**
     * Relative humidity (%)
     */
    public static final String RELATIVE_HUMIDITY = "UM";
    /**
     * Global radiation (unit??)
     */
    public static final String GLOBAL_RADIATION = "Q0";
    /**
     * Leaf wetness (Minutes per hour)
     */
    public static final String LEAF_WETNESS = "BT";
    /**
     * 
     */
    public static final String WIND_SPEED_2M = "FM2";
    
    public static final String FIXING_STRATEGY_INTERPOLATE = "INTERPOLATE";
    public static final String FIXING_STRATEGY_SET_ZERO = "SET_ZERO";
    
    
}
