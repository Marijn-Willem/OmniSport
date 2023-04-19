package com.sports.rest.xml;

import com.sports.cache.data.CrocoCupData;
import com.sports.rest.RestClientUtil;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("xml/crococup/{competitionId}/{clientName}/{password}")
public class CrocoCup {
    @GET
    @Produces(MediaType.TEXT_XML)
    public String getCrocoCup(@PathParam("competitionId") int competitionId,
                              @PathParam("clientName") String clientName,
                              @PathParam("password") String password) {
        Integer clientId = RestClientUtil.getValidatedClientId(clientName, password);
        return new XmlOutputCreator(clientId).createOutputForAdmin(new CrocoCupData(competitionId, clientId));
    }
}
