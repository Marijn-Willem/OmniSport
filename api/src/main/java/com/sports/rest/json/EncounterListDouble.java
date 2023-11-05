package com.sports.rest.json;

import com.sports.cache.data.EncounterListDoubleData;
import com.sports.rest.RestClientUtil;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;

import java.io.IOException;

@Path("json/encounterlistdouble/{double1Id}/{double2Id}/{clientName}/{password}")
public class EncounterListDouble {
	@GET
	@Produces(MediaType.APPLICATION_JSON + ";charset=\"UTF-8\"")
	public String getEncounterListDouble(@PathParam("double1Id") int double1Id,
	                                     @PathParam("double2Id") int double2Id,
	                                     @PathParam("clientName") String clientName,
	                                     @PathParam("password") String password,
	                                     @Context HttpServletResponse response) throws IOException {
		Integer clientId = RestClientUtil.getValidatedClientId(clientName, password);

		return new JsonOutputCreator(clientId, response).createOutput(new EncounterListDoubleData(
		        double1Id, double2Id, clientId));
	}
}
