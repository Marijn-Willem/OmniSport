package com.sports.rest.json;

import com.sports.cache.data.CompSeasonPhaseListData;
import com.sports.entity.key.CompSeasonKey;
import com.sports.rest.RestClientUtil;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("json/compseasonphaselist/{competitionId}/{seasonId}/{clientName}/{password}")
public class CompSeasonPhaseList {
	@GET
	@Produces(MediaType.APPLICATION_JSON + ";charset=\"UTF-8\"")
	public String getCompSeasonPhaseList(@PathParam("competitionId") int competitionId,
	                                     @PathParam("seasonId") int seasonId,
	                                     @PathParam("clientName") String clientName,
	                                     @PathParam("password") String password) {
		Integer clientId = RestClientUtil.getValidatedClientId(clientName, password);
		CompSeasonKey compSeasonKey = new CompSeasonKey(competitionId, seasonId);

		return new JsonOutputCreator(clientId, compSeasonKey).createOutput(
				new CompSeasonPhaseListData(competitionId, seasonId, clientId));
	}
}
