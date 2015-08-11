/*
 * Copyright (c) 2015 NIBIO <http://www.nibio.no/>. 
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import junit.framework.TestCase;

/**
 *
 * @author treinar
 */
public class SolarRadiationUtilTest extends TestCase {
    
    public SolarRadiationUtilTest(String testName) {
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
     * Test of getSolarRadiationAtLatitude method, of class SolarRadiationUtil.
     */
    public void testGetSolarRadiationAtLatitude() {
        System.out.println("getSolarRadiationAtLatitude");
        Double latitude = 60.0;
        Date date;
        TimeZone timeZone = TimeZone.getTimeZone("Europe/Reykjavik");
        Calendar cal = Calendar.getInstance(timeZone);
        cal.set(2015, Calendar.AUGUST, 8, 0, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);
        double[] expected = {
            0.0,                        // :00
            0.0,                        // :01
            0.0,                        // :02
            0.0,                        // :03
            0.0,                        // :04
            0.1618004720531765,         // :05
            0.4904056679049868,         // :06
            0.694612671066168,          // :07
            0.8163567559989782,         // :08
            0.8905554259500866,         // :09
            0.9351015843025755,         // :10
            0.9590133834488044,         // :11
            0.966562397281901,          // :12
            0.9590133834488044,         // :13
            0.9351015843025755,         // :14
            0.8905554259500866,         // :15
            0.8163567559989782,         // :16
            0.694612671066168,          // :17
            0.4904056679049868,         // :18
            0.1618004720531765,         // :19
            0.0, // :20
            0.0, // :21
            0.0, // :22
            0.0  // :23
        };
        
        SolarRadiationUtil instance = new SolarRadiationUtil();
        
        /*
        for(Integer i=0;i<24;i++)
        {
            cal.set(Calendar.HOUR_OF_DAY, i);
            date = cal.getTime();
            Double result = instance.getSolarRadiationAtLatitude(latitude, cal.get(Calendar.DAY_OF_YEAR), i.doubleValue());
            System.out.println(date + ": " + result);
        }*/
        
        
        for(Integer i=0;i<24;i++)
        {
            cal.set(Calendar.HOUR_OF_DAY, i);
            date = cal.getTime();
            Double expResult = expected[i];
            Double result = instance.getSolarRadiationAtLatitude(latitude, cal.get(Calendar.DAY_OF_YEAR), i.doubleValue());
            assertEquals(expResult, result);
        }

    }

    /**
     * Test of getDeclination method, of class SolarRadiationUtil.
     */
    public void testGetDeclination() {
        System.out.println("getDeclination");
        Integer dayOfYear = 172;
        SolarRadiationUtil instance = new SolarRadiationUtil();
        Double expResult = 23.449782846813658;
        Double result = instance.getDeclination(dayOfYear);
        assertEquals(expResult, result);

        dayOfYear=81;
        expResult = 0.0;
        result = instance.getDeclination(dayOfYear);
        assertEquals(expResult, result);
    }
    
    public void testToRadians()
    {
        Double input = 90.0;
        Double expResult = Math.toRadians(input);
        Double result = input * Math.PI / 180;
        assertEquals(expResult,result);
    }

    /**
     * Test of getSunHoursFromMidday method, of class SolarRadiationUtil.
     */
    public void testGetSunHoursFromMidday() {
        System.out.println("getSunHoursFromMidday");
        Double latitude = 60.0;
        Integer dayOfYear = 220;
        SolarRadiationUtil instance = new SolarRadiationUtil();
        Double expResult = 7.9801135501740275;
        Double result = instance.getSunHoursFromMidday(latitude, dayOfYear);
        assertEquals(expResult, result);

    }

    /**
     * Test of getSolarRadiationAtLocationAndTime method, of class SolarRadiationUtil.
     */
    public void testGetSolarRadiationAtLocationAndTime() {
        System.out.println("getSolarRadiationAtLocationAndTime");
        Double latitude = 59.660468;
        Double longitude = 10.781989;
        TimeZone timeZone = TimeZone.getTimeZone("Europe/Oslo");
        Calendar cal = Calendar.getInstance(timeZone);
        cal.set(2015, Calendar.AUGUST, 8, 12, 0, 0);
        Date date = cal.getTime();
        SolarRadiationUtil instance = new SolarRadiationUtil();
        Double expResult = 0.9540516211907254;
        Double result = instance.getSolarRadiationAtLocationAndTime(latitude, longitude, date, timeZone);
        assertEquals(expResult, result);
        
        /*
        // Output to file
        cal.set(2015,Calendar.MARCH,1,0,0,0);
        date = cal.getTime();
        int lastDayOfYear = 304; // Oct 31
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH");
        format.setTimeZone(timeZone);
        while(cal.get(Calendar.DAY_OF_YEAR) <= lastDayOfYear)
        {
            String resultStr = String.valueOf(instance.getSolarRadiationAtLocationAndTime(latitude, longitude, date, timeZone) * 1000);
            System.out.println(format.format(date) + ";" + resultStr.replace(".", ","));
            cal.add(Calendar.HOUR_OF_DAY, 1);
            date = cal.getTime();
        }*/
        
    }

    /**
     * Test of getLSTM method, of class SolarRadiationUtil.
     */
    public void testGetLSTM() {
        System.out.println("getLSTM");
        // Norway
        TimeZone timeZone = TimeZone.getTimeZone("Europe/Oslo");
        SolarRadiationUtil instance = new SolarRadiationUtil();
        Double expResult = 15.0;
        Double result = instance.getLSTM(timeZone);
        //System.out.println("LSTM = " + result);
        assertEquals(expResult, result);
        //UTC
        timeZone = TimeZone.getTimeZone("UTC");
        expResult = 0.0;
        result = instance.getLSTM(timeZone);
        assertEquals(expResult, result);
        // USA time
        timeZone = TimeZone.getTimeZone("America/New_York");
        expResult = -75.0;
        result = instance.getLSTM(timeZone);
        assertEquals(expResult, result);
    }

    
    /**
     * Test of getEquationOfTime method, of class SolarRadiationUtil.
     */
    public void testGetEquationOfTime() {
        System.out.println("getEquationOfTime");
        int[] daysOfYear = {1,150,200,300,365};
        double[] EoTs = {
            -3.705178323396069,   // 1
            2.6372637316334417,    // 150
            -5.929529023641085,   // 200
            16.360503661915228,   // 300
            -3.2562349238053434    // 365
        };
        
        SolarRadiationUtil instance = new SolarRadiationUtil();
        
        Double result;
        
        // Debug printout
        /*for(int i=1;i< 365;i++)
        {
            result = instance.getEquationOfTime(i);
            System.out.println("doy=" + i + ", result= " + result);
        }*/
        
        
        for(int i=0; i< daysOfYear.length;i++)
        {
            result = instance.getEquationOfTime(daysOfYear[i]);
            //System.out.println("doy=" + daysOfYear[i] + ", result= " + result);
            assertEquals(EoTs[i], result);
        }
        
    }

    /**
     * Test of getSolarTimeAdjustment method, of class SolarRadiationUtil.
     */
    public void testGetSolarTimeAdjustment() {
        System.out.println("getSolarTimeAdjustment");
        Double longitude = 15.0;
        TimeZone timeZone = TimeZone.getTimeZone("Europe/Oslo");
        Calendar cal = Calendar.getInstance(timeZone);
        cal.set(2015, Calendar.AUGUST, 11, 12, 1, 1);
        Date date = cal.getTime();
        SolarRadiationUtil instance = new SolarRadiationUtil();
        Double expResult = -64.9068977318678;
        Double result = instance.getSolarTimeAdjustment(longitude, date, timeZone);
        assertEquals(expResult, result);
        
        // Testing outside DST
        
        cal.set(2015, Calendar.FEBRUARY, 1, 12, 1, 1);
        date = cal.getTime();
        expResult = -13.689348117395623;
        result = instance.getSolarTimeAdjustment(longitude, date, timeZone);
        assertEquals(expResult, result);
        
        // Testing in other time zone
        timeZone = TimeZone.getTimeZone("America/New_York");
        longitude = -75.0;
        result = instance.getSolarTimeAdjustment(longitude, date, timeZone);
        assertEquals(expResult, result);
        
        cal.set(2015, Calendar.AUGUST, 11, 12, 1, 1);
        date = cal.getTime();
        expResult = -64.9068977318678;
        result = instance.getSolarTimeAdjustment(longitude, date, timeZone);
        assertEquals(expResult, result);
        
        // Testing to find sunrise in Bergen August 11th 2015
        // According to http://www.timeanddate.com/worldclock/norway/bergen , sun should rise at 05:40
        timeZone = TimeZone.getTimeZone("Europe/Oslo");
        cal.set(2015, Calendar.AUGUST, 11, 5, 40, 0); 
        date = cal.getTime();
        longitude = 5.33333;
        Double latitude = 60.4;
        Double H = instance.getSunHoursFromMidday(latitude, cal.get(Calendar.DAY_OF_YEAR));
        expResult = 12 - H;
        Double solarTimeAdjustment = instance.getSolarTimeAdjustment(longitude, date, timeZone);
        Double unadjustedHour = cal.get(Calendar.HOUR_OF_DAY)
                + cal.get(Calendar.MINUTE) / 60.0
                + cal.get(Calendar.SECOND) / 3600.0;
        //System.out.println("UnadjustedHour=" + unadjustedHour);
        //System.out.println("solarTimeAdjustment=" + solarTimeAdjustment);
        result = unadjustedHour + solarTimeAdjustment / 60;
        assertEquals(expResult, result,0.2);
        
        // Testing to find sunrise in Stockholm August 11th 2015
        // According to http://www.timeanddate.com/worldclock/sweden/stockholm , sun should rise at 04:55
        timeZone = TimeZone.getTimeZone("Europe/Stockholm");
        cal.set(2015, Calendar.AUGUST, 11, 4, 55, 0); 
        date = cal.getTime();
        longitude = 18.024;
        latitude = 59.33333;
        H = instance.getSunHoursFromMidday(latitude, cal.get(Calendar.DAY_OF_YEAR));
        expResult = 12 - H;
        solarTimeAdjustment = instance.getSolarTimeAdjustment(longitude, date, timeZone);
        unadjustedHour = cal.get(Calendar.HOUR_OF_DAY)
                + cal.get(Calendar.MINUTE) / 60.0
                + cal.get(Calendar.SECOND) / 3600.0;
        //System.out.println("UnadjustedHour=" + unadjustedHour);
        //System.out.println("solarTimeAdjustment=" + solarTimeAdjustment);
        result = unadjustedHour + solarTimeAdjustment / 60;
        assertEquals(expResult, result,0.2);
    }

    /**
     * Test of getTimeCorrectionFactor method, of class SolarRadiationUtil.
     */
    public void testGetTimeCorrectionFactor() {
        System.out.println("getTimeCorrectionFactor");
        Double longitude = 15.0;
        Integer dayOfYear = 300;
        TimeZone timeZone = TimeZone.getTimeZone("Europe/Oslo");
        SolarRadiationUtil instance = new SolarRadiationUtil();
        Double expResult = 16.360503661915228;
        Double result = instance.getTimeCorrectionFactor(longitude, dayOfYear, timeZone);
        assertEquals(expResult, result);
        
        longitude = 14.0;
        expResult = 12.360503661915228;
        result = instance.getTimeCorrectionFactor(longitude, dayOfYear, timeZone);
        assertEquals(expResult, result);

    }
    
  
    
}
