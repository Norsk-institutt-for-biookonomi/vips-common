/*
 * Copyright (c) 2017 NIBIO <http://www.nibio.no/>. 
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

package no.nibio.vips.util.test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MappingJsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import no.nibio.vips.entity.ModelConfiguration;
import no.nibio.vips.entity.PointWeatherObservationList;
import no.nibio.vips.entity.WeatherObservation;
import no.nibio.vips.model.ConfigValidationException;

/**
 * @copyright 2017 <a href="http://www.nibio.no/">NIBIO</a>
 * @author Tor-Einar Skog <tor-einar.skog@nibio.no>
 */
public class WeatherDataFileReader {


    public List<WeatherObservation> getWeatherObservationsFromFile(String weatherDataFileName) throws ConfigValidationException, JsonParseException, IOException {
        BufferedInputStream inputStream = new BufferedInputStream(this.getClass().getResourceAsStream(weatherDataFileName));
            //InputStream i = this.getClass().getResourceAsStream(weatherDataFileName);
            JsonFactory f = new MappingJsonFactory();
            JsonParser jp = f.createParser(inputStream);
            JsonNode all = jp.readValueAsTree();
            List<WeatherObservation> observations = new ArrayList<>();
            ObjectMapper mapper = new ObjectMapper();

            Date firstDate = null;
            Date lastDate = null;
            if(all.isArray())
            {
                for(JsonNode node : all){
                    Date timeMeasured = (Date)mapper.convertValue(node.get("timeMeasured").asText(), new TypeReference<Date>(){});
                    if(firstDate == null || firstDate.compareTo(timeMeasured) > 0)
                    {
                        firstDate = timeMeasured;
                    }
                    if(lastDate == null || lastDate.compareTo(timeMeasured) < 0)
                    {
                        lastDate = timeMeasured;
                    }
                    //System.out.println(node.toString());
                    WeatherObservation observation = new WeatherObservation();
                    observation.setTimeMeasured(timeMeasured);
                    observation.setLogIntervalId(node.get("logIntervalId").asInt());
                    observation.setElementMeasurementTypeId(node.get("elementMeasurementTypeId").asText());
                    observation.setValue(node.get("value").asDouble());
                    observations.add(observation);
                }

            }
            else
            {
                throw new ConfigValidationException("Data input from file is not a JSON array");
            }
            return observations;
    }

    /**
     * 
     * @param weatherDataFileName filename and path on classpath
     * @param modelId 10 chars ID of model
     * @return A Model Configuration with weather data. Can be filled with other configuration data
     * @throws ConfigValidationException 
     */
    public ModelConfiguration getModelConfigurationWithWeatherData(String weatherDataFileName, String modelId) throws ConfigValidationException
    {
        try {
            ModelConfiguration config = new ModelConfiguration();
            config.setModelId(modelId); 
            config.setConfigParameter("observations", this.getWeatherObservationsFromFile(weatherDataFileName));

            return config;
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new ConfigValidationException(ex.getMessage());
        } 
    }
    
    /**
     * 
     * 
     * @param weatherDataFileName filename and path on classpath
     * @param modelId 10 chars ID of model
     * @return A Model Configuration with weather data. Can be filled with other configuration data
     * @throws ConfigValidationException 
     */
    public ModelConfiguration getModelConfigurationWithMultiPointWeatherObservationList(String weatherDataFileName, String modelId) throws ConfigValidationException
    {
        try {
            ModelConfiguration config = new ModelConfiguration();
            config.setModelId(modelId);
            BufferedInputStream inputStream = new BufferedInputStream(this.getClass().getResourceAsStream(weatherDataFileName));
            JsonFactory f = new MappingJsonFactory();
            JsonParser jp = f.createParser(inputStream);
            ObjectMapper mapper = new ObjectMapper();
            List<PointWeatherObservationList> theList = new ArrayList<>();
            jp.nextToken(); // Jumping past the START_ARRAY
            while(jp.nextToken() != JsonToken.END_ARRAY)
            {
                PointWeatherObservationList list = jp.readValueAs(PointWeatherObservationList.class);
                theList.add(list);
                //System.out.println(jp.toString());
            }
            //JsonNode all = jp.readValueAsTree();
            
            //List<PointWeatherObservationList> theList = mapper.convertValue(all, new TypeReference<List<PointWeatherObservationList>>(){});
            config.setConfigParameter("multiPointWeatherObservations", theList);

            return config;
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new ConfigValidationException(ex.getMessage());
        } 
        
    }
}
