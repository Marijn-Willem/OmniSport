package com.sports.rest.json;

import com.sports.cache.data.ClientListData;
import com.sports.rest.RestClientUtil;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("json/clientlist/{clientName}/{password}")
public class ClientList {
    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=\"UTF-8\"")
    public String getClientList(@PathParam("clientName") String clientName,
                                @PathParam("password") String password) {
        Integer clientId = RestClientUtil.getValidatedClientId(clientName, password);
        return new JsonOutputCreator(clientId).createOutputForAdmin(new ClientListData());
    }
}
