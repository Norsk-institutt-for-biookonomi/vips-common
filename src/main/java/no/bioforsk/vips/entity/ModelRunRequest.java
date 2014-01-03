/*
 * Copyright (c) 2013 Bioforsk <http://www.bioforsk.no/>. 
 * 
 * This file is part of VIPSLogic.
 * VIPSLogic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * VIPSLogic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with VIPSLogic.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package no.bioforsk.vips.entity;

import java.util.Map;

/**
 * Represents the request sent to VIPSCoreManager for running a model
 * @copyright 2013 <a href="http://www.bioforsk.no/">Bioforsk</a>
 * @author Tor-Einar Skog <tor-einar.skog@bioforsk.no>
 */
public class ModelRunRequest {
    private Map<String, Object> configParameters;
    private Map<String,String> loginInfo;
    private String modelId;
    private Integer vipsCoreUserId;

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
     * @return the vipsCoreUserId
     */
    public Integer getVipsCoreUserId() {
        return vipsCoreUserId;
    }

    /**
     * @param vipsCoreUserId the vipsCoreUserId to set
     */
    public void setVipsCoreUserId(Integer vipsCoreUserId) {
        this.vipsCoreUserId = vipsCoreUserId;
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
