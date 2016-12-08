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

package no.nibio.vips.util;

/**
 * @copyright 2014 <a href="http://www.nibio.no/">NIBIO</a>
 * @author Tor-Einar Skog <tor-einar.skog@nibio.no>
 */
public class ArrayUtil {
    
    /**
     * Does exactly what it states: Creates an Integer array and attempts to 
     * convert all elements in the given String array to Integers and place in 
     * the Integer array.
     * @param stringArray
     * @return Array of Integers
     * @throws NumberFormatException if the String array contains something that isn't a number
     */
    public static synchronized Integer[] stringArrayToIntegerArray(String[] stringArray) throws NumberFormatException
    {
        if(stringArray == null)
        {
            return null; 
        }
        Integer[] integerArray = new Integer[stringArray.length];
        for(int i=0;i<stringArray.length;i++)
        {
            integerArray[i] = Integer.parseInt(stringArray[i]);
        }
        
        return integerArray;
    }

}
