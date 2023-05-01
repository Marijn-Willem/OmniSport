package com.sports.rest.json;

import com.sports.cache.data.SpeedSkatingHeatData;
import com.sports.entity.key.CompSeasonKey;
import com.sports.rest.RestClientUtil;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("json/speedskating/heat/{competitionId}/{seasonId}/{compSeasonEventId}/{compSeasonEventPartId}/{heat}/{clientName}/{password}")
public class SpeedSkatingHeat {
    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=\"UTF-8\"")
    public String getSpeedSkatingHeat(@PathParam("competitionId") int competitionId,
                                      @PathParam("seasonId") int seasonId,
                                      @PathParam("compSeasonEventId") int compSeasonEventId,
                                      @PathParam("compSeasonEventPartId") int compSeasonEventPartId,
                                      @PathParam("heat") int heat,
                                      @PathParam("clientName") String clientName,
                                      @PathParam("password") String password) {
        CompSeasonKey compSeasonKey = new CompSeasonKey(competitionId, seasonId);

        Integer clientId = RestClientUtil.getValidatedClientId(clientName, password);

        return new JsonOutputCreator(clientId, compSeasonKey).createOutput(
                new SpeedSkatingHeatData(competitionId, seasonId, compSeasonEventId, compSeasonEventPartId, heat, clientId));
    }
}
