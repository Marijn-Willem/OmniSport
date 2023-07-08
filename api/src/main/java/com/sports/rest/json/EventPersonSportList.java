package com.sports.rest.json;

import com.sports.cache.data.EventPersonSportListData;
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

@Path("json/eventpersonsportlist/{competitionId}/{seasonId}/{compSeasonEventId}/{clientName}/{password}")
public class EventPersonSportList {
	@GET
	@Produces(MediaType.APPLICATION_JSON + ";charset=\"UTF-8\"")
	public String getEventPersonSportList(@PathParam("competitionId") int competitionId,
										  @PathParam("seasonId") int seasonId,
										  @PathParam("compSeasonEventId") int compSeasonEventId,
										  @PathParam("clientName") String clientName,
										  @PathParam("password") String password,
										  @Context HttpServletResponse response) throws IOException {
		CompSeasonKey compSeasonKey = new CompSeasonKey(competitionId, seasonId);
		Integer clientId = RestClientUtil.getValidatedClientId(clientName, password);

		return new JsonOutputCreator(clientId, response, compSeasonKey).createOutput(new EventPersonSportListData(
		        competitionId, seasonId, compSeasonEventId, clientId));
	}
}
