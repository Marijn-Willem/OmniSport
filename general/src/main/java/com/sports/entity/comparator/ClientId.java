package com.sports.entity.comparator;

import com.sports.entity.Client;

public class ClientId extends SuperComparator<Client> {
    public int compare(Client o1, Client o2) {
        return o1.getId() - o2.getId();
    }
}
