/*
 * Copyright (c) 2014 NIBIO <http://www.nibio.no/>. 
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

import static java.util.stream.Collectors.toList;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.validator.routines.UrlValidator;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;

/**
 * Static convenience methods for common web-related tasks.
 * Based on code found here: http://www.javapractices.com/topic/TopicAction.do?Id=180
 * 
 * @copyright 2013 <a href="http://www.nibio.no/">NIBIO</a>
 * @author Tor-Einar Skog <tor-einar.skog@nibio.no>
 * 
 */
public final class WebUtil {
  
  /**
  * Validate the form of an email address.
  *
  * <P>Return <tt>true</tt> only if 
  *<ul> 
  * <li> <tt>aEmailAddress</tt> can successfully construct an 
  * {@link javax.mail.internet.InternetAddress} 
  * <li> when parsed with "@" as delimiter, <tt>aEmailAddress</tt> contains 
  * two tokens which satisfy {@link hirondelle.web4j.util.Util#textHasContent}.
  *</ul>
  *
  *<P> The second condition arises since local email addresses, simply of the form
  * "<tt>albert</tt>", for example, are valid for 
  * {@link javax.mail.internet.InternetAddress}, but almost always undesired.
  */
  public static boolean isValidEmailAddress(String aEmailAddress){
    if (aEmailAddress == null) return false;
    boolean result = true;
    try {
      InternetAddress emailAddr = new InternetAddress(aEmailAddress);
      if ( ! hasNameAndDomain(aEmailAddress) ) {
        result = false;
      }
    }
    catch (AddressException ex){
      result = false;
    }
    return result;
  }
  
  /**
   * Checks if this is a valid internet address
   * @param aWebAddress
   * @return 
   */
  public static boolean isValidWebAddress(String aWebAddress)
  {
      if(aWebAddress.indexOf("http") != 0)
              aWebAddress = "http://" + aWebAddress;
      String[] schemes = {"http","https"};
      UrlValidator validator = new UrlValidator(schemes);
      return validator.isValid(aWebAddress);
      
  }

  private static boolean hasNameAndDomain(String aEmailAddress){
      Boolean retVal = true;
      String[] tokens = aEmailAddress.split("@");
      if(tokens.length != 2)
          return false;
      if(tokens[0].isEmpty() || tokens[1].isEmpty())
          return false;
      String[] afterAtTokens = tokens[1].split(".");
      if(afterAtTokens.length == 1)
          return false;
      for(int i=0;i<afterAtTokens.length;i++)
      {
          if(afterAtTokens[i].isEmpty())
              return false;
      }
     return true;
  }
  
  /**
   * Replaces URL in text with an &lt;a&gt; link
   * @param text
   * @return 
   */
  public static String replaceTextURLWithLink(String text, String target)
  {
      if(target == null)
          target = ""; 
      //return text.replaceAll("(.*://[^<>[:space:]]+[[:alnum:]/])", "<a href=\"$1\">$1</a>");
      return text.replaceAll("(\\A|\\s)((http|https|ftp|mailto):\\S+)(\\s|\\z)", "$1<a href=\"$2\" target=\"" + target + "\">$2</a>$4");
  }
  
  /**
   * 
   * @param URL
   * @param queryParams
   * @param arrayQueryParams
   * @return
   * @throws IOException 
   */
  public static String getOutputFromPostRequest(String URL, Map<String,String> queryParams, Map<String, String[]> arrayQueryParams) throws IOException
  {
    CloseableHttpClient client = HttpClients.createDefault();
    String output = "";
    try
    {
        HttpPost post = new HttpPost(URL);
        post.setHeader("User-Agent", "VIPS");
        // Getting the 
        List<BasicNameValuePair> params = queryParams.keySet().stream().map(
                        key -> new BasicNameValuePair(key, queryParams.get(key))
                )
                .collect(toList());
        
        arrayQueryParams.keySet().stream().forEach((key) -> {
            Arrays.asList(arrayQueryParams.get(key)).stream().forEach(value -> params.add(new BasicNameValuePair(key, value)));
            
        });
        
        post.setEntity(new UrlEncodedFormEntity(params));
        
        
        HttpResponse response = client.execute(post);
        BufferedReader rd = new BufferedReader(
            new InputStreamReader(response.getEntity().getContent())
        );
        String line = "";
        while ((line = rd.readLine()) != null) {
              output += line + "\n";
        }
    }
    catch (UnsupportedEncodingException ex) {
        Logger.getLogger(WebUtil.class.getName()).log(Level.SEVERE, null, ex);
    }      finally{
        client.close();
    }
    return output;
  }
}
