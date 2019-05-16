/*
 * Copyright (c) 2017 NIBIO <http://www.nibio.no/>. 
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
package no.nibio.vips.gis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import junit.framework.TestCase;

/**
 *
 * @author treinar
 */
public class GISUtilTest extends TestCase {
    
    public GISUtilTest(String testName) {
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
     * Test of getNearbyDestination method, of class GISUtil.
     *
    public void testGetNearbyDestination() {
        System.out.println("getNearbyDestination");
        Coordinate origin = null;
        Double distance = null;
        Double initialBearing = null;
        GISUtil instance = new GISUtil();
        Coordinate expResult = null;
        Coordinate result = instance.getNearbyDestination(origin, distance, initialBearing);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of toRadians method, of class GISUtil.
     *
    public void testToRadians() {
        System.out.println("toRadians");
        Double degrees = null;
        GISUtil instance = new GISUtil();
        Double expResult = null;
        Double result = instance.toRadians(degrees);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of toDegrees method, of class GISUtil.
     *
    public void testToDegrees() {
        System.out.println("toDegrees");
        Double radians = null;
        GISUtil instance = new GISUtil();
        Double expResult = null;
        Double result = instance.toDegrees(radians);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getGeometriesFromGeoJSON method, of class GISUtil.
     *
    public void testGetGeometriesFromGeoJSON() {
        System.out.println("getGeometriesFromGeoJSON");
        String json = "";
        GISUtil instance = new GISUtil();
        List<Geometry> expResult = null;
        List<Geometry> result = instance.getGeometriesFromGeoJSON(json);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getGeoJSONFromGeometries method, of class GISUtil.
     *
    public void testGetGeoJSONFromGeometries() {
        System.out.println("getGeoJSONFromGeometries");
        List<Geometry> geometries = null;
        Map<String, Object> properties = null;
        GISUtil instance = new GISUtil();
        String expResult = "";
        String result = instance.getGeoJSONFromGeometries(geometries, properties);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getGeoJSONFromGeometry method, of class GISUtil.
     *
    public void testGetGeoJSONFromGeometry() {
        System.out.println("getGeoJSONFromGeometry");
        Geometry geometry = null;
        Map<String, Object> properties = null;
        GISUtil instance = new GISUtil();
        String expResult = "";
        String result = instance.getGeoJSONFromGeometry(geometry, properties);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of createPointWGS84 method, of class GISUtil.
     *
    public void testCreatePointWGS84() {
        System.out.println("createPointWGS84");
        Coordinate coordinate = null;
        GISUtil instance = new GISUtil();
        Point expResult = null;
        Point result = instance.createPointWGS84(coordinate);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    /**
     * Test of getGeometriesFromGeoJSON method, of class GISEntityUtil.
     */

    public void testRoundtrip() {
        System.out.println("testRoundtrip");
        String json = "{\"type\":\"FeatureCollection\",\"features\":[{\"type\":\"Feature\",\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[10.669097900390623,59.753628151319106],[10.612792968749998,59.70309199431276],[10.726776123046873,59.705863076677105],[10.669097900390623,59.753628151319106]]]},\"properties\":{\"observationId\":12}},{\"type\":\"Feature\",\"geometry\":{\"type\":\"Point\",\"coordinates\":[10.704803466796873,59.64831609639066]},\"properties\":{\"observationId\":12}},{\"type\":\"Feature\",\"geometry\":{\"type\":\"LineString\",\"coordinates\":[[10.649871826171871,59.67051458978321],[10.791320800781248,59.67328836837126]]},\"properties\":{\"observationId\":12}}]}";
        System.out.println("JSON=" + json);
        GISUtil instance = new GISUtil();
        //List<Geometry> expResult = null;
        List<org.locationtech.jts.geom.Geometry> geometries = instance.getGeometriesFromGeoJSON(json);
        Map<String, Object> properties = new HashMap<>();
        properties.put("observationId", 12);
        String result = instance.getGeoJSONFromGeometries(geometries, properties);
        //System.out.println(result);
        assertEquals(json, result);
        
        
    }
    
}
