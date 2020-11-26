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

package no.nibio.vips.coremanager.service;

import java.util.Map;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import no.nibio.vips.entity.ModelRunRequest;
import no.nibio.vips.ipmdecisions.IPMDecisionsModelConfiguration;

/**
 * Interface for the ManagerResource. Implemented by ManagerResourceImpl
 * in VIPSCoreManager and also as client in VIPSCommon
 * @copyright 2013 <a href="http://www.nibio.no/">NIBIO</a>
 * @author Tor-Einar Skog <tor-einar.skog@nibio.no>
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
    
    @POST
    @Path("models/{modelId}/run/ipmd")
    @Consumes("application/json")
    @Produces("application/json")
    public Response runModelForIPMDecisions(@PathParam("modelId") String modelId, IPMDecisionsModelConfiguration config);
    
    /**
     * Lists all models available in this instance, using default language (English)
     * @return list of all models available
     */
    @GET
    @Path("models")
    @Produces("text/plain;charset=UTF-8")
    public Response printModelList();
    
    /**
     * Lists all models available in this instance, using default language (English)
     * @return list of all models available
     */
    @GET
    @Path("models/json")
    @Produces("application/json;charset=UTF-8")
    public Response printModelListJSON();
    
     /**
     * Lists all models available in this instance, using default language (English)
     * @return list of all models available
     */
    @GET
    @Path("models/json/{language}")
    @Produces("application/json;charset=UTF-8")
    public Response printModelListJSON(@PathParam("language") String language);
    
    
    /**
     * Lists all models available in this instance, using default language (English)
     * @return list of all models available
     */
    @GET
    @Path("models/{language}")
    @Produces("text/plain;charset=UTF-8")
    public Response printModelList(@PathParam("language") String language);
    
    @GET
    @Path("models/{modelId}/name")
    @Produces("text/plain;charset=UTF-8")
    public Response printModelName(@PathParam("modelId") String modelId);
    
    @GET
    @Path("models/{modelId}/name/{language}")
    @Produces("text/plain;charset=UTF-8")
    public Response printModelName(@PathParam("modelId") String modelId,@PathParam("language") String language);
    
    @GET
    @Path("models/{modelId}/license")
    @Produces("text/plain;charset=UTF-8")
    public Response printModelLicense(@PathParam("modelId") String modelId);
    
    @GET
    @Path("models/{modelId}/copyright")
    @Produces("text/plain;charset=UTF-8")
    public Response printModelCopyright(@PathParam("modelId") String modelId);
    
    @GET
    @Path("models/{modelId}/description")
    @Produces("text/plain;charset=UTF-8")
    public Response printModelDescription(@PathParam("modelId") String modelId);

    
    @GET
    @Path("models/{modelId}/description/{language}")
    @Produces("text/plain;charset=UTF-8")
    public Response printModelDescription(@PathParam("modelId") String modelId,@PathParam("language") String language);

    
    @GET
    @Path("models/{modelId}/warningstatusinterpretation")
    @Produces("text/plain;charset=UTF-8")
    public Response printModelWarningStatusInterpretation(@PathParam("modelId") String modelId);
    
    @GET
    @Path("models/{modelId}/warningstatusinterpretation/{language}")
    @Produces("text/plain;charset=UTF-8")
    public Response printModelWarningStatusInterpretation(@PathParam("modelId") String modelId,@PathParam("language") String language);
    
    
    /**
     * Prints the usage instructions from the requested model
     * @param modelId
     * @param language two-letter code (<a href="http://www.loc.gov/standards/iso639-2/php/English_list.php">ISO-639-2</a>)
     * @return 
     */
    @GET
    @Path("models/{modelId}/usage/{language}")
    @Produces("text/plain;charset=UTF-8")
    public Response printModelUsage(@PathParam("modelId") String modelId, @PathParam("language") String language);
    
    /**
     * Prints the usage instructions from the requested model
     * @param modelId
     * @param language two-letter code (<a href="http://www.loc.gov/standards/iso639-2/php/English_list.php">ISO-639-2</a>)
     * @return 
     */
    @GET
    @Path("models/{modelId}/usage")
    @Produces("text/plain;charset=UTF-8")
    public Response printModelUsage(@PathParam("modelId") String modelId);
    
    /**
     * Outputs a sample configuration for the requested model
     * @param modelId the requested model
     * @return a sample configuration for the requested model
     */
    @GET
    @Path("models/{modelId}/sampleconfig")
    @Produces("text/plain;charset=UTF-8")
    public Response printModelSampleConfig(@PathParam("modelId") String modelId);
}
