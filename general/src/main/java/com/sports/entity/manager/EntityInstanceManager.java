package com.sports.entity.manager;

import com.sports.db.util.QueryUtil;
import com.sports.entity.EntityInstance;
import com.sports.entity.key.EntityInstanceKey;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class EntityInstanceManager<S extends EntityInstanceKey, T extends EntityInstance>
    extends SuperKeyAliasableManager<S, T> {
    private final String entityIdAsString = getEntityName() + "id";
    private final String instanceIdAsString = getEntityName() + "instanceid";

    abstract String getEntityName();
    abstract S getKey(int entityId, int instanceId);

    public EntityInstanceManager(Statement stat) {
        super(stat);
    }

    public T getInstanceOnDateTime(int entityId, LocalDateTime dateTime) throws SQLException {
        String dateTimeAsString = dateTime != null ? QueryUtil.convertDateTimeToDbString(dateTime) :
                QueryUtil.getDbCurrentTime();
        String coalStartDate = "COALESCE(startdate, " + dateTimeAsString + ")";
        String coalEndDate = "COALESCE(enddate, " + dateTimeAsString + ")";

        String whereClause = entityIdAsString + " = " + entityId + " AND " +
                coalStartDate + " <= " + dateTimeAsString + " AND " +
                coalEndDate + " >= " + dateTimeAsString;

        return getEntity(whereClause);
    }

    public T getCurrentInstance(int entityId) throws SQLException {
        return getEntity(entityIdAsString + " = " + entityId + " AND enddate IS NULL");
    }

    public List<T> getInstancesForEntity(int entityId) throws SQLException {
        return getEntityList(entityIdAsString + " = " + entityId);
    }

    public void insertInitialInstance(int entityId, T entityInstance) throws SQLException {
        insert(getKey(entityId, 1), entityInstance);
    }

    public void insertInitialInstances(List<T> entityInstances, int startEntityId) throws SQLException {
        Map<S, T> insertMap = new HashMap<>() {{
            for (int i = 0; i < entityInstances.size(); i++)
                put(getKey(startEntityId + i, 1), entityInstances.get(i));
        }};

        insert(insertMap);
    }

    public void deleteInstances(int entityId) throws SQLException {
        List<S> keys = getInstancesForEntity(entityId).stream()
                .map(x -> getKey(entityId, x.getEntityInstanceId())).toList();

        deleteAliasables(keys);
    }

    @Override
    String getTableName() {
        return getEntityName() + "instance";
    }

    @Override
    String getKeyColumnString() {
        return entityIdAsString + ", " + instanceIdAsString;
    }

    @Override
    String[] getValueColumns() {
        return new String[] {
                "name",
                "startdate",
                "enddate"
        };
    }

    @Override
    S getSuperKeyFromResultSet(ResultSet rs) throws SQLException {
        return getKey(rs.getInt(entityIdAsString), rs.getInt(instanceIdAsString));
    }

    @Override
    T getInstanceFromResultSet(ResultSet rs) throws SQLException {
        T entityInstance = getInstance();

        entityInstance.setEntityId(rs.getInt(entityIdAsString));
        entityInstance.setEntityInstanceId(rs.getInt(instanceIdAsString));
        entityInstance.setName(rs.getString("name"));
        entityInstance.setStartDate(QueryUtil.convertTimestampToDateTime(rs.getTimestamp("startdate")));
        entityInstance.setEndDate(QueryUtil.convertTimestampToDateTime(rs.getTimestamp("enddate")));

        return entityInstance;
    }
}
