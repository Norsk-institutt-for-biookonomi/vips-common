/*
 * Copyright (c) 2019 NIBIO <http://www.nibio.no/>. 
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

import java.util.Date;
import java.util.Map;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import com.bedatadriven.jackson.datatype.jts.JtsModule;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.bind.adapter.JsonbAdapter;
import no.nibio.vips.gis.GISUtil;

/**
 * @copyright 2019 <a href="http://www.nibio.no/">NIBIO</a>
 * @author Tor-Einar Skog <tor-einar.skog@nibio.no>
 */
public class ResultJsonbAdapter implements JsonbAdapter<Result, JsonObject>{

    @Override
    public JsonObject adaptToJson(Result original) throws Exception {
        System.out.println("adaptToJson!");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JtsModule());
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("validTimeStart", objectMapper.writeValueAsString(original.getValidTimeStart()).replaceAll("\"", ""));
        builder.add("validTimeEnd", objectMapper.writeValueAsString(original.getValidTimeStart()).replaceAll("\"", ""));
        System.out.println("adaptToJson1");
        String geoJSON = original.getValidGeometry() == null ? "null" 
                : original.getValidGeometry().getGeometryType().equals("Point") ? objectMapper.writeValueAsString((Point) original.getValidGeometry())
                : original.getValidGeometry().getGeometryType().equals("LineString") ? objectMapper.writeValueAsString((LineString) original.getValidGeometry())
                : original.getValidGeometry().getGeometryType().equals("Polygon") ? objectMapper.writeValueAsString((Polygon) original.getValidGeometry())
                : "null";
        builder.add("validGeometry", geoJSON);
        System.out.println("adaptToJson2");
        builder.add("warningStatus", original.getWarningStatus());
        System.out.println("adaptToJson3");
        builder.add("allValues", objectMapper.writeValueAsString(original.getAllValues()));
        System.out.println("adaptToJson4");
        builder.add("keys", objectMapper.writeValueAsString(original.getKeys()));
        System.out.println("adaptToJson5");
        return builder.build();
    }

    @Override
    public Result adaptFromJson(JsonObject adapted) throws Exception {
        System.out.println("adaptFromJson!");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JtsModule());
        Result r = new ResultImpl();

        
        r.setValidTimeStart(objectMapper.convertValue(adapted.getString("validTimeStart"), Date.class));
        r.setValidTimeEnd(objectMapper.convertValue(adapted.getString("validTimeEnd"), Date.class));
        System.out.println("adaptFromJson1");
        if(adapted.get("allValues") != null)
        {
            r.setAllValues(objectMapper.readValue(adapted.getString("allValues"), Map.class));
            System.out.println("adaptFromJson2");
        }
        r.setWarningStatus(adapted.getInt("warningStatus"));
        //System.out.println(adapted.getString("validGeometry"));
        

        Geometry validGeometry = null;
        //System.out.println(geomNode);
        if(!adapted.isNull("validGeometry"))
        {
            JsonObject geomNode = adapted.getValue("validGeometry").asJsonObject();
            switch(geomNode.getString("type")){
                case "Point":
                    validGeometry = objectMapper.convertValue(adapted.getString("validGeometry"), Point.class);
                    break;
                case "Polygon":
                    validGeometry = objectMapper.convertValue(adapted.getString("validGeometry"), Polygon.class);
                    break;
                case "LineString":
                    validGeometry = objectMapper.convertValue(adapted.getString("validGeometry"), LineString.class);
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
