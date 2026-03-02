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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * @copyright 2015 <a href="http://www.nibio.no/">NIBIO</a>
 * @author Tor-Einar Skog <tor-einar.skog@nibio.no>
 */
public class ParseRESTParamUtil {

    /**
     * 
     * @param dateStr
     * @param timeZone
     * @return Date or null if not parseable (input format is "yyyy-MM-dd")
     */
    public Date parseISODate(String dateStr, TimeZone timeZone)
    {
        if(dateStr == null || dateStr.isEmpty())
        {
            return null;
        }
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        format.setTimeZone(timeZone);
        try
        {
            return format.parse(dateStr);
        }
        catch(ParseException ex)
        {
            return null;
        }
    }
    
    /**
     * 
     * @param dateStr
     * @param timeZone
     * @return Date or null if not parseable (accepted format is "yyyy-MM-dd HH:mm:ss")
     */
    public Date parseISODateTime(String dateStr, TimeZone timeZone)
    {
        if(dateStr == null || dateStr.isEmpty())
        {
            return null;
        }
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        format.setTimeZone(timeZone);
        try
        {
            return format.parse(dateStr);
        }
        catch(ParseException ex)
        {
            return null;
        }
    }
    
    public Double parseDouble(String doubleStr)
    {
        if(doubleStr == null || doubleStr.isEmpty())
        {
            return null;
        }
        try
        {
            return Double.parseDouble(doubleStr);
        }
        catch(NumberFormatException ex)
        {
            return null;
        }
    }
    
    public Integer parseInteger(String intStr)
    {
        if(intStr == null || intStr.isEmpty())
        {
            return null;
        }
        try
        {
            return Integer.parseInt(intStr);
        }
        catch(NumberFormatException ex)
        {
            return null;
        }
    }
    
    public Boolean parseCheckbox(String checkboxStr)
    {
        return checkboxStr != null;
    }
}
