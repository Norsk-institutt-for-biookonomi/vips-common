package no.bioforsk.vips.coremanager.service;

import java.util.Map;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import no.bioforsk.vips.entity.ModelRunRequest;

/**
 * Interface for the ManagerResource. Implemented by ManagerResourceImpl
 * in VIPSCoreManager and also as client in VIPSLogic
 * @copyright 2013 <a href="http://www.bioforsk.no/">Bioforsk</a>
 * @author Tor-Einar Skog <tor-einar.skog@bioforsk.no>
 */
public interface ManagerResource {
    /**
     * 
     * @param input Credentials ("username" and "password")
     * @return user information
     */
    @POST
    @Path("users/info")
    @Consumes("application/json")
    @Produces("application/json")
    public Response getUserInfo(Map<String,String> input);
    
    @GET
    @Path("organizations/{organizationId}/users")
    @Produces("application/json")
    public Response getUsersForOrganization(@PathParam("organizationId") Integer organizationId);
    
    /**
     * Instantiates and runs the requested model
     * @param modelId
     * @param config input data for the model
     * @return list of result objects
     */
    @POST
    @Path("models/{modelId}/run")
    @Consumes("application/json")
    @Produces("application/json")
    public Response runModel(@PathParam("modelId") String modelId, ModelRunRequest request);
    
    
}
