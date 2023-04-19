package com.sports.rest.json;

import com.sports.cache.data.SportListData;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import static com.sports.rest.RestClientUtil.getValidatedClientId;

@Path("json/sportlist/{clientName}/{password}")
public class SportList {
    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=\"UTF-8\"")
    public String getSportList(@PathParam("clientName") String clientName,
                               @PathParam("password") String password) {
        Integer clientId = getValidatedClientId(clientName, password);

        return new JsonOutputCreator(clientId).createOutput(new SportListData(clientId));
    }
}
