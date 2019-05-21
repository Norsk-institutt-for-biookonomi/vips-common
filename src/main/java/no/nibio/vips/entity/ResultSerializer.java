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
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import java.io.IOException;
import no.nibio.vips.gis.GISUtil;

/**
 * Ref http://www.baeldung.com/jackson-custom-serialization
 * @copyright 2018 <a href="http://www.nibio.no/">NIBIO</a>
 * @author Tor-Einar Skog <tor-einar.skog@nibio.no>
 */
class ResultSerializer extends StdSerializer<ResultImpl>{
    ObjectMapper objectMapper;
    GeometryFactory gf;
    GISUtil gisUtil;
    public ResultSerializer() {
        this(null);
        this.gisUtil = new GISUtil();
    }
   
    public ResultSerializer(Class<ResultImpl> t) {
        super(t);
        this.objectMapper = new ObjectMapper();
        this.objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        this.objectMapper.registerModule(new JtsModule());
    }
    
    @Override
    public void serialize(ResultImpl t, JsonGenerator jg, SerializerProvider sp) throws IOException {
        jg.writeStartObject();
        jg.writeStringField("validTimeStart", this.objectMapper.writeValueAsString(t.getValidTimeStart()).replaceAll("\"", ""));
        jg.writeStringField("validTimeEnd", this.objectMapper.writeValueAsString(t.getValidTimeEnd()).replaceAll("\"", ""));
        String geoJSON = t.getValidGeometry() == null ? "null" 
                : t.getValidGeometry().getGeometryType().equals("Point") ? objectMapper.writeValueAsString(this.gisUtil.getVividPoint((Point) t.getValidGeometry()))
                : t.getValidGeometry().getGeometryType().equals("LineString") ? objectMapper.writeValueAsString((LineString) t.getValidGeometry())
                :t.getValidGeometry().getGeometryType().equals("Polygon") ? objectMapper.writeValueAsString((Polygon) t.getValidGeometry())
                : "null";
        
        //jg.writeObjectField("validGeometry", t.getValidGeometry());
        //System.out.println(geoJSON);
        jg.writeRaw(",\"validGeometry\":" + geoJSON);
        //
        jg.writeNumberField("warningStatus", t.getWarningStatus());
        jg.writeStringField("allValues", this.objectMapper.writeValueAsString(t.getAllValues()));
        jg.writeStringField("keys", this.objectMapper.writeValueAsString(t.getKeys()));
        jg.writeEndObject();
    }
    
    

}
