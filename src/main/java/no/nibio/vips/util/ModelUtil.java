/*
 * Copyright (c) 2015 NIBIO <http://www.nibio.no/>. 
 * 
 * This file is part of VIPSCommon.
 * VIPSCommon is free software: you can redistribute it and/or modify
 * it under the terms of the NIBIO Open Source License as published by 
 * NIBIO, either version 1 of the License, or (at your option) any
 * later version.
 * 
 * VIPSCommon is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * NIBIO Open Source License for more details.
 * 
 * You should have received a copy of the NIBIO Open Source License
 * along with VIPSCommon.  If not, see <http://www.nibio.no/licenses/>.
 * 
 */

package no.nibio.vips.util;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;

/**
 * @copyright 2015 <a href="http://www.nibio.no/">NIBIO</a>
 * @author Tor-Einar Skog <tor-einar.skog@nibio.no>
 */
public class ModelUtil {
    
    /**
     * Parsing text with reference to images. Image template tag format is:
     * {{filename="/path/to/filename.jpg" description="Sample description" float="[CSSFloat property]"}}
     * {{filename="/1348485230424.jpg" description="Chart for apple scab model" float="none"}}
     * 
     * These tags are parsed and transformed to html &lt;img/&gt; tags, and the image files 
     * are base64 encoded and embedded. The only mandatory parameter is filename. It refers to the
     * file relative to the Model jar in which it is placed.
     * @param text
     * @param resourceClass the class whose classloader you want to use
     * for getting the images. Use class in the package that the images are placed.
     * @return The same text with embedded image(s)
     * 
     */
    public String getTextWithBase64EncodedImages(String text, Class resourceClass) throws IOException
    {
        if(resourceClass == null)
        {
            resourceClass = this.getClass();
        }
        // Finding all {{}} tags
        String tagPattern = "\\{\\{(.*?)\\}\\}";
        Pattern tr = Pattern.compile(tagPattern, Pattern.DOTALL);
        Matcher tm = tr.matcher(text);
        
        // Preparing to find attrName="attrVal" inside the tags
        String attrPattern = "([^=\\s]+)=\"([^\"]+)\"";
        Pattern ar = Pattern.compile(attrPattern);
        
        StringBuffer transformedText = new StringBuffer();
        while(tm.find())
        {
            String tagText ="<img";
            // Here we get the filename="/path/to/filename.jpg" description="Sample description" float="[CSSFloat property]" from the matcher
            String tagContents = tm.group(1);
            Matcher am = ar.matcher(tagContents);
            Boolean filenameAttributeFound = false;
            while(am.find())
            {
                String attrName = am.group(1);
                String attrVal = am.group(2);
                // Analyzing each tag
                if(attrName.equals("filename"))
                {
                    filenameAttributeFound = true;
                    BufferedInputStream inputStream = new BufferedInputStream(resourceClass.getResourceAsStream(attrVal));
                    // Attempt to use 
                    String mimeType = URLConnection.guessContentTypeFromStream(inputStream);
                    tagText +=" src=\"data:" + mimeType +";base64," + this.getBase64EncodedImage(inputStream) + "\"";
                    
                }
                if(attrName.equals("description"))
                {
                    tagText += " alt=\"" + attrVal + "\""
                            + " title=\"" + attrVal + "\"";
                }
                if(attrName.equals("float"))
                {
                    tagText += " style=\"float:" + attrVal + "\"";
                }
            }
            tagText += " class=\"img-responsive\"/>";
            if(filenameAttributeFound)
            {
                tm.appendReplacement(transformedText,tagText);
            }    
        }
        tm.appendTail(transformedText);
        return transformedText.toString();
    }
    
    public String getBase64EncodedImage(InputStream image) throws IOException
    {
        return this.getBase64EncodedImage(IOUtils.toByteArray(image));
    }
    
    public String getBase64EncodedImage(byte[] bytes)
    {
        return Base64.encodeBase64String(bytes);
    }

}
