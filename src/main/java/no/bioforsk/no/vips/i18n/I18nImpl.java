package no.bioforsk.no.vips.i18n;

import java.util.HashMap;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import no.bioforsk.vips.model.Model;

/**
 * Utility class to extend for classes that wish to have easy access to 
 * ResourceBundles
 * @copyright 2013 <a href="http://www.bioforsk.no">Bioforsk</a>
 * @author Tor-Einar Skog <tor-einar.skog@bioforsk.no>
 */
public class I18nImpl {
    /* 
     * The basename of the properties file. Full package
     * must be provided, e.g. for the files no/bioforsk/vips/somepackage/texts[_lang].properties,
     * please specify "no.bioforsk.vips.somepackage.texts"
     */
    protected String resourceBundleBaseName;
    protected HashMap<String, ResourceBundle> resourceBundles;
    
    /**
     * When extending this class, use the super(resourceBundleBaseName); in the subclass's constructor
     * @param resourceBundleBaseName The basename of the properties file. Full package
     * must be provided, e.g. for the files no/bioforsk/vips/somepackage/texts[_lang].properties,
     * please specify "no.bioforsk.vips.somepackage.texts"
     */
    protected I18nImpl(String resourceBundleBaseName)
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
    protected String getText(String key, String language)
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
            this.resourceBundles.put(Model.DEFAULT_LANGUAGE, ResourceBundle.getBundle(this.resourceBundleBaseName));
        }
        
        // Has the bundle for the specified language been cached?
        if(this.resourceBundles.get(language) == null)
        {
            try
            {
                this.resourceBundles.put(language, ResourceBundle.getBundle(this.resourceBundleBaseName, new Locale(language)));
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
