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

package no.nibio.vips.i18n;

import java.util.HashMap;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import no.nibio.vips.model.Model;

/**
 * Utility class to extend for classes that wish to have easy access to.
 * Could also be used as a util class (without extending)
 * ResourceBundles
 * @copyright 2013 <a href="http://www.nibio.no">NIBIO</a>
 * @author Tor-Einar Skog <tor-einar.skog@nibio.no>
 */
public class I18nImpl {
    /* 
     * The basename of the properties file. Full package
     * must be provided, e.g. for the files no/nibio/vips/somepackage/texts[_lang].properties,
     * please specify "no.nibio.vips.somepackage.texts"
     */
    protected String resourceBundleBaseName;
    protected HashMap<String, ResourceBundle> resourceBundles;
    
    /**
     * When extending this class, use the super(resourceBundleBaseName); in the subclass's constructor
     * @param resourceBundleBaseName The basename of the properties file. Full package
     * must be provided, e.g. for the files no/nibio/vips/somepackage/texts[_lang].properties,
     * please specify "no.nibio.vips.somepackage.texts"
     */
    public I18nImpl(String resourceBundleBaseName)
    {
        this.resourceBundleBaseName = resourceBundleBaseName;
    }
    
    /**
     * Get text in specified language
     * @param key keyword for the requested text
     * @param language two-letter code (<a href="http://www.loc.gov/standards/iso639-2/php/English_list.php">ISO-639-2</a>)
     * @return the requested text in the specified language or in default language (English) if the
     * text does not exist in the specified language, or null if the text doesn't exist at all
     */
    public String getText(String key, String language)
    {
        // Input control
        if(language == null)
        {
            language = Model.DEFAULT_LANGUAGE;
        }
        
        // Must the bundles cache be instantiated?
        if(this.resourceBundles == null)
        {
            this.resourceBundles = new HashMap();
            // We explicitly use the current classloader to ensure that we find the resource bundle
            // This is an issue when using the module classloading system in JBoss/WildFly
            this.resourceBundles.put(Model.DEFAULT_LANGUAGE, ResourceBundle.getBundle(this.resourceBundleBaseName, new Locale(Model.DEFAULT_LANGUAGE), this.getClass().getClassLoader()));
        }
        
        // Has the bundle for the specified language been cached?
        if(this.resourceBundles.get(language) == null)
        {
            try
            {
                // We explicitly use the current classloader to ensure that we find the resource bundle
                // This is an issue when using the module classloading system in JBoss/WildFly
                this.resourceBundles.put(language, ResourceBundle.getBundle(this.resourceBundleBaseName, new Locale(language),this.getClass().getClassLoader()));
            }
            catch(MissingResourceException mre)
            {
                // The bundle does not exist, we return the value from the default bundle
                return this.resourceBundles.get(Model.DEFAULT_LANGUAGE).getString(key);
            }
        }
        // We return the text in specified language
        return this.resourceBundles.get(language).getString(key);
    }
    
}
