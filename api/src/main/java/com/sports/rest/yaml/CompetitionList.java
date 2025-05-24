package com.sports.rest.yaml;

import com.sports.cache.data.CompetitionListData;
import com.sports.rest.RestClientUtil;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;

import java.io.IOException;

@Path("yaml/competitionlist/{sportId}/{clientName}/{password}")
public class CompetitionList {
	@GET
	@Produces(MediaType.TEXT_PLAIN + ";charset=\"UTF-8\"")
	public String getCompetitionList(@PathParam("sportId") int sportId,
	                                 @PathParam("clientName") String clientName,
	                                 @PathParam("password") String password,
	                                 @Context HttpServletResponse response) throws IOException {
		Integer clientId = RestClientUtil.getValidatedClientId(clientName, password);
		return new YamlOutputCreator(clientId, response).createOutput(new CompetitionListData(
		        sportId, clientId));
	}
}
