/*
 * Copyright (c) 2014 Bioforsk <http://www.bioforsk.no/>. 
 * 
 * This file is part of VIPSCommon.
 * VIPSCommon is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * VIPSCommon is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with VIPSCommon.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package no.bioforsk.vips.util;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;
import junit.framework.TestCase;
import no.bioforsk.vips.entity.WeatherObservation;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.MappingJsonFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

/**
 *
 * @author treinar
 */
public class WeatherUtilTest extends TestCase {
    
    public WeatherUtilTest(String testName) {
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
     * Test of getWVD method, of class WeatherUtil.
     */
    /*
    public void testGetWVD() {
        System.out.println("getWVD");
        double temperature = 0.0;
        double relativeHumidity = 0.0;
        WeatherUtil instance = new WeatherUtil();
        double expResult = 0.0;
        double result = instance.getWVD(temperature, relativeHumidity);
        assertEquals(expResult, result, 0.0);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }*/

    /**
     * Test of getPartialPressure method, of class WeatherUtil.
     */
    /*
    public void testGetPartialPressure() {
        System.out.println("getPartialPressure");
        double saturationPressure = 0.0;
        double relativeHumidity = 0.0;
        WeatherUtil instance = new WeatherUtil();
        double expResult = 0.0;
        double result = instance.getPartialPressure(saturationPressure, relativeHumidity);
        assertEquals(expResult, result, 0.0);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }*/

    /**
     * Test of getSaturationPressure method, of class WeatherUtil.
     */
    /*
    public void testGetSaturationPressure() {
        System.out.println("getSaturationPressure");
        double temperature = 0.0;
        WeatherUtil instance = new WeatherUtil();
        double expResult = 0.0;
        double result = instance.getSaturationPressure(temperature);
        assertEquals(expResult, result, 0.0);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }*/

    /**
     * Test of getAggregatedDailyValues method, of class WeatherUtil.
     */
    public void testGetAggregatedDailyValues() {
        System.out.println("getAggregatedDailyValues");
        List<WeatherObservation> allObservations = this.getObservations();
        List<WeatherObservation> TM = new ArrayList<>();
        for(WeatherObservation obs:allObservations)
        {
            if(obs.getElementMeasurementTypeId().equals("TM"))
                TM.add(obs);
        }
        WeatherUtil instance = new WeatherUtil();
        List expResult = null;
        Double expSum = 143.091;
        Double expAvg = 5.962125;
        Double expMin = -0.191;
        Double expMax = 12.71;
        TimeZone timeZone = TimeZone.getDefault();
        //System.out.println("TimeZone=" + timeZone.getDisplayName());
        List<WeatherObservation> avgResults = null;
        List<WeatherObservation> sumResults = null;
        List<WeatherObservation> minResults = null;
        List<WeatherObservation> maxResults = null;
        try {
            avgResults = instance.getAggregatedDailyValues(TM, timeZone,23,WeatherUtil.AGGREGATION_TYPE_AVERAGE);
            Collections.sort(avgResults);
            sumResults = instance.getAggregatedDailyValues(TM, timeZone,23,WeatherUtil.AGGREGATION_TYPE_SUM);
            Collections.sort(sumResults);
            minResults = instance.getAggregatedDailyValues(TM, timeZone,23,WeatherUtil.AGGREGATION_TYPE_MINIMUM);
            Collections.sort(minResults);
            maxResults = instance.getAggregatedDailyValues(TM, timeZone,23,WeatherUtil.AGGREGATION_TYPE_MAXIMUM);
            Collections.sort(maxResults);
            
        } catch (WeatherObservationListException | InvalidAggregationTypeException ex) {
            fail(ex.getMessage());
        }
        assertEquals(expAvg, avgResults.get(0).getValue());
        assertEquals(expSum, sumResults.get(0).getValue());
        assertEquals(expMin, minResults.get(0).getValue());
        assertEquals(expMax, maxResults.get(0).getValue());
    }

    /**
     * Test of normalizeToExactDate method, of class WeatherUtil.
     *
    public void testNormalizeToExactDate() {
        System.out.println("normalizeToExactDate");
        Date timeStamp = null;
        TimeZone timeZone = null;
        WeatherUtil instance = new WeatherUtil();
        Date expResult = null;
        Date result = instance.normalizeToExactDate(timeStamp, timeZone);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }*/
    
    private List<WeatherObservation> getObservations()
    {
        try
        {
        BufferedInputStream inputStream = new BufferedInputStream(this.getClass().getResourceAsStream("/weatherData.json"));
            JsonFactory f = new MappingJsonFactory();
            JsonParser jp = f.createJsonParser(inputStream);
            JsonNode all = jp.readValueAsTree();
            List<WeatherObservation> observations = new ArrayList<>();
            ObjectMapper mapper = new ObjectMapper();

            Date firstDate = null;
            Date lastDate = null;
            if(all.isArray())
            {
                for(JsonNode node : all){
                    Date timeMeasured = (Date)mapper.convertValue(node.get("timeMeasured").getTextValue(), new TypeReference<Date>(){});
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
                    observation.setLogIntervalId(node.get("logIntervalId").getIntValue());
                    observation.setElementMeasurementTypeId(node.get("elementMeasurementTypeId").getTextValue());
                    observation.setValue(node.get("value").getDoubleValue());
                    observations.add(observation);
                }

            }
            else
            {
                fail("Data input from file is not a JSON array");
            }
            return observations;
        }
        catch(IOException ex)
        {
            return null;
        }
    }
}
