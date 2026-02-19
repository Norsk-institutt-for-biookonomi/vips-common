/*
 * Copyright (c) 2015 NIBIO <http://www.nibio.no/>. 
 * 
 * This file is part of VIPSCommon.
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
 * 
 */

package no.nibio.vips.pestmanagement;

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
