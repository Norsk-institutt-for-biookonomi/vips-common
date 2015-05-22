/*
 * Copyright (c) 2015 NIBIO <http://www.nibio.no/>. 
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

package no.bioforsk.vips.pestmanagement;

import java.util.Date;

/**
 * @copyright 2015 <a href="http://www.nibio.no/">NIBIO</a>
 * @author Tor-Einar Skog <tor-einar.skog@nibio.no>
 */
public class SprayingImpl implements Spraying{
    
    private Date sprayingDate;
    private String pesticideName;
    private Double sprayingEffectFactor;

    /**
     * @return the sprayingDate
     */
    @Override
    public Date getSprayingDate() {
        return sprayingDate;
    }

    /**
     * @param sprayingDate the sprayingDate to set
     */
    public void setSprayingDate(Date sprayingDate) {
        this.sprayingDate = sprayingDate;
    }

    /**
     * @return the pesticideName
     */
    @Override
    public String getPesticideName() {
        return pesticideName;
    }

    /**
     * @param pesticideName the pesticideName to set
     */
    public void setPesticideName(String pesticideName) {
        this.pesticideName = pesticideName;
    }

    /**
     * @return the sprayingEffectFactor
     */
    @Override
    public Double getSprayingEffectFactor() {
        return sprayingEffectFactor;
    }

    /**
     * @param sprayingEffectFactor the sprayingEffectFactor to set
     */
    public void setSprayingEffectFactor(Double sprayingEffectFactor) {
        this.sprayingEffectFactor = sprayingEffectFactor;
    }

   

}
