/*
 * Copyright (c) 2014 NIBIO <http://www.nibio.no/>. 
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

package no.nibio.vips.util;

/**
 * (Probably) temporary definitions of weather elements
 * @copyright 2013 <a href="http://www.nibio.no/">NIBIO</a>
 * @author Tor-Einar Skog <tor-einar.skog@nibio.no>
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
     * Instantaneous temperature (Celcius)
     */
    public static final String TEMPERATURE_INSTANTANEOUS = "TT";
    /**
     * Soil temperature at 5cm, mean
     */
    public static final String SOIL_TEMPERATURE_5CM_MEAN ="TJM5";
    /**
     * Soil temperature at 10cm, mean
     */
    public static final String SOIL_TEMPERATURE_10CM_MEAN ="TJM10";
    /**
     * Aggregated rainfall (millimeters)
     */
    public static final String PRECIPITATION = "RR";
    /**
     * Average relative humidity (%)
     */
    public static final String RELATIVE_HUMIDITY_MEAN = "UM";
    /**
     * Instantaneous relative humidity (%)
     */
    public static final String RELATIVE_HUMIDITY_INSTANTANEOUS = "UU";
    /**
     * Global radiation (W/sqm)
     */
    public static final String GLOBAL_RADIATION = "Q0";
    /**
     * Immmediate calculated clear sky solar radiation (clear sky)
     */
    public static final String GLOBAL_RADIATION_CLEAR_SKY_CALCULATED = "Q0c";
    /**
     * Leaf wetness (Minutes per hour)
     */
    public static final String LEAF_WETNESS = "BT";
    /**
     * Average wind speed (meters / second)
     */
    public static final String WIND_SPEED_2M = "FM2";
    
    public static final String POTENTIAL_EVAPORATION = "EPP";
    
    
    
    public static final String FIXING_STRATEGY_INTERPOLATE = "INTERPOLATE";
    public static final String FIXING_STRATEGY_SET_ZERO = "SET_ZERO";
    
    public static String getNormalDataParameter(String parameterName)
    {
        return parameterName + "_NORMAL";
    }
    
}
