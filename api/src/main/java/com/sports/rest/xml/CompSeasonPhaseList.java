package com.sports.rest.xml;

import com.sports.cache.data.CompSeasonPhaseListData;
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

@Path("xml/compseasonphaselist/{competitionId}/{seasonId}/{clientName}/{password}")
public class CompSeasonPhaseList {
	@GET
	@Produces(MediaType.APPLICATION_XML)
	public String getCompSeasonPhaseList(@PathParam("competitionId") int competitionId,
										 @PathParam("seasonId") int seasonId,
										 @PathParam("clientName") String clientName,
										 @PathParam("password") String password,
										 @Context HttpServletResponse response) throws IOException {
		Integer clientId = RestClientUtil.getValidatedClientId(clientName, password);
		CompSeasonKey compSeasonKey = new CompSeasonKey(competitionId, seasonId);

		return new XmlOutputCreator(clientId, response, compSeasonKey).createOutput(
				new CompSeasonPhaseListData(competitionId, seasonId, clientId));
	}
}
