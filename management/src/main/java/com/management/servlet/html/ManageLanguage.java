package com.management.servlet.html;

import com.sports.entity.Language;
import com.sports.entity.manager.LanguageManager;
import com.sports.logic.calculation.DbCalculation;
import com.sportservlet.html.ManageEntity;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ManageLanguage extends ManageEntity {
    @Override
    public String getBasicReturnPath(Statement stat, HttpServletRequest req) {
        return "LanguagePortal";
    }

    @Override
    protected void initSpecific(Statement stat, HttpServletRequest req) {}

    @Override
    public void initSpecificProperties(HttpServletRequest req) {
        cssList.add("styling");
    }

    @Override
    protected void initAbstractProperties(HttpServletRequest req) {
        jsSpecificList.add("language");
    }

    @Override
    protected String getEntityIdName() {
        return "id";
    }

    @Override
    protected void processSpecific(Statement stat, HttpServletRequest req, HttpServletResponse res)
            throws IOException, SQLException {
        Language language = null;
        LinkedHashMap<Integer, String> languageMap = getLanguageMap(stat);

        if (!"i".equals(mode)) {
            int id = getIntValuedParameterValue(req, "id");
            language = new LanguageManager(stat).getEntityFromId(id);
            processLanguageMap(stat, languageMap, id);
        }

        Writer w = res.getWriter();

        writeTextFieldWithLabel("Name", "nm", language != null ? language.getName() : null, w);
        writeSelectWithLabel("Fallback language", "fbid", languageMap, language != null ? language.getFallbackLanguageId() : null, w);
    }

    private void processLanguageMap(Statement stat, Map<Integer, String> languageMap, int id) throws SQLException {
        languageMap.remove(id);

        List<Language> referencingLanguages = new DbCalculation(stat).getReferencingLanguages(id);
        for (Language language : referencingLanguages)
            languageMap.remove(language.getId());
    }
}
