/*
 * Copyright (c) 2014 NIBIO <http://www.nibio.no/>. 
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
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

/**
 * Multiple parameters mapped by Date
 * @copyright 2013 <a href="http://www.nibio.no/">NIBIO</a>
 * @author Tor-Einar Skog <tor-einar.skog@nibio.no>
 */
public class DateMap {
    

    private final HashMap matrix;
    private final DecimalFormat decimalFormat;
    private final HashSet allParamNames;

    /** Creates a new instance of DateMap */
    public DateMap()
    {
        this.matrix = new HashMap();
        this.allParamNames = new HashSet();
        this.decimalFormat = new DecimalFormat("0.0");
    }

    /**
     * Set a double value
     * @param date
     * @param paramName
     * @param value 
     */
    public void setParamDoubleValueForDate(Date date, String paramName, Double value)
    {
        this.setParamValueForDate(date, paramName, value);
    }

    /**
     * Get a double value
     * @param date
     * @param paramName
     * @return 
     */
    public Double getParamDoubleValueForDate(Date date, String paramName)
    {
        try
        {
            return (Double) this.getParamValueForDate(date, paramName);
        }
        catch(NullPointerException ex) {return null; }
        catch(ClassCastException npe) {return null;}
    }

    /**
     * Set a parameter value of type Float for a given date
     * @param date
     * @param paramName
     * @param value
     */
    public void setParamFloatValueForDate(Date date, String paramName, Float value)
    {
        this.setParamValueForDate(date, paramName, value);
    }

    /**
     *
     * @param date
     * @param paramName
     * @return a parameter value of type Float for a given date
     */
    public Float getParamFloatValueForDate(Date date, String paramName)
    {
        try
        {
            return (Float) this.getParamValueForDate(date, paramName);
        }
        catch(NullPointerException ex) {return null; }
        catch(ClassCastException npe) {return null;}
    }

    /**
     * Set a parameter value of type Integer for a given date
     * @param date
     * @param paramName
     * @param value
     */
    public void setParamIntValueForDate(Date date, String paramName, Integer value)
    {
        this.setParamValueForDate(date, paramName, value);
    }

    /**
     *
     * @param date
     * @param paramName
     * @return a parameter value of type Integer for a given date
     */
    public Integer getParamIntValueForDate(Date date, String paramName)
    {
        try
        {
            return (Integer) this.getParamValueForDate(date, paramName);
        }
        catch(NullPointerException ex) {return null; }
        catch(ClassCastException npe) {return null;}
    }

    /**
     * Set a parameter value of type String for a given date
     * @param date
     * @param paramName
     * @param value
     */
    public void setParamStringValueForDate(Date date, String paramName, String value)
    {
        this.setParamValueForDate(date, paramName, value);
    }

    /**
     * Get a string value
     * @param date
     * @param paramName
     * @return 
     */
    public String getParamStringValueForDate(Date date, String paramName)
    {
        try
        {
            return (String) this.getParamValueForDate(date, paramName);
        }
        catch(NullPointerException ex) {return null; }
        catch(ClassCastException npe) {return null;}
    }

    /**
     * Set a parameter value for a given date
     * @param date
     * @param paramName
     * @param value
     */
    public void setParamValueForDate(Date date, String paramName, Object value)
    {
        HashMap valueSet = (HashMap) this.matrix.get(date);
        if(valueSet == null)
        {
            valueSet = new HashMap();
            this.matrix.put(date, valueSet);
        }
        valueSet.put(paramName, value);
        this.allParamNames.add(paramName);
    }

    /**
     * Get a parameter value for a given date
     * @param date
     * @param paramName
     * @return the value, or NULL if value not found
     */
    public Object getParamValueForDate(Date date, String paramName)
    {
        try
        {
            return ((HashMap)this.matrix.get(date)).get(paramName);
        }
        catch(NullPointerException ex) {return null; }
        catch(ClassCastException npe) {return null;}
    }


    /**
     * Returnerer en samling av de labels som finnes for den gitte datoen
     * @param date
     * @return
     */
    public Set<String> getValueLabelsForDate(Date date)
    {
        try
        {
            return ((HashMap)this.matrix.get(date)).keySet();
        }
        catch(NullPointerException ex) {return null; }
        catch(ClassCastException npe) {return null;}
    }

    /**
     * Returnerer standard formattering for en verdi
     * <ul>
     *  <li>Strenger og heltall gjengis "in natura"</li>
     *  <li>Flyttall (inkl. double) gjengis med to desimaler</li>
     * </ul>
     * @param date
     * @param paramName
     * @return
     */
    public String getDefaultFormattedValueForDate(Date date, String paramName)
    {
        Object retVal = this.getParamValueForDate(date, paramName);
        if(retVal != null)
        {
            if(retVal instanceof Float || retVal instanceof Double)
            {
                
                return this.decimalFormat.format(retVal);
            }
            else
            {
                return retVal.toString();
            }
        }
        else
            return "";
    }
    
    /**
     * Print values for a given date to standard out
     * @param date
     */
    public void printValuesForDate(Date date)
    {
        System.out.println(this.getValuesForDate(date));
    }

