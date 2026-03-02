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

package no.nibio.vips.model;

import java.util.List;
import no.nibio.vips.entity.ModelConfiguration;
import no.nibio.vips.entity.Result;

/**
 * All models must implement this interface
 * @copyright 2013 <a href="http://www.nibio.no/">NIBIO</a>
 * @author Tor-Einar Skog <tor-einar.skog@nibio.no>
 */
public interface Model {
    
    /** Default language is English */
    public static String DEFAULT_LANGUAGE = "en";
    
    public List<Result> getResult() throws ModelExcecutionException;
    
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
     * 
     * @return the License for this piece of software
     */
    public String getLicense();
    
    /**
     * 
     * @return Name of person/organization that holds the copyright, and contact information
     */
    public String getCopyright();
    
    /**
     * 
     * @return Description of model (In English). Should include interpretation of the red-yellow-green warning system
     */
    public String getModelDescription();
        
    /**
     * 
     * @param language two-letter code (<a href="http://www.loc.gov/standards/iso639-2/php/English_list.php">ISO-639-2</a>)
     * @return Description of model (In requested language, with English as fallback). Should include interpretation of the red-yellow-green warning system
     */
    public String getModelDescription(String language);
    
    /**
     * 
     * @return How to interpret the warning status (red-yellow-green, what does it mean?)
     */
    public String getWarningStatusInterpretation();
    
    /**
     * 
     * @param language two-letter code (<a href="http://www.loc.gov/standards/iso639-2/php/English_list.php">ISO-639-2</a>)
     * @return How to interpret the warning status (red-yellow-green, what does it mean?)
     */
    public String getWarningStatusInterpretation(String language);
    
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
     * 
     * @return A sample configuration in JSON format
     */
    public String getSampleConfig();
    
    /**
     * Set the configuration object (with all its possible parameters(
     * @param config 
     */
    public void setConfiguration(ModelConfiguration config) throws ConfigValidationException;
}
