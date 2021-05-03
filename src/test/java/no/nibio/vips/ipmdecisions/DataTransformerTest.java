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

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MappingJsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import static junit.framework.Assert.fail;
import junit.framework.TestCase;
import net.ipmdecisions.weather.entity.LocationWeatherData;
import net.ipmdecisions.weather.entity.WeatherData;
import no.nibio.vips.entity.WeatherObservation;

/**
 *
 * @author treinar
 */
public class DataTransformerTest extends TestCase {
    
    public DataTransformerTest(String testName) {
        super(testName);
    }
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }
    
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of getVIPSWeatherData method, of class DataTransformer.
     */
    public void testGetVIPSWeatherData() {
        System.out.println("getVIPSWeatherData");
        WeatherData IPMWeatherData = this.getWeatherData("/IPMData_hourly.json");
        DataTransformer instance = new DataTransformer();
        try
        {
            List<WeatherObservation> result = instance.getVIPSWeatherData(IPMWeatherData);
            result.forEach(obs->System.out.println(obs));
            assertEquals(IPMWeatherData.getLocationWeatherData().get(0).getLength() * IPMWeatherData.getLocationWeatherData().get(0).getWidth(), result.size());
        }catch(DataTransformerException ex)
        {
            fail(ex.getMessage());
        }
 
    }
    
    private WeatherData getWeatherData(String fileName)
    {
        try
        {
        BufferedInputStream inputStream = new BufferedInputStream(this.getClass().getResourceAsStream(fileName));
            
            JsonFactory f = new MappingJsonFactory();
            JsonParser jp = f.createParser(inputStream);
            JsonNode all = jp.readValueAsTree();
            WeatherData weatherData = new WeatherData();
            ObjectMapper mapper = new ObjectMapper();
            //mapper.convertValue(all, new TypeReference<WeatherData>(){});

            weatherData.setTimeStart(((Date)mapper.convertValue(all.get("timeStart"), new TypeReference<Date>(){})).toInstant());
            weatherData.setTimeEnd(((Date)mapper.convertValue(all.get("timeEnd"), new TypeReference<Date>(){})).toInstant());
            
            weatherData.setInterval(all.get("interval").asInt());
            Integer[] params = new Integer[all.get("weatherParameters").size()];
            for(int i=0; i< all.get("weatherParameters").size();i++){
                params[i] = all.get("weatherParameters").get(i).asInt();
            }
            weatherData.setWeatherParameters(params);
            JsonNode oneLocation = all.get("locationWeatherData").get(0);
            
            LocationWeatherData lwd = new LocationWeatherData(0.0,0.0,0.0,oneLocation.get("data").size(),params.length);
            for(int i=0; i< oneLocation.get("data").size();i++)
            {
                for(int j=0; j<params.length;j++)
                {
                    lwd.setValue(i, j,oneLocation.get("data").get(i).get(j).asDouble());
                }
            }
            
            weatherData.addLocationWeatherData(lwd);
            
            System.out.println(mapper.writeValueAsString(weatherData));
            
            return weatherData;
        }
        catch(IOException ex)
        {
            return null;
        }
    }
    
}
