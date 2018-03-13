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

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.PrecisionModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.measure.Measure;
import javax.measure.quantity.Area;
import javax.measure.unit.SI;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;
import org.wololo.geojson.Feature;
import org.wololo.geojson.FeatureCollection;
import org.wololo.geojson.GeoJSONFactory;
import org.wololo.jts2geojson.GeoJSONReader;
import org.wololo.jts2geojson.GeoJSONWriter;

/**
 * Handy tools for GIS operations
 * @copyright 2017 <a href="http://www.nibio.no/">NIBIO</a>
 * @author Tor-Einar Skog <tor-einar.skog@nibio.no>
 */
public class GISUtil {
    
    public static Integer DEFAULT_SRID = 4326;
    
    /**
     * Calculates the coordinates of a nearby destination
     * @param origin WGS84 where to start from
     * @param distance Distance in km
     * @param initialBearing The initial bearing, given as degrees clockwise starting from North
     * @return 
     */
    public Coordinate getNearbyDestination(Coordinate origin, Double distance, Double initialBearing)
    {
        // Constant
        Double R = 6371.0; // The average radius of the earth in km
        // Convert to radians
        Double bearing = this.toRadians(initialBearing);
        Double longitude = this.toRadians(origin.x);
        Double latitude = this.toRadians(origin.y);
        
        // Latitude is seemingly easy
        Double angularDistance = distance/R;
        Double deltaLat = angularDistance * Math.cos(bearing);
        Double newLat = latitude + deltaLat;
        
        // check for some daft bugger going past the pole, normalize latitude if so
        if(Math.abs(newLat) > Math.PI/2)
        {
            newLat = newLat > 0 ? Math.PI - newLat : -Math.PI - newLat;
        }
        
        // Longitude
        Double projectedLatitudeDifference = Math.log(Math.tan(newLat/2 + Math.PI/4)/Math.tan(latitude/2 + Math.PI/4));
        Double q = Math.abs(projectedLatitudeDifference) > 10e-12 ? deltaLat/projectedLatitudeDifference : Math.cos(latitude); // E-W course becomes ill-conditioned with 0/0
        Double deltaLong = angularDistance * Math.sin(bearing)/q;
        Double newLong = longitude + deltaLong;
        
        return new Coordinate(this.toDegrees(newLong), this.toDegrees(newLat));
    }
    
    /**
     * 
     * @param degrees
     * @return radian value
     */
    public Double toRadians(Double degrees)
    {
        return degrees * Math.PI / 180;
    }
    
    /**
     * 
     * @param radians
     * @return degree value
     */
    public Double toDegrees(Double radians)
    {
        return radians * 180 / Math.PI;
    }
    
    /**
     * Parses all features from GeoJSON string, returns as JTS Geometry objects
     * @param json
     * @return 
     */
    public List<Geometry> getGeometriesFromGeoJSON(String json)
    {
        FeatureCollection featureCollection = (FeatureCollection) GeoJSONFactory.create(json);
        GeoJSONReader reader = new GeoJSONReader();
        List<Geometry> retVal = new ArrayList<>();
        for(Feature feature: featureCollection.getFeatures())
        {
            retVal.add(reader.read(feature.getGeometry()));
        }
        return retVal;
    }
    
    /**
     * Hand in your JTS Geometry objects, get GEOJSON back
     * @param geometries
     * @param properties these props will be attached to all features
     * @return 
     */
    public String getGeoJSONFromGeometries(List<Geometry> geometries, Map<String, Object> properties)
    {
        if(geometries == null || geometries.isEmpty())
        {
            return "";
        }
        List<Feature> features = new ArrayList<>();
        GeoJSONWriter writer = new GeoJSONWriter();
        geometries.stream().forEach((geometry)->features.add(new Feature(writer.write(geometry), properties)));
        FeatureCollection json = writer.write(features);
        return json.toString();
    }
    
    /**
     * Convert one JTS Geometry object into a GeoJSON feature
     * @param geometry
     * @param properties
     * @return 
     */
    public String getGeoJSONFromGeometry(Geometry geometry,Map<String, Object> properties)
    {
        if(geometry == null)
        {
            return "";
        }
        List<Feature> features = new ArrayList<>();
        GeoJSONWriter writer = new GeoJSONWriter();
       
        features.add(new Feature(writer.write(geometry), properties));
        
        FeatureCollection json = writer.write(features);
        return json.toString();
    }
    
    /**
     * If you have WGS84 coordinates, th
     * @param coordinate in WGS84 system
     * @return 
     */
    public Point createPointWGS84(Coordinate coordinate)
    {
        GeometryFactory gf = new GeometryFactory(new PrecisionModel(), 4326);
        return gf.createPoint(coordinate);
    }

    /**
     * Calculate distance between two points, courtesy of <a href="http://www.movable-type.co.uk/scripts/latlong.html">this page</a>. Explanation:
     * <pre>
     * This uses the "haversine" formula to calculate the great-circle distance between two points - 
     * that is, the shortest distance over the earth's surface, giving an 'as-the-crow-flies distance between the points (ignoring any hills, of course!).
        Haversine formula:
                a = sin²(?lat/2) + cos(lat1).cos(lat2).sin²(?long/2)
                c = 2.atan2(?a, ?(1?a))
                d = R.c
        where R is earth?s radius (mean radius = 6,371km);
        Note that angles need to be in radians to pass to trig functions!
     * </pre>
     * @param coord1
     * @param coord2
     * @return distance in kilometers
     */
    public Double getDistance(Coordinate coord1, Coordinate coord2)
    {
        Integer R = 6371; // km
        Double dLat = Math.toRadians(coord2.y - coord1.y);
        Double dLon = Math.toRadians(coord2.x - coord1.x);
        Double lat1 = Math.toRadians(coord1.y);
        Double lat2 = Math.toRadians(coord2.y);

        Double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
        Math.sin(dLon/2) * Math.sin(dLon/2) * Math.cos(lat1) * Math.cos(lat2); 
        Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a)); 
        Double d = R * c;
        
        return d;
    }
    
    /**
     * 
     * @param p
     * @return Area in square meters
     */
    public Measure<Double, Area> calcArea(Polygon p) {
        Point centroid = p.getCentroid();
        try {
          String code = "AUTO:42003," + centroid.getX() + "," + centroid.getY();
          CoordinateReferenceSystem auto = CRS.decode(code);

          MathTransform transform = CRS.findMathTransform(DefaultGeographicCRS.WGS84, auto);

          Polygon projed = (Polygon) JTS.transform(p, transform);
          return Measure.valueOf(projed.getArea(), SI.SQUARE_METRE);
        } catch (MismatchedDimensionException | TransformException | FactoryException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
        return Measure.valueOf(0.0, SI.SQUARE_METRE);
    }
    
    /**
     * Create a polygon with the given bounds
     * @param north
     * @param south
     * @param east
     * @param west
     * @return 
     */
    public Polygon createRectangleFromBounds(Double north, Double south, Double east, Double west)
    {
        GeometryFactory gf = new GeometryFactory(new PrecisionModel(), 4326);
        Coordinate[] coords = {
            new Coordinate(west, south),
                new Coordinate(west,north),
                new Coordinate(east,north),
                new Coordinate(east,south),
                new Coordinate(west,south)
        };
        return gf.createPolygon(coords);
    }
}
