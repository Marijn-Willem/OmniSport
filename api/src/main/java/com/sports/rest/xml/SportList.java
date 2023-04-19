package com.sports.rest.xml;

import com.sports.cache.data.SportListData;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import static com.sports.rest.RestClientUtil.getValidatedClientId;

@Path("xml/sportlist/{clientName}/{password}")
public class SportList {
    @GET
    @Produces(MediaType.TEXT_XML)
    public String getSportList(@PathParam("clientName") String clientName,
                               @PathParam("password") String password) {
        Integer clientId = getValidatedClientId(clientName, password);

        return new XmlOutputCreator(clientId).createOutput(new SportListData(clientId));
    }
}
