package com.sports.cache.data;

import com.sports.cache.key.CacheDataKey;
import com.sports.cache.key.ClientListKey;
import com.sports.cache.util.DataFragmentUtil;
import com.sports.cache.util.JsonUtil;
import com.sports.cache.util.XmlUtil;
import com.sports.cache.util.YamlUtil;
import com.sports.entity.Client;
import com.sports.entity.comparator.NamedEntityName;
import com.sports.entity.manager.ClientManager;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class ClientListData extends OutputData {
    private final Map<String, ClientFragment> clientMap = new HashMap<>();
    private final List<ClientFragment> clientsSorted = new ArrayList<>();
    private final Set<Integer> adminIds = new HashSet<>();

    @Override
    public CacheDataKey getCacheKey() {
        return new ClientListKey();
    }

    @Override
    public void fill(Statement stat) throws SQLException {
        List<Client> clientList = new ArrayList<>(new ClientManager(stat).getClientList());
        clientList.sort(new NamedEntityName());

        clientList.forEach(x -> clientsSorted.add(
                new ClientFragment(x, DataFragmentUtil.getLevelForNestedList(nestingLevel), true)));
        DataFragmentUtil.fillDataFragments(clientsSorted, getCacheKey());

        clientsSorted.forEach(x -> {
            clientMap.put(x.getName(), x);
            if (x.isAdmin())
                adminIds.add(x.getId());
        });
    }

    @Override
    public boolean isValidOutput() {
        return !clientMap.isEmpty();
    }

    @Override
    public String toXML() {
        return XmlUtil.getTopLevelXmlList("clientList", "client", clientsSorted);
    }

    @Override
    public String toJson() {
        return "{" + JsonUtil.getArray("clientList", clientsSorted) + "}";
    }

    @Override
    public String toYaml() {
        return new YamlUtil(nestingLevel).getArray("clientList", clientsSorted);
    }

    public ClientFragment getValidatedClient(String name, String passWord) {
        ClientFragment clientFragment = clientMap.get(name);
        if (clientFragment != null && !passWord.equals(clientFragment.getPassWord()))
            clientFragment = null;

        return clientFragment;
    }

    public boolean isAdmin(int clientId) {
        return adminIds.contains(clientId);
    }
}
