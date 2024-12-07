package com.sports.rest.xml;

import com.sports.cache.data.DivisionStandingEvolutionData;
import com.sports.entity.key.CompSeasonKey;
import com.sports.rest.RestClientUtil;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;

import java.io.IOException;

@Path("xml/divisionstandingevolution/{competitionId}/{seasonId}/{compSeasonPhaseId}/{compDivisionId}/{clientName}/{password}")
public class DivisionStandingEvolution {
	@GET
	@Produces(MediaType.APPLICATION_XML)
	public String getDivisionStandingEvolution(@PathParam("competitionId") int competitionId,
	                                           @PathParam("seasonId") int seasonId,
	                                           @PathParam("compSeasonPhaseId") int compSeasonPhaseId,
	                                           @PathParam("compDivisionId") int compDivisionId,
	                                           @PathParam("clientName") String clientName,
	                                           @PathParam("password") String password,
	                                           @Context HttpServletResponse response) throws IOException {
		CompSeasonKey compSeasonKey = new CompSeasonKey(competitionId, seasonId);
		Integer clientId = RestClientUtil.getValidatedClientId(clientName, password);

		return new XmlOutputCreator(clientId, response, compSeasonKey).createOutput(new DivisionStandingEvolutionData(
		        competitionId, seasonId, compSeasonPhaseId, compDivisionId, clientId));
	}
}
