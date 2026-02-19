/*
 * Copyright (c) 2014 NIBIO <http://www.nibio.no/>. 
 * 
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

package no.nibio.vips.core.service;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;
import no.nibio.vips.entity.ModelConfiguration;
import no.nibio.vips.ipmdecisions.IPMDecisionsModelConfiguration;

/**
 * Interface for the ModelResource. Implemented by ModelResourceImpl
 * in VIPSCore and also as client in VIPSCoreManager
 * @copyright 2013 <a href="http://www.nibio.no/">NIBIO</a>
 * @author Tor-Einar Skog <tor-einar.skog@nibio.no>
 */
@Path("") 
public interface ModelResource {
    
    @GET
    @Path("")
    @Produces("text/html")
    public Response defaultResponse();
    
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
    
    /**
     * Instantiates and runs the requested model
     * @param config input data for the model
     * @return list of result objects
     */
    @POST
    @Path("models/run")
    @Consumes("application/json")
    @Produces("application/json")
    public Response runModel(ModelConfiguration config);
    
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
    public Response runModel(@PathParam("modelId") String modelId, ModelConfiguration config);
    
    @POST
    @Path("models/{modelId}/run/ipmd")
    @Consumes("application/json")
    @Produces("application/json")
    public Response runModelForIPMDecisions(@PathParam("modelId") String modelId, IPMDecisionsModelConfiguration config);
}
