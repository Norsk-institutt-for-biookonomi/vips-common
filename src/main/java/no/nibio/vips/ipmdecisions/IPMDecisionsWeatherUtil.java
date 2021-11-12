/*
 * Copyright (c) 2020 NIBIO <http://www.nibio.no/>. 
 * 
 * This file is part of LMTServices.
 * LMTServices is free software: you can redistribute it and/or modify
 * it under the terms of the NIBIO Open Source License as published by 
 * NIBIO, either version 1 of the License, or (at your option) any
 * later version.
 * 
 * LMTServices is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * NIBIO Open Source License for more details.
 * 
 * You should have received a copy of the NIBIO Open Source License
 * along with LMTServices.  If not, see <http://www.nibio.no/licenses/>.
 * 
 */

package no.nibio.vips.ipmdecisions;

import java.util.HashMap;
import java.util.Map;
import static java.util.Map.entry;
import no.nibio.vips.util.WeatherElements;

/**
 * @copyright 2020 <a href="http://www.nibio.no/">NIBIO</a>
 * @author Tor-Einar Skog <tor-einar.skog@nibio.no>
 */
public class IPMDecisionsWeatherUtil {
    private final Map<String, Integer> VIPSToIPM = Map.of(
            WeatherElements.TEMPERATURE_MEAN,1002, // TM
            WeatherElements.PRECIPITATION, 2001, // RR
            WeatherElements.RELATIVE_HUMIDITY_MEAN, 3002, // UM
            WeatherElements.GLOBAL_RADIATION, 5001,// Q0
            WeatherElements.LEAF_WETNESS_DURATION, 3101 // BT
    );
    
    // Has more than 10 entries, so must be initalized in the class constructor
    private final Map<Integer, String> IPMToVIPS = Map.ofEntries(
            entry(1001,WeatherElements.TEMPERATURE_MEAN), // TM (Actually TT...)
            entry(1002,WeatherElements.TEMPERATURE_MEAN), // TM
            entry(1901, WeatherElements.DEW_POINT_TEMPERATURE), // DT
            entry(2001, WeatherElements.PRECIPITATION), // RR
            entry(3001, WeatherElements.RELATIVE_HUMIDITY_MEAN), // UU/UM
            entry(3002, WeatherElements.RELATIVE_HUMIDITY_MEAN), // UM
            entry(3101, WeatherElements.LEAF_WETNESS_DURATION), // BT
            entry(4002, WeatherElements.WIND_SPEED_2M), // FM2/FF2
            entry(4003, WeatherElements.WIND_SPEED_2M), // FM2/FF2
            entry(4012, WeatherElements.WIND_SPEED_10M), // FM10/FF10
            entry(4013, WeatherElements.WIND_SPEED_10M), // FM10/FF10
            entry(5001, WeatherElements.GLOBAL_RADIATION) // Q0       
    ) ; 

    public Integer getIPMParameterId(String VIPSParameterId)
    {
        return this.VIPSToIPM.get(VIPSParameterId);
    }
    
    public String getVIPSParameterId(Integer IPMParameterId)
    {
        return this.IPMToVIPS.get(IPMParameterId);
    }
    
    //public WeatherData deserializeWeatherData(Object)
}
