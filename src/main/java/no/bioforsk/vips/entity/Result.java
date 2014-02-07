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
import java.util.Map;
import java.util.Set;
import org.codehaus.jackson.map.annotate.JsonDeserialize;

/**
 * Represents a result from a model run
 * @copyright 2013 {@link http://www.bioforsk.no Bioforsk}
 * @author Tor-Einar Skog <tor-einar.skog@hyper.no>
 */
@JsonDeserialize(as=ResultImpl.class)
public interface Result extends Comparable{
    
    public final static Integer WARNING_STATUS_NO_WARNING = 0;
    public final static Integer WARNING_STATUS_NO_WARNING_MISSING_DATA = 1;
    public final static Integer WARNING_STATUS_NO_RISK = 2;
    public final static Integer WARNING_STATUS_MINOR_RISK = 3;
    public final static Integer WARNING_STATUS_HIGH_RISK = 4;

    /* Time for which these results are valid */
    public void setResultValidTime(Date time);
    public Date getResultValidTime();
    
    /* The result parameters in this Result object */
    public void setKeys(Set<String> keys);
    public Set<String> getKeys();
    
    /* Setting and getting values */
    public void setValue(String namespace, String key, String value);
    public String getValue(String namespace, String key);
    public void setAllValues(Map<String,String> values);
    public Map<String,String> getAllValues();
    
    public void setWarningStatus(Integer warningStatus);
    public Integer getWarningStatus();
}
