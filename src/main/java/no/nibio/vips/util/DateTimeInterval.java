/*
 * Copyright (c) 2018 NIBIO <http://www.nibio.no/>. 
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
import java.util.List;
import java.util.Optional;
import java.util.OptionalLong;

/**
 * Util for simple treatment of DateTime interval/period
 * ...meaning a period that starts with a date and ends with a date
 * @copyright 2018 <a href="http://www.nibio.no/">NIBIO</a>
 * @author Tor-Einar Skog <tor-einar.skog@nibio.no>
 */
public class DateTimeInterval {

    private Date start, end;
    public DateTimeInterval(Date start, Date end){
        this.start = start;
        this.end = end;
    }
    
    public Boolean isDateInInterval(Date aDate){
        return aDate.compareTo(this.start) >= 0 && aDate.compareTo(this.end) <= 0;
    }
    
    public Boolean isDateAfterInterval(Date aDate){
        return aDate.compareTo(this.end) >= 0;
    }
    
    public Boolean isDateBeforeInterval(Date aDate){
        return aDate.compareTo(this.start) <= 0;
    }

    /**
     * Given a list of periods, finds the last end date of all periods
     * @param intervals the set of periods to compare
     * @return 
     */
    public static Date getLastEndDate(List<DateTimeInterval> intervals)
    {
        OptionalLong max = intervals.stream().filter(i -> i != null).mapToLong(i -> i.getEnd().getTime()).max();
        return max.isPresent() ? new Date(max.getAsLong()) : null;
    }
    
    /**
     * Given a list of periods, finds the last end date of all periods
     * @param intervals the set of periods to compare
     * @return 
     */
    public static Date getFirstStartDate(List<DateTimeInterval> intervals)
    {
        OptionalLong min = intervals.stream().filter(i -> i != null).mapToLong(i->i.getStart().getTime()).min();
        return min.isPresent() ? new Date(min.getAsLong()) : null;
    }
    
    public static Boolean isDateInIntervals(Date aDate, List<DateTimeInterval> intervals)
    {
        Optional<DateTimeInterval> interval = intervals.stream().filter(i -> i != null && i.isDateInInterval(aDate)).findFirst();
        return interval.isPresent();
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
    public void setStart(Date start) {
        this.start = start;
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
    public void setEnd(Date end) {
        this.end = end;
    }
}
