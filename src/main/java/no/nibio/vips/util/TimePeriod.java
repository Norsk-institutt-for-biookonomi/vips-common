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
