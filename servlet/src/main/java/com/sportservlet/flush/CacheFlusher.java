package com.sportservlet.flush;

import com.sports.cache.key.CacheKey;
import com.sports.db.execute.DatabaseExecutor;
import com.sports.entity.Client;
import com.sports.entity.key.ClientCompSeasonKey;
import com.sports.entity.key.CompSeasonKey;
import com.sports.entity.manager.ClientCompSeasonManager;
import com.sports.entity.manager.ClientManager;
import com.sports.logic.async.ThreadWorker;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public abstract class CacheFlusher extends DatabaseExecutor implements ThreadWorker {
    private List<Integer> clientIdsWithPermission;

    protected abstract List<CacheKey> getCacheKeys(Statement stat) throws SQLException;

    @Override
    public void doWork(Statement stat) throws SQLException {
        getCacheKeys(stat).forEach(CacheKey::delete);
    }

    @Override
    public void doWork() {
        execute();
    }

    List<CacheKey> replicateForClientsWithRights(ClientReplicator replicator, CompSeasonKey compSeasonKey, Statement stat)
            throws SQLException {
        return getClientIdsWithPermission(compSeasonKey, stat).stream().map(x -> replicator.getCacheKeyForClientCompSeason(
                new ClientCompSeasonKey(x, compSeasonKey.getCompetitionId(), compSeasonKey.getSeasonId())
        )).toList();
    }

    private List<Integer> getClientIdsWithPermission(CompSeasonKey compSeasonKey, Statement stat) throws SQLException {
        if (clientIdsWithPermission == null)
            clientIdsWithPermission = new ArrayList<>() {{
                addAll(new ClientCompSeasonManager(stat).getClientIdsForCompSeason(compSeasonKey));
                addAll(new ClientManager(stat).getAdminClients().stream().map(Client::getId).toList());
            }};

        return clientIdsWithPermission;
    }

    static abstract class ClientReplicator {
        abstract CacheKey getCacheKeyForClientCompSeason(ClientCompSeasonKey clientCompSeasonKey);
    }
}
