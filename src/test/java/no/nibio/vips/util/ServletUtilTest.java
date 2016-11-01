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

import javax.servlet.http.HttpServletRequest;
import junit.framework.TestCase;

/**
 *
 * @author treinar
 */
public class ServletUtilTest extends TestCase {
    
    public ServletUtilTest(String testName) {
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
     * Test of getCleanedQueryString method, of class ServletUtil.
     */
    public void testGetCleanedQueryString() {
        System.out.println("getCleanedQueryString");
        String servletPathWithQueryString = "/blabla?removeParam=77&keepParam=66&keepAlso=uyt";
        String parameterToRemove = "removeParam";
        String expResult = "/blabla?keepParam=66&keepAlso=uyt";
        String result = ServletUtil.getCleanedQueryString(servletPathWithQueryString, parameterToRemove);
        System.out.println("result=" + result);
        assertEquals(expResult, result);
        servletPathWithQueryString = "/blabla?keepParam=66&removeParam=77&keepAlso=uyt";
        result = ServletUtil.getCleanedQueryString(servletPathWithQueryString, parameterToRemove);
        assertEquals(expResult, result);
        servletPathWithQueryString = "/blabla?keepParam=66&keepAlso=uyt&removeParam=77";
        result = ServletUtil.getCleanedQueryString(servletPathWithQueryString, parameterToRemove);
        assertEquals(expResult, result);
        servletPathWithQueryString = "/blabla?removeParam=77";
        expResult = "/blabla";
        result = ServletUtil.getCleanedQueryString(servletPathWithQueryString, parameterToRemove);
        assertEquals(expResult, result);
    }
    
}
