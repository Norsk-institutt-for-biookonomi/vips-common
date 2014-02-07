/*
 * Copyright (c) 2014 Bioforsk <http://www.bioforsk.no/>. 
 * 
 * This file is part of VIPSCommon.
 * VIPSCommon is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * VIPSCommon is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with VIPSCommon.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package no.bioforsk.vips.entity;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Represents a result
 * @copyright 2013 <a href="http://www.bioforsk.no">Bioforsk</a>
 * @author Tor-Einar Skog <tor-einar.skog@bioforsk.no>
 */
public class ResultImpl implements Result{
    private Date resultValidTime;
    private Set<String> keys;
    private Map<String, String> values;
    private Integer warningStatus;

    @Override
    public void setResultValidTime(Date time) {
        this.resultValidTime = time;
    }

    @Override
    public Date getResultValidTime() {
        return this.resultValidTime;
    }

    @Override
    public void setKeys(Set<String> keys) {
        this.keys = keys;
    }

    @Override
    public Set<String> getKeys() {
        return this.values.keySet();
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
        return this.values;
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
        return this.getResultValidTime().compareTo(((Result) t).getResultValidTime());
    }

    @Override
    public void setWarningStatus(Integer warningStatus) {
        this.warningStatus = warningStatus;
    }

    @Override
    public Integer getWarningStatus() {
        return this.warningStatus != null ? this.warningStatus: WARNING_STATUS_NO_WARNING;
    }
    
}
