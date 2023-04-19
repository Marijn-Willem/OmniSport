package com.sports.rest.xml;

import com.sports.cache.data.DartsMatchData;
import com.sports.entity.key.CompSeasonKey;
import com.sports.rest.RestClientUtil;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("xml/dartsmatch/{competitionId}/{seasonId}/{personMatchId}/{clientName}/{password}")
public class DartsMatch {
    @GET
    @Produces(MediaType.TEXT_XML)
    public String getDartsMatch(@PathParam("competitionId") int competitionId, @PathParam("seasonId") int seasonId,
                                @PathParam("personMatchId") int personMatchId,
                                @PathParam("clientName") String clientName,
                                @PathParam("password") String password) {
        CompSeasonKey compSeasonKey = new CompSeasonKey(competitionId, seasonId);
        Integer clientId = RestClientUtil.getValidatedClientId(clientName, password);

        return new XmlOutputCreator(clientId, compSeasonKey).createOutput(
                new DartsMatchData(competitionId, seasonId, personMatchId, clientId));
    }
}
