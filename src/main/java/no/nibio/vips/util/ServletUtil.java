/*
 * Copyright (c) 2016 NIBIO <http://www.nibio.no/>. 
 * 
 * This file is part of VIPSCommon.
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

package no.nibio.vips.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.stream.Collectors;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

/**
 * @copyright 2016 <a href="http://www.nibio.no/">NIBIO</a>
 * @author Tor-Einar Skog <tor-einar.skog@nibio.no>
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
        String serverName = request.getHeader("X-Forwarded-Host") != null ? request.getHeader("X-Forwarded-Host") : request.getServerName();
        // If several reverse proxies are involved, parse and get the first one.
        if(serverName.contains(", "))
        {
            serverName = serverName.split(", ")[0];
        }
        return serverName;
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
     * Attempts to get the most correct client IP, even when this app is 
     * placed behind a reverse proxy
     * @param request
     * @return 
     */
    public static String getClientIP(HttpServletRequest request)
    {
        String XFF = request.getHeader("X-Forwarded-For");
        if(XFF != null && ! XFF.isEmpty())
        {
            String[] IPs = XFF.split(",");
            return IPs[0];
        }
        else
        {
            return request.getRemoteAddr();
        }
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
    
    /**
     * Quickest way for caller to get a cookie
     * @param request
     * @param cookieName
     * @return 
     */
    public static Cookie getCookie(HttpServletRequest request, String cookieName)
    {
        if(request.getCookies() == null)
        {
            return null;
        }
        
        for(Cookie cookie:request.getCookies())
        {
            if(cookie.getName().equals(cookieName))
            {
                return cookie;
            }
        }
        return null;
    }
    
    /**
     * Removes the given parameters from a query string
     * @param parametersToRemove
     * @return 
     */
    public static String getCleanedQueryString(String servletPathWithQueryString, String parameterToRemove)
    {
        List<String> whatsLeft;
        String servletPath = servletPathWithQueryString.split("\\?").length > 1 ? 
                servletPathWithQueryString.split("\\?")[0] 
                : ""; 
        whatsLeft = (servletPathWithQueryString.split("\\?").length > 1 ?
                Arrays.asList(servletPathWithQueryString.split("\\?")[1].split("&")) :
                new ArrayList<String>())
            .stream().filter(
                paramStr ->  ! paramStr.contains(parameterToRemove)
            ).collect(Collectors.toList());
        return servletPath + (!whatsLeft.isEmpty() ? "?" + String.join("&", whatsLeft) : "");
    }
}
