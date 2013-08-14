package no.bioforsk.vips.model;

import no.bioforsk.vips.entity.Result;
import java.util.List;
import no.bioforsk.vips.entity.ModelConfiguration;

/**
 * All models must implement this interface
 * @copyright 2013 <a href="http://www.bioforsk.no/">Bioforsk</a>
 * @author Tor-Einar Skog <tor-einar.skog@bioforsk.no>
 */
public interface Model {
    
    /** Default language is English */
    public static String DEFAULT_LANGUAGE = "en";
    
    public List<Result> getResult();
    
    /**
     * 
     * @return 10-character ID of the model. Must be unique (at least in the current system)
     */
    public ModelId getModelId();
    
    /**
     * @return The name of the model, in English (Language code "en", referring to <a href="http://www.loc.gov/standards/iso639-2/php/English_list.php">ISO-639-2</a>)
     */
    public String getModelName();
    
    /**
     * 
     * @param language two-letter code (<a href="http://www.loc.gov/standards/iso639-2/php/English_list.php">ISO-639-2</a>)
     * @return The name of the model, in the specified language (English as fallback)
     */
    public String getModelName(String language);
    
    /**
     * @return a thorough manual for this model, in English (Language code "en", referring to <a href="http://www.loc.gov/standards/iso639-2/php/English_list.php">ISO-639-2</a>)
     */
    public String getModelUsage();
    
    /**
     * 
     * @param language two-letter code (<a href="http://www.loc.gov/standards/iso639-2/php/English_list.php">ISO-639-2</a>)
     * @return a thorough manual for this model, in the specified language (English as fallback)
     */
    public String getModelUsage(String language);
    
    /**
     * Set the configuration object (with all its possible parameters(
     * @param config 
     */
    public void setConfiguration(ModelConfiguration config);
}
