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

import java.util.Map;

/**
 * Represents the request sent to VIPSCoreManager for running a model
 * @copyright 2013 <a href="http://www.nibio.no/">NIBIO</a>
 * @author Tor-Einar Skog <tor-einar.skog@nibio.no>
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
