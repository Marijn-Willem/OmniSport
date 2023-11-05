package com.sports.rest.json;

import com.sports.cache.data.EncounterListPersonSportData;
import com.sports.rest.RestClientUtil;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;

import java.io.IOException;

@Path("json/encounterlistpersonsport/{personSport1Id}/{personSport2Id}/{clientName}/{password}")
public class EncounterListPersonSport {
	@GET
	@Produces(MediaType.APPLICATION_JSON + ";charset=\"UTF-8\"")
	public String getEncounterListPersonSport(@PathParam("personSport1Id") int personSport1Id,
	                                          @PathParam("personSport2Id") int personSport2Id,
	                                          @PathParam("clientName") String clientName,
	                                          @PathParam("password") String password,
	                                          @Context HttpServletResponse response) throws IOException {
		Integer clientId = RestClientUtil.getValidatedClientId(clientName, password);

		return new JsonOutputCreator(clientId, response).createOutput(new EncounterListPersonSportData(
		        personSport1Id, personSport2Id, clientId));
	}
}
