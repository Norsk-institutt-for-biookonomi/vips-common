/*
 * Copyright (c) 2015 NIBIO <http://www.nibio.no/>. 
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
