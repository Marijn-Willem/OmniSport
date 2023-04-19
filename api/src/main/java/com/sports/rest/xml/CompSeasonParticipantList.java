package com.sports.rest.xml;

import com.sports.cache.data.CompSeasonParticipantListData;
import com.sports.entity.key.CompSeasonKey;
import com.sports.rest.RestClientUtil;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("xml/compseasonparticipantlist/{competitionId}/{seasonId}/{clientName}/{password}")
public class CompSeasonParticipantList {
    @GET
    @Produces(MediaType.TEXT_XML)
    public String getCompSeasonParticipantList(@PathParam("competitionId") int competitionId,
                                               @PathParam("seasonId") int seasonId,
                                               @PathParam("clientName") String clientName,
                                               @PathParam("password") String password) {
        Integer clientId = RestClientUtil.getValidatedClientId(clientName, password);

        return new XmlOutputCreator(clientId, new CompSeasonKey(competitionId, seasonId))
                .createOutput(new CompSeasonParticipantListData(competitionId, seasonId, clientId));
    }
}
