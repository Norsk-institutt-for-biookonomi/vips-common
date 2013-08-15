package no.bioforsk.vips.util;

import java.io.IOException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * Simple utility class for JSON, building on top of Jackson
 * @copyright 2013 <a href="http://www.bioforsk.no/">Bioforsk</a>
 * @author Tor-Einar Skog <tor-einar.skog@bioforsk.no>
 */
public class JSONUtil {
    public static boolean isValidJSON(final String json) {
        boolean valid = false;
        try {
           final JsonParser parser = new ObjectMapper().getJsonFactory()
                 .createJsonParser(json);
           while (parser.nextToken() != null) {
           }
           valid = true;
        } catch (JsonParseException jpe) {
           jpe.printStackTrace();
        } catch (IOException ioe) {
           ioe.printStackTrace();
       }

       return valid;
    }
}
