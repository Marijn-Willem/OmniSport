package com.sports.rest.json;

import com.sports.cache.data.CompSeasonDivisionListData;
import com.sports.entity.key.CompSeasonKey;
import com.sports.rest.RestClientUtil;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("json/compseasondivisionlist/{competitionId}/{seasonId}/{clientName}/{password}")
public class CompSeasonDivisionList {
	@GET
	@Produces(MediaType.APPLICATION_JSON + ";charset=\"UTF-8\"")
	public String getCompSeasonDivisionList(@PathParam("competitionId") int competitionId,
	                                        @PathParam("seasonId") int seasonId,
	                                        @PathParam("clientName") String clientName,
	                                        @PathParam("password") String password) {
		CompSeasonKey compSeasonKey = new CompSeasonKey(competitionId, seasonId);
		Integer clientId = RestClientUtil.getValidatedClientId(clientName, password);

		return new JsonOutputCreator(clientId, compSeasonKey).createOutput(new CompSeasonDivisionListData(
		        competitionId, seasonId, clientId));
	}
}
