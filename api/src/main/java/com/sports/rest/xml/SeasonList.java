package com.sports.rest.xml;

import com.sports.cache.data.SeasonListData;
import com.sports.rest.RestClientUtil;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("xml/seasonlist/{competitionId}/{clientName}/{password}")
public class SeasonList {
	@GET
	@Produces(MediaType.APPLICATION_XML
)	public String getSeasonList(@PathParam("competitionId") int competitionId,
	                            @PathParam("clientName") String clientName,
	                            @PathParam("password") String password) {
		Integer clientId = RestClientUtil.getValidatedClientId(clientName, password);

		return new XmlOutputCreator(clientId).createOutput(new SeasonListData(competitionId, clientId));
	}
}
