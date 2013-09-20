package no.bioforsk.vips.util;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

/**
 * Multiple parameters mapped by Date
 * @copyright 2013 <a href="http://www.bioforsk.no/">Bioforsk</a>
 * @author Tor-Einar Skog <tor-einar.skog@bioforsk.no>
 */
public class DateMap {
    

    private HashMap matrix;
    private DecimalFormat decimalFormat;

    /** Creates a new instance of DateMap */
    public DateMap()
    {
        this.matrix = new HashMap();
        this.decimalFormat = new DecimalFormat("0.0");
    }

    private Date getJavaUtilDate(Date date)
    {
        if(date instanceof java.sql.Date)
            return new Date(date.getTime());
        else
            return date;
    }

    public void setParamDoubleValueForDate(Date date, String paramName, Double value)
    {
        this.setParamValueForDate(getJavaUtilDate(date), paramName, value);
    }

    public Double getParamDoubleValueForDate(Date date, String paramName)
    {
        try
        {
            return (Double) this.getParamValueForDate(getJavaUtilDate(date), paramName);
        }
        catch(NullPointerException | ClassCastException npe) {return null;}
    }

    /**
     * Set a parameter value of type Float for a given date
     * @param date
     * @param paramName
     * @param value
     */
    public void setParamFloatValueForDate(Date date, String paramName, Float value)
    {
        this.setParamValueForDate(getJavaUtilDate(date), paramName, value);
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
            return (Float) this.getParamValueForDate(getJavaUtilDate(date), paramName);
        }
        catch(NullPointerException | ClassCastException npe) {return null;}
    }

    /**
     * Set a parameter value of type Integer for a given date
     * @param date
     * @param paramName
     * @param value
     */
    public void setParamIntValueForDate(Date date, String paramName, Integer value)
    {
        this.setParamValueForDate(getJavaUtilDate(date), paramName, value);
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
            return (Integer) this.getParamValueForDate(getJavaUtilDate(date), paramName);
        }
        catch(NullPointerException | ClassCastException npe) {return null;}
    }

    /**
     * Set a parameter value of type String for a given date
     * @param date
     * @param paramName
     * @param value
     */
    public void setParamStringValueForDate(Date date, String paramName, String value)
    {
        this.setParamValueForDate(getJavaUtilDate(date), paramName, value);
    }

    public String getParamStringValueForDate(Date date, String paramName)
    {
        try
        {
            return (String) this.getParamValueForDate(getJavaUtilDate(date), paramName);
        }
        catch(NullPointerException | ClassCastException npe) {return null;}
    }

    /**
     * Set a parameter value for a given date
     * @param date
     * @param paramName
     * @param value
     */
    public void setParamValueForDate(Date date, String paramName, Object value)
    {
        HashMap valueSet = (HashMap) this.matrix.get(getJavaUtilDate(date));
        if(valueSet == null)
        {
            valueSet = new HashMap();
            this.matrix.put(getJavaUtilDate(date), valueSet);
        }
        valueSet.put(paramName, value);
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
            return ((HashMap)this.matrix.get(getJavaUtilDate(date))).get(paramName);
        }
        catch(NullPointerException | ClassCastException npe) {return null;}
    }


    /**
     * Returnerer en samling av de labels som finnes for den gitte datoen
     * @param date
     * @return
     */
    public Set getValueLabelsForDate(Date date)
    {
        try
        {
            return ((HashMap)this.matrix.get(getJavaUtilDate(date))).keySet();
        }
        catch(NullPointerException | ClassCastException npe) {return null;}
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
        Object retVal = this.getParamValueForDate(getJavaUtilDate(date), paramName);
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
        System.out.println(this.getValuesForDate(getJavaUtilDate(date)));
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
            HashMap valueSet = (HashMap)this.matrix.get(getJavaUtilDate(date));
            if(valueSet != null)
            {
                StringBuilder result = new StringBuilder(getJavaUtilDate(date).toString() + ": ");
                for(Iterator i = valueSet.keySet().iterator();i.hasNext();)
                {
                    String key = (String) i.next();
                    result.append(key).append("=").append(valueSet.get(key).toString()).append(",");
                }
                return result.toString();
            }
            else
            {
                return getJavaUtilDate(date).toString() + ": No values found for this date";
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
    
    public List<Date> getSortedDateKeys()
    {
        ArrayList<Date> keys = new ArrayList(this.matrix.keySet());
        Collections.sort(keys);
        return keys;
    }
    
    public List<Date> getSortedDateAtMidnightKeys(TimeZone timeZone)
    {
        List<Date> retVal = new ArrayList<>();
        Calendar cal = Calendar.getInstance(timeZone);
        for(Date timeStamp:this.getSortedDateKeys())
        {
            cal.setTime(timeStamp);
            if(cal.get(Calendar.HOUR_OF_DAY) == 0)
                retVal.add(timeStamp);
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
            buffer.append(this.getValuesForDate(getJavaUtilDate(date))).append("\n");
        }
        return buffer.toString();
    }
}
