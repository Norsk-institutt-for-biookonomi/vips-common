/*
 * Copyright (c) 2017 NIBIO <http://www.nibio.no/>. 
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
import java.util.Date;

/**
 * @copyright 2017 <a href="http://www.nibio.no/">NIBIO</a>
 * @author Tor-Einar Skog <tor-einar.skog@nibio.no>
 */
public class DateUtil {
    /**
     * 
     * @param from
     * @param to
     * @return 
     */
    public int getDaysBetween(Date from, Date to) throws DateUtilException
    {
        if(to.before(from))
        {
            throw new DateUtilException("Date to (" + to + ") is before date from (" + from + ")");
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(from);
        int fromYear = cal.get(Calendar.YEAR);
        int fromDOY = cal.get(Calendar.DAY_OF_YEAR);
        cal.setTime(to);
        int toYear = cal.get(Calendar.YEAR);
        int toDOY = cal.get(Calendar.DAY_OF_YEAR);
        
        int retVal = 0;
        int currentYear = fromYear;
        while(currentYear < toYear)
        {
            cal.set(Calendar.YEAR, currentYear);
            retVal += currentYear == fromYear ? cal.getActualMaximum(Calendar.DAY_OF_YEAR) - fromDOY
                    : cal.getActualMaximum(Calendar.DAY_OF_YEAR);
            currentYear++;
        }
        retVal += toDOY;
        return retVal;
    }
}
