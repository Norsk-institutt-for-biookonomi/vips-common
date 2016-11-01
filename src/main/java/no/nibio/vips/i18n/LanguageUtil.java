/*
 * Copyright (c) 2016 NIBIO <http://www.nibio.no/>. 
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

import com.ibm.icu.util.ULocale;
import java.util.Arrays;
import java.util.List;
import static java.util.stream.Collectors.toList;

/**
 * @copyright 2016 <a href="http://www.nibio.no/">NIBIO</a>
 * @author Tor-Einar Skog <tor-einar.skog@nibio.no>
 */
public class LanguageUtil {
    
    private static List<ULocale> distinctLanguages;

    /**
     * 
     * @return All available locales with distinct languages
     * 
     */
    public static List<ULocale> getAvailableLocalesWithDistinctLanguage()
    {
        if(distinctLanguages == null)
        {
            String[] availableLanguages;
        try{
            availableLanguages = System.getProperty("no.nibio.vips.logic.AVAILABLE_LANGUAGES").split(",");
        }
        catch(NullPointerException ex){
            availableLanguages = new String[1];
            availableLanguages[0] = "en";
        }
        distinctLanguages = Arrays.asList(availableLanguages).stream()
                .map(lang -> new ULocale(lang))
                .collect(toList());
        }
        return distinctLanguages;
    }
}
