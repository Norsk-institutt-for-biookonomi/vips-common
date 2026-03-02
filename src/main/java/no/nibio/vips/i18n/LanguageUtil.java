/*
 * Copyright (c) 2016 NIBIO <http://www.nibio.no/>. 
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
