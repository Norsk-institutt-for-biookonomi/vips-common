/*
 * Copyright (c) 2017 NIBIO <http://www.nibio.no/>. 
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

import java.text.SimpleDateFormat;
import java.util.Date;
import junit.framework.TestCase;

/**
 *
 * @author treinar
 */
public class DateUtilTest extends TestCase {
    
    public DateUtilTest(String testName) {
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
     * Test of getDaysBetween method, of class DateUtil.
     */
    public void testGetDaysBetween() throws Exception {
        System.out.println("getDaysBetween");
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        
        // Start and end year is not a leap year
        Date from = f.parse("2015-12-31");
        Date to = f.parse("2017-12-22");
        DateUtil instance = new DateUtil();
        int expResult = 722;
        int result = instance.getDaysBetween(from, to);
        assertEquals(expResult, result);
        
        // Start is leap year, end is not, start date is after leap date
        from = f.parse("2016-12-31");
        to = f.parse("2017-01-01");
        expResult = 1;
        result = instance.getDaysBetween(from, to);
        assertEquals(expResult, result);
        
        // Start is leap year, end is not, start date is before leap date
        from = f.parse("2016-02-25");
        expResult = 311;
        result = instance.getDaysBetween(from, to);
        assertEquals(expResult, result);
        
        // Start is not leap year, end is, end is after leap date
        from = f.parse("2015-04-01");
        to = f.parse("2016-06-01");
        expResult = 427;
        result = instance.getDaysBetween(from, to);
        assertEquals(expResult, result);
        
    }
    
}
