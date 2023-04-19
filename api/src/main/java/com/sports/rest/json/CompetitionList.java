package com.sports.rest.json;

import com.sports.cache.data.CompetitionListData;
import com.sports.rest.RestClientUtil;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("json/competitionlist/{sportId}/{clientName}/{password}")
public class CompetitionList {
    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=\"UTF-8\"")
    public String getCompetitionList(@PathParam("sportId") int sportId,
                                     @PathParam("clientName") String clientName,
                                     @PathParam("password") String password) {
        Integer clientId = RestClientUtil.getValidatedClientId(clientName, password);
        return new JsonOutputCreator(clientId).createOutput(new CompetitionListData(sportId, clientId));
    }
}
