/*
 * Copyright (c) 2014 NIBIO <http://www.nibio.no/>. 
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

package no.nibio.vips.util;

import no.nibio.vips.util.WeatherElements;
import no.nibio.vips.util.WeatherObservationListException;
import no.nibio.vips.util.InvalidAggregationTypeException;
import no.nibio.vips.util.WeatherUtil;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MappingJsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;
import junit.framework.TestCase;
import no.nibio.vips.entity.WeatherObservation;
import no.nibio.vips.model.ConfigValidationException;

/**
 *
 * @author Tor-Einar Skog <tor-einar.skog@nibio.no>
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
    
    public void testCutTrailingHourlyValues()
    {
        System.out.println("testCutTrailingHourlyValues");
        WeatherUtil instance = new WeatherUtil();
        List<WeatherObservation> TM = this.getObservations("/weatherDataTestCutTrailing.json");
        List<WeatherObservation> result = instance.cutTrailingHourlyValues(TM, TimeZone.getTimeZone("Europe/Oslo"));
        assertEquals(216, result.size());
    }
    
    public void testCheckForAndFixHourlyTimeSeriesHoles()
    {
        try {
            System.out.println("testCheckForAndFixHourlyTimeSeriesHoles");
            // Testing with TM values firrst
            List<WeatherObservation> seriesWithHoles = this.getObservations("/weatherDataWithHoles.json");
            WeatherUtil wUtil = new WeatherUtil();
            List<WeatherObservation> result = wUtil.checkForAndFixHourlyTimeSeriesHoles(seriesWithHoles);
            /*for(WeatherObservation o:result)
            {
                System.out.println(o.getTimeMeasured() + ": " + o.getValue());
            }*/
            assertEquals(48,result.size());
            // Testing with RR values - should be calculated to 0
            seriesWithHoles = this.getObservations("/weatherDataWithHolesRR.json");
            result = wUtil.checkForAndFixHourlyTimeSeriesHoles(seriesWithHoles);
            /*for(WeatherObservation o:result)
            {
                System.out.println(o.getTimeMeasured() + ": " + o.getValue());
            }*/
            assertEquals(48,result.size());
        } catch (WeatherObservationListException ex) {
            fail(ex.getMessage());
        }
    }
    
    public void testBosnianData()
    {
        try {
            System.out.println("testBosnianData");
            // Testing with TM values first
            List<WeatherObservation> allData = this.getObservations("/bosniandata.json");
            List<WeatherObservation> TM = new ArrayList<WeatherObservation>();
            for(WeatherObservation obs:allData)
            {
                if(obs.getElementMeasurementTypeId().equals("TM"))
                    TM.add(obs);
            }
            WeatherUtil wUtil = new WeatherUtil();
            List<WeatherObservation> result = wUtil.checkForAndFixHourlyTimeSeriesHoles(TM);
            /*for(WeatherObservation o:result)
            {
                System.out.println(o.getTimeMeasured() + ": " + o.getValue());
            }*/
            assertEquals(1128,result.size());
            
        } catch (WeatherObservationListException ex) {
            fail(ex.getMessage());
        }
    }
    
    public void testSwedishData()
    {
        try {
            System.out.println("testSwedishData");
            // Testing with TM values first
            List<WeatherObservation> allData = this.getObservations("/swedishData.json");
            List<WeatherObservation> TM = new ArrayList<WeatherObservation>();
            for(WeatherObservation obs:allData)
            {
                if(obs.getElementMeasurementTypeId().equals("TM"))
                    TM.add(obs);
            }
            WeatherUtil wUtil = new WeatherUtil();
            List<WeatherObservation> result = wUtil.checkForAndFixHourlyTimeSeriesHoles(TM);
            /*for(WeatherObservation o:result)
            {
                System.out.println(o.getTimeMeasured() + ": " + o.getValue());
            }*/
            assertEquals(271,result.size());
            
        } catch (WeatherObservationListException ex) {
            fail(ex.getMessage());
        }
    }
    
    public void testFixHourlyValuesForParameters()
    {
        try {
            System.out.println("testFixHourlyValuesForParameters");
            // Testing first with data with RR present in both ends
            List<WeatherObservation> allData = this.getObservations("/swedishData.json");
            WeatherUtil wUtil = new WeatherUtil();
            Set<String> paramNames = new HashSet(Arrays.asList("TM", "UM","RR"));
            Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/Stockholm"));
            cal.set(2015, Calendar.MARCH, 15, 0, 0, 0);
            cal.set(Calendar.MILLISECOND, 0);
            List<WeatherObservation> result = wUtil.fixHourlyValuesForParameters(allData, paramNames, cal.getTime(),null);
            /*for(WeatherObservation o:result)
            {
                System.out.println(o.getTimeMeasured() + ": " + o.getValue());
            }*/
            assertEquals(271*3,result.size());
            
            // Testing with data where RR is missing at the end
            allData = this.getObservations("/swedishData_1.json");
            result = wUtil.fixHourlyValuesForParameters(allData, paramNames, cal.getTime(),null);
            assertEquals(270*3,result.size());
            
            // Testing with data where RR is "accidentally" added at the end
            allData = this.getObservations("/swedishData_2.json");
            result = wUtil.fixHourlyValuesForParameters(allData, paramNames, cal.getTime(),null);
            assertEquals(296*3,result.size());
            
            // Testing sending only one parameter in
            result = wUtil.fixHourlyValuesForParameters(allData, new HashSet(Arrays.asList("TM")), cal.getTime(),null);
            assertEquals(295,result.size());
            
        } catch (WeatherObservationListException ex) {
            fail(ex.getMessage());
        }
    }
    
    

    /**
     * Test of getAggregatedDailyValues method, of class WeatherUtil.
     */
    public void testGetAggregatedDailyValues() {
        System.out.println("getAggregatedDailyValues");
        List<WeatherObservation> allObservations = this.getObservations("/weatherData.json");
        List<WeatherObservation> TM = new ArrayList<WeatherObservation>();
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
        TimeZone timeZone = TimeZone.getTimeZone("Europe/Oslo");
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
            
        } catch (WeatherObservationListException ex)
        {fail(ex.getMessage());
        }
        catch(InvalidAggregationTypeException ex) 
        {
            fail(ex.getMessage());
        }
        assertEquals(expAvg, avgResults.get(0).getValue());
        assertEquals(expSum, sumResults.get(0).getValue());
        assertEquals(expMin, minResults.get(0).getValue());
        assertEquals(expMax, maxResults.get(0).getValue());
        
        // Testing with weather data that is in DST transition (from winter to summer time)
        allObservations = this.getObservations("/alnarp_dst_transition.json");
        TM = new ArrayList<WeatherObservation>();
        for(WeatherObservation obs:allObservations)
        {
            if(obs.getElementMeasurementTypeId().equals("TM"))
                TM.add(obs);
        }
        timeZone = TimeZone.getTimeZone("Europe/Stockholm");
        try {
            avgResults = instance.getAggregatedDailyValues(TM, timeZone,23,WeatherUtil.AGGREGATION_TYPE_AVERAGE);
            Collections.sort(avgResults);
            for(WeatherObservation obs:avgResults)
            {
                System.out.println(obs.getTimeMeasured()+":" + obs.getValue());
            }
        } catch (WeatherObservationListException ex) {
            fail(ex.getMessage());
        } catch (InvalidAggregationTypeException ex) {
            fail(ex.getMessage());
        }
    }
    
    public void testCalculateLeafWetnessHourSeriesBestEffort()
    {
        System.out.println("testCalculateLeafWetnessHourSeriesBestEffort");
        List<WeatherObservation> allObservations = this.getObservations("/weatherDataLeafWetnessCalc.json");
        List<WeatherObservation> TM = new ArrayList<WeatherObservation>();
        List<WeatherObservation> UM = new ArrayList<WeatherObservation>();
        List<WeatherObservation> FM2 = new ArrayList<WeatherObservation>();
        List<WeatherObservation> Q0 = new ArrayList<WeatherObservation>();
        List<WeatherObservation> RR = new ArrayList<WeatherObservation>();
        List<WeatherObservation> BT = new ArrayList<WeatherObservation>();
        // Add collections here
        for(WeatherObservation obs:allObservations)
        {
            if(obs.getElementMeasurementTypeId().equals(WeatherElements.TEMPERATURE_MEAN))
                TM.add(obs);
            else if(obs.getElementMeasurementTypeId().equals(WeatherElements.RELATIVE_HUMIDITY_MEAN))
                UM.add(obs);
            else if(obs.getElementMeasurementTypeId().equals(WeatherElements.WIND_SPEED_2M))
                FM2.add(obs);
            else if(obs.getElementMeasurementTypeId().equals(WeatherElements.GLOBAL_RADIATION))
                Q0.add(obs);
            else if(obs.getElementMeasurementTypeId().equals(WeatherElements.PRECIPITATION))
                RR.add(obs);
            else if(obs.getElementMeasurementTypeId().equals(WeatherElements.LEAF_WETNESS))
                BT.add(obs);
        }
        
        // Trying "Best effort"
        List<WeatherObservation> BTtest = BT.size() > 1 ? new ArrayList(BT.subList(0, BT.size()/2)) : new ArrayList(BT);
        System.out.println("BTtest.size=" + BTtest.size());
        System.out.println("BT.size=" + BT.size());
        
        WeatherUtil instance = new WeatherUtil();
        try {
            // Naerstad should be called now
            List<WeatherObservation> result = instance.calculateLeafWetnessHourSeriesBestEffort(BTtest, TM, RR, Q0, FM2, UM);
            System.out.println("result.size=" + result.size());
            // Test 1 // Result have same length as input data
            assertEquals(TM.size(), result.size());
            // Starts with same timestamp
            assertEquals(TM.get(0).getTimeMeasured(), result.get(0).getTimeMeasured());
            // Ends with same timestamp
            assertEquals(TM.get(TM.size()-1).getTimeMeasured(), result.get(TM.size()-1).getTimeMeasured());
            /*
            for(int i=0; i< result.size();i++)
            {
                WeatherObservation calc = result.get(i);
                WeatherObservation orig = i < BT.size() ? BT.get(i) : null;
                System.out.println(calc.getElementMeasurementTypeId() + " [" +calc.getTimeMeasured() + "]: " + calc.getValue() + " | " + (orig != null ? orig.getValue():"NULL"));
            }*/
            
            // Testing Nærstad with completely empty BT as input
            BTtest = new ArrayList<WeatherObservation>();
            result = instance.calculateLeafWetnessHourSeriesBestEffort(BTtest, TM, RR, Q0, FM2, UM);
            System.out.println("result.size=" + result.size());
            // Test 1 // Result have same length as input data
            assertEquals(TM.size(), result.size());
            // Starts with same timestamp
            assertEquals(TM.get(0).getTimeMeasured(), result.get(0).getTimeMeasured());
            // Ends with same timestamp
            assertEquals(TM.get(TM.size()-1).getTimeMeasured(), result.get(TM.size()-1).getTimeMeasured());
            /*
            for(int i=0; i< result.size();i++)
            {
                WeatherObservation calc = result.get(i);
                WeatherObservation orig = i < BT.size() ? BT.get(i) : null;
                System.out.println(calc.getElementMeasurementTypeId() + " [" +calc.getTimeMeasured() + "]: " + calc.getValue() + " | " + (orig != null ? orig.getValue():"NULL"));
            }*/
            
            
            // Trapman should be called now
            Q0 = new ArrayList<WeatherObservation>();
            BTtest = BT.size() > 1 ? new ArrayList(BT.subList(0, BT.size()/2)) : new ArrayList(BT);
            result = instance.calculateLeafWetnessHourSeriesBestEffort(BTtest, TM, RR, Q0, FM2, UM);
            System.out.println("result.size=" + result.size());
            // Test 1 // Result have same length as input data
            assertEquals(TM.size(), result.size());
            // Starts with same timestamp
            assertEquals(TM.get(0).getTimeMeasured(), result.get(0).getTimeMeasured());
            // Ends with same timestamp
            assertEquals(TM.get(TM.size()-1).getTimeMeasured(), result.get(TM.size()-1).getTimeMeasured());
            /*
            for(int i=0; i< result.size();i++)
            {
                WeatherObservation calc = result.get(i);
                WeatherObservation orig = i < BT.size() ? BT.get(i) : null;
                System.out.println(calc.getElementMeasurementTypeId() + " [" +calc.getTimeMeasured() + "]: " + calc.getValue() + " | " + (orig != null ? orig.getValue():"NULL"));
            }*/
        } catch (ConfigValidationException ex) {
            Logger.getLogger(WeatherUtilTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void testFindFirstHoleInObservationSeries()
    {
        TimeZone tz = TimeZone.getTimeZone("Europe/Oslo");
        System.out.println("testFindFirstHoleInObservationSeries");
        List<WeatherObservation> TM = this.getObservations("/JSONWeatherDataTMDailyComplete.json");
        WeatherUtil instance = new WeatherUtil();
        assertNull(instance.findFirstHoleInObservationSeries(TM, WeatherObservation.LOG_INTERVAL_ID_1D,tz));
        
        TM = this.getObservations("/JSONWeatherDataTMDailyFaulty.json");
        Date result = instance.findFirstHoleInObservationSeries(TM, WeatherObservation.LOG_INTERVAL_ID_1D,tz);
        System.out.println("Found hole here: " + result);
        assertNotNull(result);
        
        // TEST hourly data
        TM = this.getObservations("/weatherDataWithoutHolesRR.json");
        result = instance.findFirstHoleInObservationSeries(TM, WeatherObservation.LOG_INTERVAL_ID_1H,tz);
        if(result != null)
            System.out.println("Found hole here: " + result);
        assertNull(result);
        
        TM = this.getObservations("/weatherDataWithHolesRR.json");
        result = instance.findFirstHoleInObservationSeries(TM, WeatherObservation.LOG_INTERVAL_ID_1H,tz);
        System.out.println("Found hole here: " + result);
        assertNotNull(result);
    }
    
    public void testIsHomogenousTimeSeries()
    {
        System.out.println("testIsHomogenousTimeSeries");
        
        WeatherUtil instance = new WeatherUtil();
        
        List<WeatherObservation> TM = new ArrayList<WeatherObservation>();
        List<WeatherObservation> RR = new ArrayList<WeatherObservation>();
        List<WeatherObservation> UM = new ArrayList<WeatherObservation>();


        List<WeatherObservation> faultySeries = this.getObservations("/weatherDataFaultyUnequalSize.json");
        
        for(WeatherObservation o:faultySeries)
        {
            if(o.getElementMeasurementTypeId().equals(WeatherElements.TEMPERATURE_MEAN))
            {
                TM.add(o);
            }
            else if(o.getElementMeasurementTypeId().equals(WeatherElements.PRECIPITATION))
            {
                RR.add(o);
            }
            else if(o.getElementMeasurementTypeId().equals(WeatherElements.RELATIVE_HUMIDITY_MEAN))
            {
                UM.add(o);
            }
        }
        
        try
        {
            // Should throw exception if at least one list is empty/NULL
            instance.isHomogenousTimeSeries(TM, new ArrayList<WeatherObservation>());
            fail("Method should have thrown exception");
        }
         catch(WeatherObservationListException ex) {
            // This means that the method works as expected
        }
        
        try
        {
            // Should throw exception with faulty data
            instance.isHomogenousTimeSeries(TM,RR,UM);
            fail("Method should have thrown exception");
        }
        catch(WeatherObservationListException ex) {
            // This means that the method works as expected
        }
        
        // Test for data where at least one series starts at a different time
        
        TM.removeAll(TM);
        UM.removeAll(UM);
        RR.removeAll(RR);
        
        faultySeries = this.getObservations("/weatherDataFaultyUnequalStart.json");
        
        for(WeatherObservation o:faultySeries)
        {
            if(o.getElementMeasurementTypeId().equals(WeatherElements.TEMPERATURE_MEAN))
            {
                TM.add(o);
            }
            else if(o.getElementMeasurementTypeId().equals(WeatherElements.PRECIPITATION))
            {
                RR.add(o);
            }
            else if(o.getElementMeasurementTypeId().equals(WeatherElements.RELATIVE_HUMIDITY_MEAN))
            {
                UM.add(o);
            }
        }
        
        try
        {
            // Should throw exception with faulty data
            instance.isHomogenousTimeSeries(TM,RR,UM);
            fail("Method should have thrown exception");
        }
        catch(WeatherObservationListException ex) {
            System.out.println(ex.getMessage());
            // This means that the method works as expected
        }
        
        // Test for data where at least one series ends at a different time
        
        TM.removeAll(TM);
        UM.removeAll(UM);
        RR.removeAll(RR);
        
        faultySeries = this.getObservations("/weatherDataFaultyUnequalEnd.json");
        
        for(WeatherObservation o:faultySeries)
        {
            if(o.getElementMeasurementTypeId().equals(WeatherElements.TEMPERATURE_MEAN))
            {
                TM.add(o);
            }
            else if(o.getElementMeasurementTypeId().equals(WeatherElements.PRECIPITATION))
            {
                RR.add(o);
            }
            else if(o.getElementMeasurementTypeId().equals(WeatherElements.RELATIVE_HUMIDITY_MEAN))
            {
                UM.add(o);
            }
        }
        
        try
        {
            // Should throw exception with faulty data
            instance.isHomogenousTimeSeries(TM,RR,UM);
            fail("Method should have thrown exception");
        }
        catch(WeatherObservationListException ex) {
            System.out.println(ex.getMessage());
            // This means that the method works as expected
        }
        
        // Finally, should let valid series pass
        
        TM.removeAll(TM);
        UM.removeAll(UM);
        RR.removeAll(RR);
        
        faultySeries = this.getObservations("/weatherDataNotFaulty.json");
        
        for(WeatherObservation o:faultySeries)
        {
            if(o.getElementMeasurementTypeId().equals(WeatherElements.TEMPERATURE_MEAN))
            {
                TM.add(o);
            }
            else if(o.getElementMeasurementTypeId().equals(WeatherElements.PRECIPITATION))
            {
                RR.add(o);
            }
            else if(o.getElementMeasurementTypeId().equals(WeatherElements.RELATIVE_HUMIDITY_MEAN))
            {
                UM.add(o);
            }
        }
        
        try
        {
            // Should not throw exception
            instance.isHomogenousTimeSeries(TM,RR,UM);
        }
        catch(WeatherObservationListException ex) {
            fail(ex.getMessage());
        }
        
    }
    
    public void testRemoveDuplicateWeatherObservations()
    {
        System.out.println("testRemoveDuplicateWeatherObservations()");
        List<WeatherObservation> faultySeries = this.getObservations("/weatherDataWithDuplicates.json");
        
        WeatherUtil instance = new WeatherUtil();
        
        try
        {
            List<WeatherObservation> result = instance.removeDuplicateWeatherObservations(faultySeries, null);
            assertNotSame(faultySeries.size(), result.size());
            // Checking tolerance
            boolean errorThrown = false;
            try
            {
                result = instance.removeDuplicateWeatherObservations(faultySeries, 0.02);
            }
            catch(WeatherObservationListException ex)
            {
                errorThrown = true;
            }
            if(!errorThrown)
            {
                fail("Should have detected too many duplicates, but didn't.");
            }
            
            
        }
        catch(WeatherObservationListException ex)
        {
            fail(ex.getMessage());
        }
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
    
    private List<WeatherObservation> getObservations(String fileName)
    {
        try
        {
        BufferedInputStream inputStream = new BufferedInputStream(this.getClass().getResourceAsStream(fileName));
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
