package com.sports.rest;

import com.sports.cache.data.ClientCompSeasonData;
import com.sports.cache.data.ClientFragment;
import com.sports.cache.data.ClientListData;
import com.sports.cache.util.DataFragmentUtil;
import com.sports.entity.Client;
import com.sports.entity.key.CompSeasonKey;

public class RestClientUtil {
    static boolean clientHasCompSeasonPermit(int clientId, CompSeasonKey compSeasonKey) {
        return clientIsAdmin(clientId) ||
                DataFragmentUtil.getFilledOutputData(new ClientCompSeasonData(clientId), null)
                        .hasCompSeason(compSeasonKey);
    }

    public static boolean clientIsAdmin(int clientId) {
        return DataFragmentUtil.getFilledOutputData(new ClientListData(), null).isAdmin(clientId);
    }

    public static Integer getValidatedClientId(String clientName, String passWord) {
        Client client = getValidatedClient(clientName, passWord);

        return client != null ? client.getId() : null;
    }

    public static Client getValidatedClient(String clientName, String passWord) {
        Client client = null;
        ClientFragment clientFragment = DataFragmentUtil.getFilledOutputData(new ClientListData(), null)
                .getValidatedClient(clientName, passWord);

        if (clientFragment != null) {
            client = new Client();
            client.setId(clientFragment.getId());
            client.setName(clientFragment.getName());
            client.setPassWord(clientFragment.getPassWord());
            client.setAdmin(clientFragment.isAdmin());
        }

        return client;
    }
}
