/*
 * Copyright (c) 2020 NIBIO <http://www.nibio.no/>. 
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

package net.ipmdecisions.weather.entity;

import javax.validation.constraints.NotNull;

/**
 * @copyright 2020 <a href="http://www.nibio.no/">NIBIO</a>
 * @author Tor-Einar Skog <tor-einar.skog@nibio.no>
 */
public class WeatherParameter {
    @NotNull
    private Integer parameterCode;
    @NotNull
    private String name;
    private String description;
    @NotNull
    private String unit;

    /**
     * @return the parameterCode
     */
    public Integer getParameterCode() {
        return parameterCode;
    }

    /**
     * @param parameterCode the parameterCode to set
     */
    public void setParameterCode(Integer parameterCode) {
        this.parameterCode = parameterCode;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the unit
     */
    public String getUnit() {
        return unit;
    }

    /**
     * @param unit the unit to set
     */
    public void setUnit(String unit) {
        this.unit = unit;
    }
}
