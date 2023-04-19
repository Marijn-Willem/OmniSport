package com.sports.rest.xml;

import com.sports.cache.data.EventDisciplinePartRankingData;
import com.sports.entity.key.CompSeasonKey;
import com.sports.rest.RestClientUtil;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("xml/eventdisciplinepartranking/{competitionId}/{seasonId}/{sportEventId}/{compSeasonEventPartId}/{eventDisciplinePartId}/{clientName}/{password}")
public class EventDisciplinePartRanking {
	@GET
	@Produces(MediaType.APPLICATION_XML)
	public String getEventDisciplinePartRanking(@PathParam("competitionId") int competitionId,
												@PathParam("seasonId") int seasonId,
												@PathParam("sportEventId") int sportEventId,
												@PathParam("compSeasonEventPartId") int compSeasonEventPartId,
												@PathParam("eventDisciplinePartId") int eventDisciplinePartId,
												@PathParam("clientName") String clientName,
												@PathParam("password") String password) {
		CompSeasonKey compSeasonKey = new CompSeasonKey(competitionId, seasonId);
		Integer clientId = RestClientUtil.getValidatedClientId(clientName, password);

		return new XmlOutputCreator(clientId, compSeasonKey).createOutput(
				new EventDisciplinePartRankingData(competitionId, seasonId, sportEventId, compSeasonEventPartId,
						eventDisciplinePartId, clientId));
	}
}
