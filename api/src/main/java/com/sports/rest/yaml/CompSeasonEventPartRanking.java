package com.sports.rest.yaml;

import com.sports.cache.data.CompSeasonEventPartRankingData;
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

@Path("yaml/compseasoneventpartranking/{competitionId}/{seasonId}/{compSeasonEventId}/{compSeasonEventPartId}/{clientName}/{password}")
public class CompSeasonEventPartRanking {
    @GET
    @Produces(MediaType.TEXT_PLAIN + ";charset=\"UTF-8\"")
    public String getCompSeasonEventPartRanking(@PathParam("competitionId") int competitionId,
                                                @PathParam("seasonId") int seasonId,
                                                @PathParam("compSeasonEventId") int compSeasonEventId,
                                                @PathParam("compSeasonEventPartId") int compSeasonEventPartId,
                                                @PathParam("clientName") String clientName,
                                                @PathParam("password") String password,
                                                @Context HttpServletResponse response) throws IOException {
        CompSeasonKey compSeasonKey = new CompSeasonKey(competitionId, seasonId);
        Integer clientId = RestClientUtil.getValidatedClientId(clientName, password);

        return new YamlOutputCreator(clientId, response, compSeasonKey).createOutput(
                new CompSeasonEventPartRankingData(competitionId, seasonId, compSeasonEventId, compSeasonEventPartId, clientId));
    }
}
