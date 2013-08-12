package no.bioforsk.vips.model;

import no.bioforsk.vips.entity.Result;
import java.util.List;

/**
 * All models must implement this interface
 * @copyright 2013 {@link http://www.bioforsk.no Bioforsk}
 * @author Tor-Einar Skog <tor-einar.skog@bioforsk.no>
 */
public interface Model {
    
    public List<Result> getResult();
    
    /**
     * 
     * @return 10-character ID of the model. Must be unique (at least in the current system)
     */
    public ModelId getModelId();
    
    /**
     * @return The name of the model
     */
    public String getModelName();
    
    /**
     * @return a thorough manual for this model
     */
    public String getModelUsage();
}
