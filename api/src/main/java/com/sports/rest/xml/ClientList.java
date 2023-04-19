package com.sports.rest.xml;

import com.sports.cache.data.ClientListData;
import com.sports.rest.RestClientUtil;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("xml/clientlist/{clientName}/{password}")
public class ClientList {
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public String getClientList(@PathParam("clientName") String clientName,
                                @PathParam("password") String password) {
        Integer clientId = RestClientUtil.getValidatedClientId(clientName, password);
        return new XmlOutputCreator(clientId).createOutputForAdmin(new ClientListData());
    }
}
