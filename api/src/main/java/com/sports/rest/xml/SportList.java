package com.sports.rest.xml;

import com.sports.cache.data.SportListData;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;

import java.io.IOException;

import static com.sports.rest.RestClientUtil.getValidatedClientId;

@Path("xml/sportlist/{clientName}/{password}")
public class SportList {
    @GET
    @Produces(MediaType.TEXT_XML)
    public String getSportList(@PathParam("clientName") String clientName,
                               @PathParam("password") String password,
                               @Context HttpServletResponse response) throws IOException {
        Integer clientId = getValidatedClientId(clientName, password);

        return new XmlOutputCreator(clientId, response).createOutput(new SportListData(clientId));
    }
}
