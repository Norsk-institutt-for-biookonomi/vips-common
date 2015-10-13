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

import java.util.Date;

/**
 * @copyright 2015 <a href="http://www.nibio.no/">NIBIO</a>
 * @author Tor-Einar Skog <tor-einar.skog@nibio.no>
 */
public class TimePeriod implements Comparable{
    private Date start, end;
    
    public TimePeriod()
    {
        
    }
    
    public TimePeriod(Date start, Date end)
    {
        this.start = start;
        this.end = end;
    }
    
    public boolean isDateInTimePeriod(Date date)
    {
        return ! (date.before(this.start) || date.after(this.end));
    }
    
    public boolean isDateBeforeTimePeriod(Date date)
    {
        return date.before(this.start);
    }
    
    public boolean isDateAfterTimePeriod(Date date)
    {
        return date.after(this.end);
    }

    /**
     * @return the start
     */
    public Date getStart() {
        return start;
    }

    /**
     * @param start the start to set
     */
    public TimePeriod setStart(Date start) {
        this.start = start;
        return this;
    }

    /**
     * @return the end
     */
    public Date getEnd() {
        return end;
    }

    /**
     * @param end the end to set
     */
    public TimePeriod setEnd(Date end) {
        this.end = end;
        return this;
    }

    @Override
    public int compareTo(Object t) {
        TimePeriod other = (TimePeriod) t;
        return this.getStart().compareTo(other.getStart());
    }
    
    
    
}
