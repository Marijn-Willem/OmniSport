package com.sports.cache.data;

import com.sports.cache.key.CacheDataKey;
import com.sports.cache.key.ClientCompSeasonKey;
import com.sports.cache.util.DataFragmentUtil;
import com.sports.entity.key.CompSeasonKey;
import com.sports.entity.manager.ClientCompSeasonManager;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ClientCompSeasonData extends OutputData {
    private final int clientId;

    private final Set<CompSeasonFragment> compSeasonFragments = new HashSet<>();

    public ClientCompSeasonData(int clientId) {
        this.clientId = clientId;
    }

    @Override
    public CacheDataKey getCacheKey() {
        return new ClientCompSeasonKey(clientId);
    }

    @Override
    public void fill(Statement stat) throws SQLException {
        List<CompSeasonFragment> fragmentList = new ArrayList<>(
                new ClientCompSeasonManager(stat).getCompSeasonKeys(clientId))
                .stream().map(CompSeasonFragment::new)
                .collect(Collectors.toList());

        DataFragmentUtil.fillDataFragments(fragmentList, getCacheKey());

        compSeasonFragments.addAll(fragmentList);
    }

    @Override
    public boolean isValidOutput() {
        return !compSeasonFragments.isEmpty();
    }

    @Override
    public String toXML() {
        return null;
    }

    @Override
    public String toJson() {
        return null;
    }

    public Set<CompSeasonFragment> getCompSeasonFragments() {
        return compSeasonFragments;
    }

    public boolean hasCompSeason(CompSeasonKey compSeasonKey) {
        return compSeasonFragments.contains(new CompSeasonFragment(compSeasonKey));
    }
}
