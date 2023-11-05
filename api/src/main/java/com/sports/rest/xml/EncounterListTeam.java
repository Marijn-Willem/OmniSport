package com.sports.rest.xml;

import com.sports.cache.data.EncounterListTeamData;
import com.sports.rest.RestClientUtil;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;

import java.io.IOException;

@Path("xml/encounterlistteam/{team1Id}/{team2Id}/{clientName}/{password}")
public class EncounterListTeam {
	@GET
	@Produces(MediaType.APPLICATION_XML)
	public String getEncounterListTeam(@PathParam("team1Id") int team1Id,
	                                   @PathParam("team2Id") int team2Id,
	                                   @PathParam("clientName") String clientName,
	                                   @PathParam("password") String password,
	                                   @Context HttpServletResponse response) throws IOException {
		Integer clientId = RestClientUtil.getValidatedClientId(clientName, password);

		return new XmlOutputCreator(clientId, response).createOutput(new EncounterListTeamData(
		        team1Id, team2Id, clientId));
	}
}
