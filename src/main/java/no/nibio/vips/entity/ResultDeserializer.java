/*
 * Copyright (c) 2018 NIBIO <http://www.nibio.no/>. 
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

package no.nibio.vips.entity;

import com.bedatadriven.jackson.datatype.jts.JtsModule;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import java.io.IOException;
import java.util.Date;
import java.util.Map;
import no.nibio.vips.gis.GISUtil;

/**
 * @copyright 2018 <a href="http://www.nibio.no/">NIBIO</a>
 * @author Tor-Einar Skog <tor-einar.skog@nibio.no>
 */
public class ResultDeserializer extends StdDeserializer<Result>{

    public ResultDeserializer(){
        this(null);
    }
    
    public ResultDeserializer(Class<?>vc){
        super(vc);
    }
    
    @Override
    public Result deserialize(JsonParser jp, DeserializationContext dc) throws IOException, JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JtsModule());
        Result r = new ResultImpl();
        //System.out.println(jp.getText());
        JsonNode node = jp.getCodec().readTree(jp);
        //System.out.println(node);
        r.setValidTimeStart(objectMapper.convertValue(node.get("validTimeStart"), Date.class));
        r.setValidTimeEnd(objectMapper.convertValue(node.get("validTimeEnd"), Date.class));
        if(node.get("allValues") != null)
        {
            r.setAllValues(objectMapper.readValue(node.get("allValues").asText(), Map.class));
        }
        r.setWarningStatus(node.get("warningStatus").asInt());
        JsonNode geomNode = node.get("validGeometry");
        Geometry validGeometry = null;
        //System.out.println(geomNode);
        if(geomNode != null && geomNode.get("type") != null)
        {
            switch(geomNode.get("type").asText()){
                case "Point":
                    validGeometry = objectMapper.convertValue(geomNode, Point.class);
                    break;
                case "Polygon":
                    validGeometry = objectMapper.convertValue(geomNode, Polygon.class);
                    break;
                case "LineString":
                    validGeometry = objectMapper.convertValue(geomNode, LineString.class);
                    break;
                default:
                    validGeometry = null;
            }
            if(validGeometry.getSRID() <= 0)
            {
                validGeometry.setSRID(GISUtil.DEFAULT_SRID);
            }
        }
        
        r.setValidGeometry(validGeometry);
        //System.out.println(r.getValidGeometry());
        return r;
    }

}
