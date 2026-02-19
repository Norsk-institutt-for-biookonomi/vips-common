/*
 * Copyright (c) 2014 NIBIO <http://www.nibio.no/>. 
 * 
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
