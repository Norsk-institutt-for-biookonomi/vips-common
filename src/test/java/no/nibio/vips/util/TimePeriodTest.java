/*
 * Copyright (c) 2015 NIBIO <http://www.nibio.no/>. 
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
import java.util.Date;
import junit.framework.TestCase;

/**
 *
 * @author treinar
 */
public class TimePeriodTest extends TestCase {
    
    public TimePeriodTest(String testName) {
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
     * Test of isDateInTimePeriod method, of class TimePeriod.
     */
    public void testIsDateInTimePeriod() {
        System.out.println("isDateInTimePeriod");
        Calendar cal = Calendar.getInstance();
        cal.set(2015,Calendar.AUGUST,5,12,0);
        cal.set(Calendar.MILLISECOND, 0);
        Date date = cal.getTime();
        TimePeriod instance = new TimePeriod();
        instance.setStart(cal.getTime());
        cal.set(2015, Calendar.AUGUST,10,11,0);
        instance.setEnd(cal.getTime());
        
        // Testing edge cases INSIDE
        boolean expResult = true;
        boolean result = instance.isDateInTimePeriod(date);
        assertEquals(expResult, result);
        
        cal.set(2015, Calendar.AUGUST,10,11,0);
        date = cal.getTime();
        result = instance.isDateInTimePeriod(date);
        assertEquals(expResult, result);
        
        // Testing edge cases OUTSIDE
        expResult = false;
        cal.set(2015,Calendar.AUGUST,5,11,59);
        date = cal.getTime();
        result = instance.isDateInTimePeriod(date);
        assertEquals(expResult, result);
        
        cal.set(2015, Calendar.AUGUST,10,11,1);
        date = cal.getTime();
        result = instance.isDateInTimePeriod(date);
        assertEquals(expResult, result);
        
        // Testing in the middle
        cal.set(2015,Calendar.AUGUST,7,12,0);
        date = cal.getTime();
        expResult = true;
        result = instance.isDateInTimePeriod(date);
        assertEquals(expResult, result);
    }

    /**
     * Test of isDateBeforeTimePeriod method, of class TimePeriod.
     */
    public void testIsDateBeforeTimePeriod() {
       System.out.println("isDateInTimePeriod");
        Calendar cal = Calendar.getInstance();
        cal.set(2015,Calendar.AUGUST,5,12,0);
        cal.set(Calendar.MILLISECOND, 0);
        Date date = cal.getTime();
        TimePeriod instance = new TimePeriod();
        instance.setStart(cal.getTime());
        cal.set(2015, Calendar.AUGUST,10,11,0);
        instance.setEnd(cal.getTime());
        boolean expResult = false;
        boolean result = instance.isDateBeforeTimePeriod(date);
        assertEquals(expResult, result);
        
        cal.set(2015,Calendar.AUGUST,5,11,59);
        date = cal.getTime();
        expResult = true;
        result = instance.isDateBeforeTimePeriod(date);
        assertEquals(expResult, result);
        
        cal.set(2015,Calendar.AUGUST,10,12,1);
        date = cal.getTime();
        expResult = false;
        result = instance.isDateBeforeTimePeriod(date);
        assertEquals(expResult, result);
    }

    /**
     * Test of isDateAfterTimePeriod method, of class TimePeriod.
     */
    public void testIsDateAfterTimePeriod() {
        System.out.println("isDateInTimePeriod");
        Calendar cal = Calendar.getInstance();
        cal.set(2015,Calendar.AUGUST,5,12,0);
        cal.set(Calendar.MILLISECOND, 0);
        Date date = cal.getTime();
        TimePeriod instance = new TimePeriod();
        instance.setStart(cal.getTime());
        cal.set(2015, Calendar.AUGUST,10,11,0);
        instance.setEnd(cal.getTime());
        boolean expResult = false;
        boolean result = instance.isDateAfterTimePeriod(date);
        assertEquals(expResult, result);
        
        cal.set(2015,Calendar.AUGUST,5,11,59);
        date = cal.getTime();
        expResult = false;
        result = instance.isDateAfterTimePeriod(date);
        assertEquals(expResult, result);
        
        cal.set(2015,Calendar.AUGUST,10,12,1);
        date = cal.getTime();
        expResult = true;
        result = instance.isDateAfterTimePeriod(date);
        assertEquals(expResult, result);

    }

    
    
}
