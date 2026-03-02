/*
 * Copyright (c) 2014 NIBIO <http://www.nibio.no/>. 
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

package no.nibio.vips.util;

import java.util.List;

/**
 * Definitions of weather elements
 * @copyright 2017-2026 <a href="http://www.nibio.no/">NIBIO</a>
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
     * Soil temperatures at various depths
     */
    public static final String SOIL_TEMPERATURE_5CM_MEAN ="TJM5";
    public static final String SOIL_TEMPERATURE_10CM_MEAN ="TJM10";
    public static final String SOIL_TEMPERATURE_25CM_MEAN ="TJM25";
    public static final String SOIL_TEMPERATURE_50CM_MEAN ="TJM50";
    /**
     * Dew point temperature (Celcius)
     */
    public static final String DEW_POINT_TEMPERATURE = "TD";
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
    public static final String LEAF_WETNESS_DURATION = "BT";

    /**
     * Leaf wetness with sensor in fruit tree (Minutes per hour)
     */
    public static final String LEAF_WETNESS_DURATION_FRUIT_TREE = "BTf";

    /**
     * Leaf wetness measured from two combined sensors: On on the weather station and one in a fruit tree (Minutes per hour)
     */
    public static final String LEAF_WETNESS_DURATION_COMBINED_STATION_FRUIT_TREE = "BTfF";

    /**
     * Leaf wetness at ground level (Minutes per hour)
     */
    public static final String LEAF_WETNESS_DURATION_GROUND_LEVEL = "BTg";

     /**
     * All variants of leaf wetness duration (Minutes per hour) for convenience when checking for leaf wetness in a dataset
     */
    public static final List<String> LEAF_WETNESS_ALL_VARIANTS = List.of(LEAF_WETNESS_DURATION, LEAF_WETNESS_DURATION_GROUND_LEVEL, LEAF_WETNESS_DURATION_FRUIT_TREE, LEAF_WETNESS_DURATION_COMBINED_STATION_FRUIT_TREE);

    /**
     * Soil water content at various depths
     */
    public static final String SOIL_WATER_CONTENT_5CM = "VAN5";
    public static final String SOIL_WATER_CONTENT_10CM = "VAN10";
    public static final String SOIL_WATER_CONTENT_25CM = "VAN25";
    public static final String SOIL_WATER_CONTENT_50CM = "VAN50";
    /**
     * Soil conductivity at various depths
     */
    public static final String SOIL_CONDUCTIVITY_5CM = "LEJ5";
    /**
     * Average wind speed (meters / second) for the last 60 minutes, measured at 2 meter height
     */
    public static final String WIND_SPEED_2M = "FM2";    
    /**
     * Average wind speed (meters / second) for the last 60 minutes, measured at 10 meter height
     */
    public static final String WIND_SPEED_10M = "FM";
    /**
     * Average wind speed (meters / second) for the last 10 minutes, measured at 2 meter height
     */
    public static final String WIND_SPEED_10MIN_2M = "FF2";
    /**
     * Average wind speed (meters / second) for the last 10 minutes, measured at 10 meter height
     */
    public static final String WIND_SPEED_10MIN_10M = "FF";
    /**
     * Average wind direction for the last 10 minutes, measured at 2 meter height
     */
    public static final String WIND_DIR_10MIN_2M = "DD2";
    /**
     * Average wind direction for the last 10 minutes, measured at 10 meter height
     */
    public static final String WIND_DIR_10MIN_10M = "DD";
    /**
     * Potential evapotranspiration by the Penman equation
     */
    public static final String POTENTIAL_EVAPORATION = "EPP";
    
    
    
    public static final String FIXING_STRATEGY_INTERPOLATE = "INTERPOLATE";
    public static final String FIXING_STRATEGY_SET_ZERO = "SET_ZERO";
    
    public static String getNormalDataParameter(String parameterName)
    {
        return parameterName + "_NORMAL";
    }
    
}
