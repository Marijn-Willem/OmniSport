package com.sports.entity.manager;

import com.sports.db.util.QueryUtil;
import com.sports.entity.Language;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LanguageManager extends IntSuperManager<Language> {
    public LanguageManager(Statement stat) {
        super(stat);
    }

    @Override
    String getTableName() {
        return "language";
    }

    @Override
    String[] getValueColumns() {
        return new String[] { "name", "fallbacklanguageid" };
    }

    @Override
    Language getInstanceFromResultSet(ResultSet rs) throws SQLException {
        Language language = new Language();

        language.setId(rs.getInt("id"));
        language.setName(rs.getString("name"));
        language.setFallbackLanguageId(QueryUtil.getIntegerFromResultSet(rs, "fallbacklanguageid"));

        return language;
    }

    public Map<Integer, Language> getLanguageMap() throws SQLException {
        Map<Integer, Language> languageMap = new HashMap<>();

        ResultSet rs = stat.executeQuery(getGenericQuery(null));

        while (rs.next())
            languageMap.put(rs.getInt("id"), getInstanceFromResultSet(rs));

        return languageMap;
    }

    public List<Language> getLanguageList() throws SQLException {
        return getEntityList(null);
    }

    public List<Language> getDirectFallbackLanguages(int languageId) throws SQLException {
        return getEntityList("fallbacklanguageid = " + languageId);
    }
}
