package com.sports.entity.comparator;

import com.sports.entity.key.ClientCompSeasonKey;

public class ClientCompSeasonKeyClientId extends SuperComparator<ClientCompSeasonKey> {
    public int compare(ClientCompSeasonKey o1, ClientCompSeasonKey o2) {
        return o1.getClientId() - o2.getClientId();
    }
}
