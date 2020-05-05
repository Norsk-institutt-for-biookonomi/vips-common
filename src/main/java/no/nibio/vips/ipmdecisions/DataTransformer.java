/*
 * Copyright (c) 2020 NIBIO <http://www.nibio.no/>. 
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

package no.nibio.vips.ipmdecisions;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import net.ipmdecisions.weather.entity.LocationWeatherData;
import net.ipmdecisions.weather.entity.WeatherData;
import no.nibio.vips.entity.WeatherObservation;

/**
 * @copyright 2020 <a href="http://www.nibio.no/">NIBIO</a>
 * @author Tor-Einar Skog <tor-einar.skog@nibio.no>
 */
public class DataTransformer {
    
    public List<WeatherObservation> getVIPSWeatherData(WeatherData IPMWeatherData)
    {
        List<WeatherObservation> retVal = new ArrayList<>();
        IPMDecisionsWeatherUtil ipmUtil = new IPMDecisionsWeatherUtil();
        Integer logIntervalId = IPMWeatherData.getInterval().equals(3600) ? WeatherObservation.LOG_INTERVAL_ID_1H
                : WeatherObservation.LOG_INTERVAL_ID_1D;
        LocationWeatherData oneLocation = IPMWeatherData.getLocationWeatherData().get(0);
        Double[][] data = oneLocation.getData();
        Instant currentTimeStamp = IPMWeatherData.getTimeStart();
        for(int i=0; i<data.length; i++)
        {
            // Calculate the timeMeasured for this row
            
            for(int j=0; j<data[i].length;j++)
            {
                WeatherObservation VIPSObs = new WeatherObservation();
                VIPSObs.setTimeMeasured(Date.from(currentTimeStamp));
                VIPSObs.setLogIntervalId(logIntervalId);
                VIPSObs.setElementMeasurementTypeId(ipmUtil.getVIPSParameterId(IPMWeatherData.getWeatherParameters()[j]));
                VIPSObs.setValue(data[i][j]);
                retVal.add(VIPSObs);
            }
            currentTimeStamp = currentTimeStamp.plusSeconds(IPMWeatherData.getInterval());
        }
        return retVal;
    }

}