    /**
     * 
     * @param date
     * @return
     */
    private String getValuesForDate(Date date)
    {
        try
        {
            HashMap valueSet = (HashMap)this.matrix.get(date);
            if(valueSet != null)
            {
                StringBuilder result = new StringBuilder(date.toString() + ": ");
                for(Iterator i = valueSet.keySet().iterator();i.hasNext();)
                {
                    String key = (String) i.next();
                    result.append(key).append("=").append(valueSet.get(key).toString()).append(",");
                }
                return result.toString();
            }
            else
            {
                return date.toString() + ": No values found for this date";
            }
        }
        catch(NullPointerException npe) {return "No values";}
        catch(ClassCastException cce) {return "A ClassCastException occured. Message is (" + cce.getMessage() +")";}
    }

    /**
     * 
     * @return The first date with values 
     */
    public Date getFirstDateWithValues()
    {
        ArrayList<Date> dates = new ArrayList(this.matrix.keySet());
        if(dates.isEmpty())
        {
            return null;
        }
        else
        {
            Collections.sort(dates);
            return dates.get(0);
        }
    }
    
    /**
     * 
     * @return the last date with values
     */
    public Date getLastDateWithValues()
    {
        ArrayList<Date> dates = new ArrayList(this.matrix.keySet());
        if(dates.isEmpty())
        {
            return null;
        }
        else
        {
            Collections.sort(dates);
            return dates.get(dates.size()-1);
        }
    }
    
    /**
     * 
     *  return all dates with values, sorted ascending
     */
    public List<Date> getSortedDateKeys()
    {
        ArrayList<Date> keys = new ArrayList(this.matrix.keySet());
        Collections.sort(keys);
        return keys;
    }
    
    /**
     * 
     * @param timeZone
     * @return 
     */
    public List<Date> getSortedDateAtMidnightKeys(TimeZone timeZone)
    {
        List<Date> retVal = new ArrayList<Date>();
        Calendar cal = Calendar.getInstance(timeZone);
        for(Date timeStamp:this.getSortedDateKeys())
        {
            cal.setTime(timeStamp);
            if(cal.get(Calendar.HOUR_OF_DAY) == 0)
                retVal.add(timeStamp);
        }
        return retVal;
    }
    
    /**
     * 
     * @param parameter
     * @return The first date with value for this parameter, null if not set
     */
    public Date getFirstDateWithParameterValue(String parameter)
    {
        ArrayList<Date> dates = new ArrayList(this.matrix.keySet());
        if(dates.isEmpty())
        {
            return null;
        }
        else
        {
            Collections.sort(dates);
            for(Date date:dates)
            {
                if(this.getParamValueForDate(date, parameter) != null)
                {
                    return date;
                }
            }
        }
        return null;
    }
    
    /**
     * 
     * @param parameter
     * @return The last date with value for this parameter, null if not set
     */
    public Date getLastDateWithParameterValue(String parameter)
    {
        ArrayList<Date> dates = new ArrayList(this.matrix.keySet());
        if(dates.isEmpty())
        {
            return null;
        }
        else
        {
            Collections.sort(dates);
            Collections.reverse(dates);
            for(Date date:dates)
            {
                if(this.getParamValueForDate(date, parameter) != null)
                {
                    return date;
                }
            }
        }
        return null;
    }
    
    /**
     * Get a list of all values for a parameter. The list is sorted by date.
     * @param parameter
     * @return 
     */
    public List getAllValuesForParameterAsList(String parameter)
    {
        List retVal = new ArrayList();
        ArrayList<Date> dates = new ArrayList(this.matrix.keySet());
        if(dates.isEmpty())
        {
            return null;
        }
        else
        {
            Collections.sort(dates);
            for(Date date:dates)
            {
                retVal.add(this.getParamValueForDate(date, parameter));
            }
        }
        return retVal;
    }

    @Override
    public String toString()
    {
        // Get all date keys, sort them
        ArrayList keys = new ArrayList(this.matrix.keySet());
        Collections.sort(keys);
        StringBuilder buffer = new StringBuilder();
        for(Iterator<Date> i = keys.iterator();i.hasNext();)
        {
            Date date = i.next();
            buffer.append(this.getValuesForDate(date)).append("\n");
        }
        return buffer.toString();
    }
    
    /**
     * Outputs contents as CSV
     * @return 
     */
    public String toCSV()
    {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
        // Get all date keys, sort them
        ArrayList keys = new ArrayList(this.matrix.keySet());
        Collections.sort(keys);
        StringBuilder buffer = new StringBuilder();
        List<String> paramNames = new ArrayList(this.allParamNames);
        buffer.append("Timestamp");
        for(String paramName:paramNames)
        {
            buffer.append(",").append(paramName);
        }
        buffer.append("\n");
        for(Iterator<Date> i = keys.iterator();i.hasNext();)
        {
            Date date = i.next();
            buffer.append(format.format(date));
            for(String paramName:paramNames)
            {
                buffer.append(",").append(this.getParamValueForDate(date, paramName));
            }
            buffer.append("\n");
        }
        return buffer.toString();
    }
}
