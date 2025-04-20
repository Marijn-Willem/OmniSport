package com.sports.rest.json;

import com.sports.cache.data.DartsPremierLeagueStandingData;
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

@Path("json/darts/premierleaguestanding/{seasonId}/{clientName}/{password}")
public class DartsPremierLeagueStanding {
	@GET
	@Produces(MediaType.APPLICATION_JSON + ";charset=\"UTF-8\"")
	public String getDartsPremierLeagueStanding(@PathParam("seasonId") int seasonId,
	                                            @PathParam("clientName") String clientName,
	                                            @PathParam("password") String password,
	                                            @Context HttpServletResponse response) throws IOException {
		CompSeasonKey compSeasonKey = new CompSeasonKey(Competition.competitionIdDartsPremierLeague, seasonId);
		Integer clientId = RestClientUtil.getValidatedClientId(clientName, password);

		return new JsonOutputCreator(clientId, response, compSeasonKey).createOutput(new DartsPremierLeagueStandingData(
		        seasonId, clientId));
	}
}
