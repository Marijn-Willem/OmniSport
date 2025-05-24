package com.sports.rest.yaml;

import com.sports.cache.data.CrocoCupData;
import com.sports.rest.RestClientUtil;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;

import java.io.IOException;

@Path("yaml/crococup/{competitionId}/{clientName}/{password}")
public class CrocoCup {
	@GET
	@Produces(MediaType.TEXT_PLAIN + ";charset=\"UTF-8\"")
	public String getCrocoCup(@PathParam("competitionId") int competitionId,
	                          @PathParam("clientName") String clientName,
	                          @PathParam("password") String password,
	                          @Context HttpServletResponse response) throws IOException {
		Integer clientId = RestClientUtil.getValidatedClientId(clientName, password);
		return new YamlOutputCreator(clientId, response).createOutputForAdmin(
				new CrocoCupData(competitionId, clientId));
	}
}
