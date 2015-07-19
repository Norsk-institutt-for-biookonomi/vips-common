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
