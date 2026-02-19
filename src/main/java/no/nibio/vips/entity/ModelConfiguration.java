/*
 * Copyright (c) 2014 NIBIO <http://www.nibio.no/>. 
 * 
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
 */

package no.nibio.vips.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents all configuration for a model to be run.
 * To see which configuration needs to be provided for a particular model,
 * read the getUsage() from that model.
 * 
 * @copyright 2013 {@link http://www.nibio.no NIBIO}
 * @author Tor-Einar Skog <tor-einar.skog@nibio.no>
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
    
    public String toJSON()
    {
        ObjectMapper mapper = new ObjectMapper();
        try
        {
            return mapper.writeValueAsString(this.configParameters);
        }
        catch(JsonProcessingException ex)
        {
            return ex.getClass().getName() + ": " + ex.getMessage();
        }
    }
       
}
