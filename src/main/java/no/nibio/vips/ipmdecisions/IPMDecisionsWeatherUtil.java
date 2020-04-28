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

import java.util.Map;
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
    
    private final Map<Integer, String> IPMToVIPS = Map.of(
            1002,WeatherElements.TEMPERATURE_MEAN, // TM
            2001, WeatherElements.PRECIPITATION, // RR
            3002, WeatherElements.RELATIVE_HUMIDITY_MEAN, // UM
            5001, WeatherElements.GLOBAL_RADIATION, // Q0
            3101, WeatherElements.LEAF_WETNESS_DURATION // BT
    );
    
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
