/*
 * Copyright (c) 2015 NIBIO <http://www.nibio.no/>. 
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

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;
import no.nibio.vips.entity.WeatherObservation;

/**
 * @copyright 2015 <a href="http://www.nibio.no/">NIBIO</a>
 * @author Tor-Einar Skog <tor-einar.skog@nibio.no>
 */
public class CSVPrintUtil {

    public String printWeatherObservations(List<WeatherObservation> observations, TimeZone timeZone, String separator)
    {
        separator = separator == null ? "," : separator;
        String retVal = "Time" + separator + "Parameter" + separator + "LogIntervalId" + separator + "Value\n";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssXXX");
        format.setTimeZone(timeZone);
        for(WeatherObservation observation:observations)
        {
            retVal += format.format(observation.getTimeMeasured()) + separator 
                    + observation.getElementMeasurementTypeId() + separator 
                    + observation.getLogIntervalId() + separator 
                    + observation.getValue() + "\n";
        }
        return retVal;
    }
}
