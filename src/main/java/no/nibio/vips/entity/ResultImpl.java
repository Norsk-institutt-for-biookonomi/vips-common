/*
 * Copyright (c) 2017 NIBIO <http://www.nibio.no/>. 
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

package no.nibio.vips.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.locationtech.jts.geom.Geometry;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

/**
 * Represents a result
 * @copyright 2013-2017 <a href="http://www.nibio.no">NIBIO</a>
 * @author Tor-Einar Skog <tor-einar.skog@nibio.no>
 */
@JsonSerialize(using=ResultSerializer.class)
public class ResultImpl implements Result{
    private Date validTimeStart, validTimeEnd;
    private Geometry validGeometry;
    private Map<String, String> values;
    private Integer warningStatus;

    @Override
    public void setValidTimeStart(Date time) {
        this.validTimeStart = time;
    }

    @Override
    public Date getValidTimeStart() {
        return this.validTimeStart;
    }
    
    @Override
    public void setKeys(Set<String> keys) {
        // Don't remove this!
        // This is just here to trick Jackson/Objectmapper!
    }

    @Override
    public Set<String> getKeys() {
        return this.values != null ? this.values.keySet() : new HashSet<>();
    }

    @Override
    public void setValue(String namespace, String key, String value) {
        if(this.values == null)
            this.values = new HashMap();
        this.values.put(namespace + "." + key, value);
    }

    @Override
    public String getValue(String namespace, String key) {
        return this.values != null ? this.values.get(namespace + "." + key) : null;
    }

    @Override
    public Map<String, String> getAllValues() {
        
        return this.values != null ? this.values : new HashMap<>();
    }
    
    @Override
    public void setAllValues(Map<String, String> values){
        this.values = values;
    }

    /**
     * Sorting by timestamp
     * @param t
     * @return 
     */
    @Override
    public int compareTo(Object t) {
        return this.getValidTimeStart().compareTo(((Result) t).getValidTimeStart());
    }

    @Override
    public void setWarningStatus(Integer warningStatus) {
        this.warningStatus = warningStatus;
    }

    @Override
    public Integer getWarningStatus() {
        return this.warningStatus != null ? this.warningStatus: WARNING_STATUS_NO_WARNING;
    }
    
    @Override
    public String toString(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        String retVal = "[" + format.format(validTimeStart) + "], WARNING_STATUS=" + this.getWarningStatus() + ", ";
        if(this.getKeys() != null)
        {
            for(String key: this.getKeys())
            {
                String[] keyElements = key.split("\\.");
                if(keyElements.length == 2)
                {
                    retVal += key + "=" + this.getValue(keyElements[0], keyElements[1]) + ", ";
                }
            }
        }
        return retVal;
    }

    /**
     * @return the validTimeEnd
     */
    @Override
    public Date getValidTimeEnd() {
        return validTimeEnd != null ? this.validTimeEnd : this.validTimeStart;
    }

    /**
     * @param validTimeEnd the validTimeEnd to set
     */
    @Override
    public void setValidTimeEnd(Date validTimeEnd) {
        this.validTimeEnd = validTimeEnd;
    }

    /**
     * @return the validGeometry
     */
    @Override
    public Geometry getValidGeometry() {
        return validGeometry;
    }

    /**
     * @param validGeometry the validGeometry to set
     */
    @Override
    public void setValidGeometry(Geometry validGeometry) {
        this.validGeometry = validGeometry;
    }
}
