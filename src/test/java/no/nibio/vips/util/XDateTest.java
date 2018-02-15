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
package no.nibio.vips.util;

import java.util.Calendar;
import junit.framework.TestCase;

/**
 *
 * @author treinar
 */
public class XDateTest extends TestCase {
    
    public XDateTest(String testName) {
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
     * Test of add1Day method, of class XDate.
     */
    public void testAdd1Day() {
        System.out.println("add1Day");
        XDate instance = new XDate();
        Calendar cal = Calendar.getInstance();
        cal.setTime(instance);
        cal.add(Calendar.DATE, 1);
        instance.add1Day();
        assertEquals(cal.getTime(), instance);
        
    }
    
}
