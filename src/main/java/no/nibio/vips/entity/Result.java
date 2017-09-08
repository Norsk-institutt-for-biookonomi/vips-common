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

package no.nibio.vips.entity;

import java.util.Date;
import java.util.Map;
import java.util.Set;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.vividsolutions.jts.geom.Geometry;


/**
 * Represents a result from a model run
 * @copyright 2013 {@link http://www.nibio.no NIBIO}
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
    public void setValidTimeStart(Date time);
    public Date getValidTimeStart();
    public void setValidTimeEnd(Date time);
    public Date getValidTimeEnd();
    
    // Spatial validity
    public void setValidGeometry(Geometry geometry);
    public Geometry getValidGeometry();
    
    /* The result parameters in this Result object */
    public Set<String> getKeys();
    
    /* Setting and getting values */
    public void setValue(String namespace, String key, String value);
    public String getValue(String namespace, String key);
    public void setAllValues(Map<String,String> values);
    public Map<String,String> getAllValues();
    
    public void setWarningStatus(Integer warningStatus);
    public Integer getWarningStatus();
}
