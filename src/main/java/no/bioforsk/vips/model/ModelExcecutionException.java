package no.bioforsk.vips.model;

/**
 * Thrown if the excecution of the model fails
 * @copyright 2013 Bioforsk
 * @author Tor-Einar Skog <tor-einar.skog@bioforsk.no>
 */
public class ModelExcecutionException extends Exception {

    /**
     * Creates a new instance of
     * <code>ModelExcecutionException</code> without detail message.
     */
    public ModelExcecutionException() {
    }

    /**
     * Constructs an instance of
     * <code>ModelExcecutionException</code> with the specified detail message.
     *
     * @param msg the detail message.
     */
    public ModelExcecutionException(String msg) {
        super(msg);
    }
}
