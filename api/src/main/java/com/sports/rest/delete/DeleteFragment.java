package com.sports.rest.delete;

import com.sports.cache.util.CacheUtil;
import com.sports.rest.RestClientUtil;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;

@Path("delete/fragment/{cacheKey}/{clientName}/{password}")
public class DeleteFragment {
    @DELETE
    public void deleteFromCache(@PathParam("cacheKey") String cacheKey,
                                @PathParam("clientName") String clientName,
                                @PathParam("password") String password) {
        Integer clientId = RestClientUtil.getValidatedClientId(clientName, password);

        if (clientId != null && RestClientUtil.clientIsAdmin(clientId))
            CacheUtil.deleteFragment(cacheKey);
    }
}
