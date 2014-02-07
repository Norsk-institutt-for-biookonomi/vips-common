/*
 * Copyright (c) 2014 Bioforsk <http://www.bioforsk.no/>. 
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

package no.bioforsk.vips.model;

/**
 * Represents a model Id. Must be a 10-character string with alphanumerics and no spaces
 * @copyright 2013 {@link http://www.bioforsk.no Bioforsk}
 * @author Tor-Einar Skog <tor-einar.skog@bioforsk.no>
 */
public class ModelId {
    private char[] id;
    
    public ModelId(){
    };
    
    /**
     * 
     * @param theId Must be a 10-character string with alphanumerics and no spaces
     */
    public ModelId(String theId){
        this();
        this.setId(theId);
    }

    private void setId(String theId) {
        this.id = theId.toCharArray();
    }
    
    @Override
    public String toString()
    {
        StringBuilder retVal = new StringBuilder();
        retVal.append(this.id);
        return retVal.toString();
    }
}
