package no.bioforsk.vips.entity;

import java.util.Map;

/**
 * @copyright 2013 <a href="http://www.bioforsk.no/">Bioforsk</a>
 * @author Tor-Einar Skog <tor-einar.skog@bioforsk.no>
 */
public class ModelRunRequest {
    private Map<String, Object> configParameters;
    private Map<String,String> loginInfo;
    private String modelId;
    private Integer batchSystemClientId;

    /**
     * Dummy constructor for Jackson
     */
    public ModelRunRequest(){
        
    }
    
    public ModelRunRequest(ModelConfiguration config)
    {
        this.configParameters = config.getConfigParameters();
        this.modelId = config.getModelId();
    }
    /**
     * @return the credentials
     */
    public Map<String,String> getLoginInfo() {
        return this.loginInfo;
    }

    /**
     * @param credentials the credentials to set
     */
    public void setLoginInfo(Map<String,String> loginInfo) {
        this.loginInfo = loginInfo;
    }

    /**
     * @return the batchSystemClientId
     */
    public Integer getBatchSystemClientId() {
        return batchSystemClientId;
    }

    /**
     * @param batchSystemClientId the batchSystemClientId to set
     */
    public void setBatchSystemClientId(Integer batchSystemClientId) {
        this.batchSystemClientId = batchSystemClientId;
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
