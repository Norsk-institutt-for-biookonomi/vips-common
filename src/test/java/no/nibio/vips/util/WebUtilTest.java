/*
 * Copyright (c) 2016 NIBIO <http://www.nibio.no/>. 
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

import java.util.HashMap;
import java.util.Map;
import junit.framework.TestCase;

/**
 *
 * @author treinar
 */
public class WebUtilTest extends TestCase {
    
    public WebUtilTest(String testName) {
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
     * Test of isValidEmailAddress method, of class WebUtil.
     */
    public void IsValidEmailAddress() {
        System.out.println("isValidEmailAddress");
        String aEmailAddress = "";
        boolean expResult = false;
        boolean result = WebUtil.isValidEmailAddress(aEmailAddress);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isValidWebAddress method, of class WebUtil.
     */
    public void IsValidWebAddress() {
        System.out.println("isValidWebAddress");
        String aWebAddress = "";
        boolean expResult = false;
        boolean result = WebUtil.isValidWebAddress(aWebAddress);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of replaceTextURLWithLink method, of class WebUtil.
     */
    public void ReplaceTextURLWithLink() {
        System.out.println("replaceTextURLWithLink");
        String text = "";
        String target = "";
        String expResult = "";
        String result = WebUtil.replaceTextURLWithLink(text, target);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getOutputFromPostRequest method, of class WebUtil.
     */
    public void testGetOutputFromPostRequest() throws Exception {
        System.out.println("getOutputFromPostRequest");
        String URL = "http://uspest.org/risk/models";
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("station", "FNWO3");
            queryParams.put("stm","10"); // Start month
            queryParams.put("std","1"); // Start dayOfMonth
            queryParams.put("sty","2016"); // Start year
            queryParams.put("span","60"); // Number of days from start
            queryParams.put("fsm","11"); // Forecast start month
            queryParams.put("fsd","1"); // Forecast start dayOfMonth
            // fcast, Type of forecast
            // 1 = after 7day use 10 year averages (default)
            // 2 = after 7day use 30 year averages
            // 3 = after 7day use data from 1 year ago (if available)
            // 4  = after 7day use data from 2 years ago (if available)
            // 5 = after 7day use NMME extended seasonal forecast
            // 6 = after 7day use the CFSv2 extended seasonal forecast
            queryParams.put("fcast","1");
            queryParams.put("en_v2_tdp_missing", "on"); // Replace missing Temperature and Dew Point
            queryParams.put("en_v2_tdp_flatline", "on"); // Replace flat line Temperature and Dew Point
            queryParams.put("en_v2_tdp_range", "on"); // Replace range exceed Temperature and Dew Point
            queryParams.put("en_v2_tdp_extrap", "on"); // Weather data fallback config ASK LEN
            queryParams.put("en_v2_tdp_interp", "on"); // Weather data fallback config ASK LEN
            queryParams.put("en_v2_tdp_elevreg", "on"); // Weather data fallback config ASK LEN
            queryParams.put("en_v2_windspeed_missing", "on"); // Replace missing wind speed
            queryParams.put("en_v2_rain_missing", "on"); // Replance missing precipitation
            queryParams.put("download_data","Download Data");
            queryParams.put("last_date_of_refresh","20161101"); // ??
            queryParams.put("stdt","20161001"); // Probably start date, but why the double info (see stm/std)
            queryParams.put("date","20161101"); // Should be "today"
            queryParams.put("tbl","0"); // Does this mean don't display table, ie irrelevant? ASK LEN
            queryParams.put("Y", "464"); // ASK LEN
        
        String expResult = "";
        String[] weatherParams = {"temp", "dewpt","rel_hum","windspeed","rain","leafwet"};
        Map<String, String[]> arrP = new HashMap<>();
        arrP.put("wp", weatherParams);
        String result = WebUtil.getOutputFromPostRequest(URL, queryParams, arrP);
        //System.out.println(result);
        assertNotNull(expResult, result);
        
    }
    
}
