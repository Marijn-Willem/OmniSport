package com.sports.rest.json;

import com.sports.cache.data.DivisionStandingData;
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

@Path("json/divisionstanding/{competitionId}/{seasonId}/{compSeasonPhaseId}/{compDivisionId}/{clientName}/{password}")
public class DivisionStanding {
    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=\"UTF-8\"")
    public String getDivisionStanding(@PathParam("competitionId") int competitionId,
                                      @PathParam("seasonId") int seasonId,
                                      @PathParam("compSeasonPhaseId") int compSeasonPhaseId,
                                      @PathParam("compDivisionId") int compDivisionId,
                                      @PathParam("clientName") String clientName,
                                      @PathParam("password") String password,
                                      @Context HttpServletResponse response) throws IOException {
        Integer clientId = RestClientUtil.getValidatedClientId(clientName, password);

        return new JsonOutputCreator(clientId, response, new CompSeasonKey(competitionId, seasonId))
                .createOutput(
                        new DivisionStandingData(competitionId, seasonId, compSeasonPhaseId, compDivisionId, clientId)
                );
    }
}
