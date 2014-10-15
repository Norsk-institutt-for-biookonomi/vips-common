/*
 * Copyright (c) 2014 Bioforsk <http://www.bioforsk.no/>. 
 * 
 * This file is part of VIPSCommon.
 * VIPSCommon is free software: you can redistribute it and/or modify
 * it under the terms of the Bioforsk Open Source License as published by 
 * Bioforsk, either version 1 of the License, or (at your option) any
 * later version.
 * 
 * VIPSCommon is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * Bioforsk Open Source License for more details.
 * 
 * You should have received a copy of the Bioforsk Open Source License
 * along with VIPSCommon.  If not, see <http://www.bioforsk.no/licenses/>.
 * 
 */

package no.bioforsk.vips.entity;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents all configuration for a model to be run.
 * To see which configuration needs to be provided for a particular model,
 * read the getUsage() from that model.
 * 
 * @copyright 2013 {@link http://www.bioforsk.no Bioforsk}
 * @author Tor-Einar Skog <tor-einar.skog@bioforsk.no>
 */
public class ModelConfiguration {
    private String modelId;
    private Map<String, Object> configParameters;
        
    public ModelConfiguration()
    {
    }
    
    /**
     * Set a particular config parameter
     * @param parameterName
     * @param parameterValue 
     */
    public void setConfigParameter(String parameterName, Object parameterValue)
    {
        if(this.configParameters == null)
        {
            this.configParameters = new HashMap();
        }
        this.configParameters.put(parameterName, parameterValue);
    }
    
    /**
     * Returns the requested configuration parameter
     * @param parameterName
     * @return 
     */
    public Object getConfigParameter(String parameterName)
    {
        return this.configParameters != null ? this.configParameters.get(parameterName) : null;
    }

    /**
     * @return the modelId
     */
    public String getModelId() {
        return modelId;
    }

    /**
     * @param modelId the modelId to set
     */
    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    /**
     * @return the configParameters
     */
    public Map<String, Object> getConfigParameters() {
        return configParameters;
    }
    
    /**
     * @param configParameters the configParameters to set
     */
    public void setConfigParameters(Map<String, Object> configParameters) {
        this.configParameters = configParameters;
    }
    
       
}
