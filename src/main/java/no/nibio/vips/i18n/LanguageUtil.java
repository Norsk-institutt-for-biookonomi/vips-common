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

package no.nibio.vips.i18n;

import java.util.HashMap;
import java.util.Map;
import com.ibm.icu.util.ULocale;

/**
 * @copyright 2014 <a href="http://www.nibio.no/">NIBIO</a>
 * @author Tor-Einar Skog <tor-einar.skog@nibio.no>
 */
public class LanguageUtil {
    
    private static ULocale[] distinctLanguages;

    /**
     * 
     * @return All available locales with distinct languages
     */
    public static ULocale[] getAvailableLocalesWithDistinctLanguage()
    {
        
        if(distinctLanguages == null)
        {
            Map<String, ULocale> localeBucket = new HashMap<String, ULocale>();
            for(ULocale locale:ULocale.getAvailableLocales())
            {
                localeBucket.put(locale.getLanguage(), locale);
            }
            distinctLanguages = localeBucket.values().toArray(new ULocale[0]);
        }
        
        return distinctLanguages;
    }
}
