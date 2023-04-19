package com.sports.rest.xml;

import com.sports.cache.data.SpeedSkatingHeatData;
import com.sports.entity.key.CompSeasonKey;
import com.sports.rest.RestClientUtil;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("xml/speedskating/heat/{competitionId}/{seasonId}/{sportEventId}/{sportEventPartId}/{heat}/{clientName}/{password}")
public class SpeedSkatingHeat {
    @GET
    @Produces(MediaType.TEXT_XML)
    public String getSpeedSkatingHeat(@PathParam("competitionId") int competitionId,
                                      @PathParam("seasonId") int seasonId,
                                      @PathParam("sportEventId") int sportEventId,
                                      @PathParam("sportEventPartId") int sportEventPartId,
                                      @PathParam("heat") int heat,
                                      @PathParam("clientName") String clientName,
                                      @PathParam("password") String password) {
        CompSeasonKey compSeasonKey = new CompSeasonKey(competitionId, seasonId);

        Integer clientId = RestClientUtil.getValidatedClientId(clientName, password);

        return new XmlOutputCreator(clientId, compSeasonKey).createOutput(
                new SpeedSkatingHeatData(competitionId, seasonId, sportEventId, sportEventPartId, heat, clientId));
    }
}
