/*
 * Copyright (c) 2018 NIBIO <http://www.nibio.no/>. 
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Adding some utils to the java.util.Date class!
 * @copyright 2018 <a href="http://www.nibio.no/">NIBIO</a>
 * @author Tor-Einar Skog <tor-einar.skog@nibio.no>
 */
public class XDate extends java.util.Date{
        
    private SimpleDateFormat ISOUTCFormat;
    private TimeZone timeZone;
    
    public XDate(){
        super();
        this.timeZone = TimeZone.getDefault();
        ISOUTCFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");
        ISOUTCFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        
    }
    public XDate(Date date)
    {
        this(date, TimeZone.getDefault());
    }
    public XDate(Date date, TimeZone timeZone)
    {
        super();
        this.setTime(date.getTime());
        this.timeZone = timeZone;
    }
    
    /**
     * Move this date one year forward
     */
    public void add1Year()
    {
        this.addYears(1);
    }
    
    /**
     * Move this date one week forward
     */
    public void add1Week()
    {
        this.addWeeks(1);
    }
    
    /**
     * Move this date one day forward
     */
    public void add1Day()
    {
        this.addDays(1);
    }
    
    /**
     * Move this date one hour forward
     */
    public void add1Hour()
    {
        this.addHours(1);
    }
    
    /**
     * Move this date one year back
     */
    public void subtract1Year()
    {
        this.addYears(-1);
    }
    
    /**
     * Move this date one week back
     */
    public void subtract1Week()
    {
        this.addWeeks(-1);
    }
    
    /**
     * Move this date one day back
     */
    public void subtract1Day()
    {
        this.addDays(-1);
    }
    
    /**
     * Move this date one hour back
     */
    public void subtract1Hour()
    {
        this.addHours(-1);
    }
    
    /**
     * 
     * @return Yesterday at same time
     */
    public Date getYesterday(){
        Calendar cal = Calendar.getInstance(this.timeZone);
        cal.setTime(this);
        cal.add(Calendar.DATE, -1);
        return cal.getTime();
    }
    
    /**
     * 
     * @return Tomorrow at same time
     */
    public Date getTomorrow(){
        Calendar cal = Calendar.getInstance(this.timeZone);
        cal.setTime(this);
        cal.add(Calendar.DATE, +1);
        return cal.getTime();
    }
    
    /**
     * Move this date a number of hours forward (negative numbers move backwards)
     * @param hours 
     */
    public void addHours(Integer hours){
        Calendar cal = Calendar.getInstance(this.timeZone);
        cal.setTime(this);
        cal.add(Calendar.HOUR_OF_DAY, hours);
        this.setTime(cal.getTime().getTime());
    }
    
    /**
     * Move this date a number of days forward (negative numbers move backwards)
     * @param days 
     */
    public void addDays(Integer days){
        Calendar cal = Calendar.getInstance(this.timeZone);
        cal.setTime(this);
        cal.add(Calendar.DATE, days);
        this.setTime(cal.getTime().getTime());
    }
    
    /**
     * Move this date a number of weeks forward (negative numbers move backwards)
     * @param weeks 
     */
    public void addWeeks(Integer weeks){
        Calendar cal = Calendar.getInstance(this.timeZone);
        cal.setTime(this);
        cal.add(Calendar.WEEK_OF_YEAR, weeks);
        this.setTime(cal.getTime().getTime());
    }
    
    /**
     * Move this date a number of years forward (negative numbers move backwards)
     * @param years 
     */
    public void addYears(Integer years){
        Calendar cal = Calendar.getInstance(this.timeZone);
        cal.setTime(this);
        cal.add(Calendar.YEAR, years);
        this.setTime(cal.getTime().getTime());
    }
    
    /**
     * TODO Check that this actually returns an ISO compliant string
     * @return 
     */
    public String getISOUTCFormat()
    {
        return this.ISOUTCFormat.format(this);
    }
}
