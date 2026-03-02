/*
 * Copyright (c) 2018 NIBIO <http://www.nibio.no/>. 
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
