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

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import org.apache.commons.validator.UrlValidator;

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
}
