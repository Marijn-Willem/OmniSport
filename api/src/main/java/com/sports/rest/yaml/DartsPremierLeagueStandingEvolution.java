package com.sports.rest.yaml;

import com.sports.cache.data.DartsPremierLeagueStandingEvolutionData;
import com.sports.entity.Competition;
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

@Path("yaml/darts/premierleaguestandingevolution/{seasonId}/{clientName}/{password}")
public class DartsPremierLeagueStandingEvolution {
	@GET
	@Produces(MediaType.TEXT_PLAIN + ";charset=\"UTF-8\"")
	public String getDartsPremierLeagueStandingEvolution(@PathParam("seasonId") int seasonId,
	                                                     @PathParam("clientName") String clientName,
	                                                     @PathParam("password") String password,
	                                                     @Context HttpServletResponse response) throws IOException {
		CompSeasonKey compSeasonKey = new CompSeasonKey(Competition.competitionIdDartsPremierLeague, seasonId);
		Integer clientId = RestClientUtil.getValidatedClientId(clientName, password);

		return new YamlOutputCreator(clientId, response, compSeasonKey).createOutput(new DartsPremierLeagueStandingEvolutionData(
		        seasonId, clientId));
	}
}
