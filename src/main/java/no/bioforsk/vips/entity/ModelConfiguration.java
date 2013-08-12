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
