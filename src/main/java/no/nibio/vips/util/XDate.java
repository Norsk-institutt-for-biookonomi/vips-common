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

import java.util.Calendar;
import java.util.Date;

/**
 * Adding some utils to the java.util.Date class!
 * @copyright 2018 <a href="http://www.nibio.no/">NIBIO</a>
 * @author Tor-Einar Skog <tor-einar.skog@nibio.no>
 */
public class XDate extends java.util.Date{
    
    public XDate(){
        super();
    }
    public XDate(Date date)
    {
        super();
        this.setTime(date.getTime());
    }
    
    public void add1Year()
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(this);
        cal.add(Calendar.YEAR, 1);
        this.setTime(cal.getTime().getTime());
    }
    
    public void add1Week()
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(this);
        cal.add(Calendar.WEEK_OF_YEAR, 1);
        this.setTime(cal.getTime().getTime());
    }
    
    public void add1Day()
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(this);
        cal.add(Calendar.DATE, 1);
        this.setTime(cal.getTime().getTime());
    }
    
    public void add1Hour()
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(this);
        cal.add(Calendar.HOUR_OF_DAY, 1);
        this.setTime(cal.getTime().getTime());
    }
    
    public void subtract1Year()
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(this);
        cal.add(Calendar.YEAR, -1);
        this.setTime(cal.getTime().getTime());
    }
    
    public void subtract1Week()
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(this);
        cal.add(Calendar.WEEK_OF_YEAR, -1);
        this.setTime(cal.getTime().getTime());
    }
    
    public void subtract1Day()
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(this);
        cal.add(Calendar.DATE, -1);
        this.setTime(cal.getTime().getTime());
    }
    
    public void subtract1Hour()
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(this);
        cal.add(Calendar.HOUR_OF_DAY, -1);
        this.setTime(cal.getTime().getTime());
    }
    
    public Date getYesterday(){
        Calendar cal = Calendar.getInstance();
        cal.setTime(this);
        cal.add(Calendar.HOUR_OF_DAY, -1);
        return cal.getTime();
    }
    
    public Date getTomorrow(){
        Calendar cal = Calendar.getInstance();
        cal.setTime(this);
        cal.add(Calendar.HOUR_OF_DAY, +1);
        return cal.getTime();
    }
}
