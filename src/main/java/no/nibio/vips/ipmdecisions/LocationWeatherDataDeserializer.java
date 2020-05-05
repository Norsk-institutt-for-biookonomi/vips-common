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

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import net.ipmdecisions.weather.entity.LocationWeatherData;

/**
 * @copyright 2020 <a href="http://www.nibio.no/">NIBIO</a>
 * @author Tor-Einar Skog <tor-einar.skog@nibio.no>
 */
public class LocationWeatherDataDeserializer extends JsonDeserializer<LocationWeatherData>{


    @Override
    public LocationWeatherData deserialize(JsonParser jp, DeserializationContext dc) throws IOException, JsonProcessingException {
        ObjectCodec oc = jp.getCodec();
        JsonNode oneLocation = oc.readTree(jp);
        LocationWeatherData lwd = new LocationWeatherData(
                oneLocation.get("longitude").asDouble(),
                oneLocation.get("latitude").asDouble(),
                oneLocation.get("altitude").asDouble(),
                oneLocation.get("data").size(),
                oneLocation.get("data").get(0).size());
        
        for(int i=0; i< oneLocation.get("data").size();i++)
        {
            for(int j=0; j<oneLocation.get("data").get(0).size();j++)
            {
                lwd.setValue(i, j,oneLocation.get("data").get(i).get(j).asDouble());
            }
        }
        return lwd;
    }
}
