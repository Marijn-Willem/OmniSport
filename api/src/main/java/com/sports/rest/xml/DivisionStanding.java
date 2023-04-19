package com.sports.rest.xml;

import com.sports.cache.data.DivisionStandingData;
import com.sports.entity.key.CompSeasonKey;
import com.sports.rest.RestClientUtil;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("xml/divisionstanding/{competitionId}/{seasonId}/{compSeasonPhaseId}/{compDivisionId}/{clientName}/{password}")
public class DivisionStanding {
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public String getDivisionStanding(@PathParam("competitionId") int competitionId,
                                      @PathParam("seasonId") int seasonId,
                                      @PathParam("compSeasonPhaseId") int compSeasonPhaseId,
                                      @PathParam("compDivisionId") int compDivisionId,
                                      @PathParam("clientName") String clientName,
                                      @PathParam("password") String password) {
        Integer clientId = RestClientUtil.getValidatedClientId(clientName, password);

        return new XmlOutputCreator(clientId, new CompSeasonKey(competitionId, seasonId))
                .createOutput(
                        new DivisionStandingData(competitionId, seasonId, compSeasonPhaseId, compDivisionId, clientId)
                );
    }
}
