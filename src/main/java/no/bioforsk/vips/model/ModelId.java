package no.bioforsk.vips.model;

/**
 * Represents a model Id. Must be a 10-character string with alphanumerics and no spaces
 * @copyright 2013 {@link http://www.bioforsk.no Bioforsk}
 * @author Tor-Einar Skog <tor-einar.skog@bioforsk.no>
 */
public class ModelId {
    private char[] id;
    
    public ModelId(){
    };
    
    /**
     * 
     * @param theId Must be a 10-character string with alphanumerics and no spaces
     */
    public ModelId(String theId){
        this();
        this.setId(theId);
    }

    private void setId(String theId) {
        this.id = theId.toCharArray();
    }
    
    @Override
    public String toString()
    {
        StringBuilder retVal = new StringBuilder();
        retVal.append(this.id);
        return retVal.toString();
    }
}
