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

package no.nibio.vips.model;

/**
 * Represents a model Id. Must be a 10-character string with alphanumerics and no spaces
 * @copyright 2013 {@link http://www.nibio.no NIBIO}
 * @author Tor-Einar Skog <tor-einar.skog@nibio.no>
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

    private void setId(String theId){
        theId = theId.trim();
        if(theId.length() < 10)
        {
            theId = theId + new String(new char[10-theId.length()]).replace("\0", "X");
        }
        else if(theId.length() > 10)
        {
            theId = theId.substring(0, 10);
        }
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
