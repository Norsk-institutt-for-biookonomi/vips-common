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
    
    public static void debugDumpHeaders(HttpServletRequest request)
    {
        for(Enumeration<String> e = request.getHeaderNames(); e.hasMoreElements();e.nextElement())
        {
            String headerName = e.nextElement();
            System.out.println(headerName + ": " + request.getHeader(headerName));
        }
    }
    
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
}
