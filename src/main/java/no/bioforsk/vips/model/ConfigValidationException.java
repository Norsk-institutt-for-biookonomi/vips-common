package no.bioforsk.vips.model;

/**
 * Thrown when the configuration of the model is incorrect,
 * i.e. when input data do not meet the criteria of the model
 * @copyright 2013 Bioforsk
 * @author Tor-Einar Skog <tor-einar.skog@bioforsk.no>
 */
public class ConfigValidationException extends Exception {

    /**
     * Creates a new instance of
     * <code>ConfigValidationException</code> without detail message.
     */
    public ConfigValidationException() {
    }

    /**
     * Constructs an instance of
     * <code>ConfigValidationException</code> with the specified detail message.
     *
     * @param msg the detail message.
     */
    public ConfigValidationException(String msg) {
        super(msg);
    }
}
