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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import net.ipmdecisions.model.entity.LocationResult;
import net.ipmdecisions.model.entity.ModelOutput;
import net.ipmdecisions.weather.entity.LocationWeatherData;
import net.ipmdecisions.weather.entity.WeatherData;
import no.nibio.vips.entity.Result;
import no.nibio.vips.entity.WeatherObservation;
import no.nibio.vips.observation.Observation;
import no.nibio.vips.observation.ObservationImpl;

/**
 * Util methods to transform data between IPM Decisions and VIPS formats
 * @copyright 2020 <a href="http://www.nibio.no/">NIBIO</a>
 * @author Tor-Einar Skog <tor-einar.skog@nibio.no>
 */
public class DataTransformer {
    
    public List<WeatherObservation> getVIPSWeatherData(WeatherData IPMWeatherData) throws DataTransformerException
    {
        IPMDecisionsWeatherUtil ipmUtil = new IPMDecisionsWeatherUtil();
        // Input check: Are we able to transform all the parameters?
        List<Integer> missingParamCodes = new ArrayList<>();
        for(Integer paramCode: IPMWeatherData.getWeatherParameters())
        {
            if(ipmUtil.getVIPSParameterId(paramCode) == null)
            {
                missingParamCodes.add(paramCode);
            }
        }
        if(missingParamCodes.size() > 0)
        {
            String paramStr = missingParamCodes.stream().map(p->String.valueOf(p)).collect(Collectors.joining(","));
            throw new DataTransformerException("Could not find matching internal parameter codes for: " + paramStr);
        }
        
        List<WeatherObservation> retVal = new ArrayList<>();
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

    /**
     * Convert VIPS Result list to a IPM Decisions model output object
     * @param VIPSResults
     * @return 
     */
    public ModelOutput getIPMDecisionsModelOutput(List<Result> VIPSResults){
        // Loop through once first to 
        // a) Detect the max frequency between results and 
        // b) check all possible result parameters
        Collections.sort(VIPSResults);
        Set<String> resultParameters = new HashSet<>();
        //resultParameters.add("WARNING_STATUS");
        Long minSecondsBetween = null;
        Long lastTime = null;
        for(Result r:VIPSResults)
        {
            resultParameters.addAll(r.getKeys());
            if(lastTime != null)
            {
                minSecondsBetween = minSecondsBetween != null ? Math.min(minSecondsBetween, (r.getValidTimeStart().getTime() - lastTime)/1000)
                        :(r.getValidTimeStart().getTime() - lastTime)/1000;
            }
            lastTime = r.getValidTimeStart().getTime();
        }
        
        String[] VIPSResultParameters = resultParameters.stream().toArray(String[] ::new);
        String[] IPMResultParameters = new String[VIPSResultParameters.length];
        for(int i=0;i<VIPSResultParameters.length;i++)
        {
            String[] parts = VIPSResultParameters[i].split("\\.");
            IPMResultParameters[i] = parts[parts.length-1];
        }
        
        // Build the result
        ModelOutput retVal = new ModelOutput();
        retVal.setInterval(minSecondsBetween.intValue());
        retVal.setResultParameters(IPMResultParameters);
        retVal.setTimeStart(VIPSResults.get(0).getValidTimeStart().toInstant());
        retVal.setTimeEnd(VIPSResults.get(VIPSResults.size()-1).getValidTimeStart().toInstant());
        LocationResult locationResult = new LocationResult();
        Long rows = 1 + (retVal.getTimeEnd().getEpochSecond() - retVal.getTimeStart().getEpochSecond()) / retVal.getInterval();
        Double[][] data = new Double[rows.intValue()][retVal.getResultParameters().length]; 
        Integer[] warningStatus = new Integer[rows.intValue()];
        for(Result r:VIPSResults)
        {
            // Calculate which row, based on 
            Long row = (r.getValidTimeStart().getTime()/1000 - retVal.getTimeStart().getEpochSecond()) / retVal.getInterval();
            warningStatus[row.intValue()] = r.getWarningStatus();
            // Using the ordering in the resultParameters
            for(int i=0;i<retVal.getResultParameters().length;i++)
            {
                data[row.intValue()][i] = null;
                if(VIPSResultParameters[i] != null)
                {
                    if(r.getAllValues().get(VIPSResultParameters[i]) != null && ! r.getAllValues().get(VIPSResultParameters[i]).toLowerCase().equals("null"))
                    {
                        data[row.intValue()][i] = Double.valueOf(r.getAllValues().get(VIPSResultParameters[i]));
                    }
                }
            }
        } 
        locationResult.setWarningStatus(warningStatus);
        locationResult.setData(data);
        retVal.addLocationResult(locationResult);
        return retVal;
    }

    public List<Observation> getVIPSPestObservations(List<Map> fieldObservations, List<Map> fieldObservationQuantifications) {
        List<Observation> retVal = new ArrayList<>();
        ObjectMapper om = new ObjectMapper();
        for(int i=0;i<fieldObservations.size();i++)
        {
            Map fieldObservation = fieldObservations.get(i);
            ObservationImpl VIPSObs = new ObservationImpl();
            VIPSObs.setGeoinfo(fieldObservation.get("location").toString());
            try
            {
                VIPSObs.setObservationData(om.writeValueAsString(fieldObservationQuantifications.get(i)));
            }catch(JsonProcessingException ex)
            {
                ex.printStackTrace();
            }
            VIPSObs.setTimeOfObservation(om.convertValue(fieldObservation.get("time"), Date.class));
            retVal.add(VIPSObs);
        }
        return retVal;
    }
}
