package com.sports.rest.json;

import com.sports.cache.data.CyclingRoadPersonResultsData;
import com.sports.rest.RestClientUtil;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;

import java.io.IOException;

@Path("json/cyclingroad/personresults/{personSportId}/{seasonId}/{clientName}/{password}")
public class CyclingRoadPersonResults {
	@GET
	@Produces(MediaType.APPLICATION_JSON + ";charset=\"UTF-8\"")
	public String getPersonResults(@PathParam("personSportId") int personSportId,
								   @PathParam("seasonId") int seasonId,
								   @PathParam("clientName") String clientName,
								   @PathParam("password") String password,
								   @Context HttpServletResponse response) throws IOException {
		Integer clientId = RestClientUtil.getValidatedClientId(clientName, password);

		return new JsonOutputCreator(clientId, response).createOutput(new CyclingRoadPersonResultsData(
		        personSportId, seasonId, clientId));
	}
}
