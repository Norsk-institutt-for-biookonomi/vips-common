/*
 * Copyright (c) 2014 Bioforsk <http://www.bioforsk.no/>. 
 * 
 * This file is part of VIPSCommon.
 * VIPSCommon is free software: you can redistribute it and/or modify
 * it under the terms of the Bioforsk Open Source License as published by 
 * Bioforsk, either version 1 of the License, or (at your option) any
 * later version.
 * 
 * VIPSCommon is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * Bioforsk Open Source License for more details.
 * 
 * You should have received a copy of the Bioforsk Open Source License
 * along with VIPSCommon.  If not, see <http://www.bioforsk.no/licenses/>.
 * 
 */

package no.bioforsk.vips.util;

import java.util.Enumeration;
import javax.servlet.http.HttpServletRequest;

/**
 * @copyright 2013 <a href="http://www.bioforsk.no/">Bioforsk</a>
 * @author Tor-Einar Skog <tor-einar.skog@bioforsk.no>
 */
public class ServletUtil {
    /**
     * Because we may be behind a reverse proxy, we might have to read Apache headers
     * to resolve the actual called server name
     * @param request
     * @return 
     */
    public static String getServerName(HttpServletRequest request)
    {
        // request.getServerName() is fallback if we're not behind a reverse proxy
        return request.getHeader("X-Forwarded-Host") != null ? request.getHeader("X-Forwarded-Host") : request.getServerName();
    }
    
    /**
     * For debugging purposes. Dumps all headers in request to stdout
     * @param request 
     */
    public static void debugDumpHeaders(HttpServletRequest request)
    {
        for(Enumeration<String> e = request.getHeaderNames(); e.hasMoreElements();e.nextElement())
        {
            String headerName = e.nextElement();
            System.out.println(headerName + ": " + request.getHeader(headerName));
        }
    }
    
    /**
     * 
     * @param request
     * @return the context path, regardless of reverse proxy mixing stuff up
     */
    public static String getContextPath(HttpServletRequest request)
    {
        return request.getHeader("X-Forwarded-Host") != null ? "" : request.getContextPath();
    }
    
    /**
     * 
     * @param request
     * @return 
     */
    public static String getNormalizedServerNameAndPort(HttpServletRequest request)
    {
        return ServletUtil.getServerName(request) + (request.getServerPort() != 80 && request.getHeader("X-Forwarded-Host") == null ? ":" + request.getServerPort() : "");
    }
    
    /**
     * Reconstructs the entire URL (including protocol) that was used for this request
     * TODO: Check for https (how to??)
     * @param request
     * @return 
     */
    public static String getCompleteRequestURL(HttpServletRequest request)
    {
        return "http://" + getNormalizedServerNameAndPort(request) + getFullRequestURI(request);
    }
    
    /**
     * This method builds a full RequestURI, including query string, ignoring ServletContext,
     * thus enabling correct restore of request when behind reverse proxy
     * @param request
     * @return 
     */
    public static String getFullRequestURI(HttpServletRequest request)
    {
        StringBuilder retVal = new StringBuilder(request.getServletPath());
        if(request.getPathInfo() != null)
        {
            retVal.append(request.getPathInfo());
        }
        if(request.getQueryString() != null)
        {
            retVal.append("?").append(request.getQueryString());
        }
        return retVal.toString();
    }
    
    public static Integer getIntegerParameter(HttpServletRequest request, String parameterName)
    {
        try
        {
            return Integer.valueOf(request.getParameter(parameterName));
        }
        catch(NullPointerException ex) {return null; }
        catch(NumberFormatException nfe) {return null;}
    }
}
