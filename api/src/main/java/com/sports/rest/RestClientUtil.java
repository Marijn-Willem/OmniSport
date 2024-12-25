package com.sports.rest;

import com.sports.cache.data.ClientCompSeasonData;
import com.sports.cache.data.ClientFragment;
import com.sports.cache.data.ClientListData;
import com.sports.cache.util.DataFragmentUtil;
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
        ClientFragment clientFragment = DataFragmentUtil.getFilledOutputData(new ClientListData(), null)
                .getValidatedClient(clientName, passWord);

        return clientFragment != null ? clientFragment.getId() : null;
    }
}
