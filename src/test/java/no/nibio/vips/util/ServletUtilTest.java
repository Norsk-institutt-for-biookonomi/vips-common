/*
 * Copyright (c) 2016 NIBIO <http://www.nibio.no/>. 
 * 
 * This file is part of VIPSCommon.
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 * 
 */
package no.nibio.vips.util;

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
