package com.management.servlet.ajax;

import com.sports.entity.Language;
import com.sports.entity.comparator.NamedEntityName;
import com.sports.entity.manager.LanguageManager;
import com.sportservlet.SuperResponseServlet;
import com.sportservlet.util.ServletUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class LanguageList extends SuperResponseServlet {
    @Override
    protected void processBody(Statement stat, HttpServletRequest req, HttpServletResponse resp)
            throws IOException, SQLException {
        List<Language> languages = new LanguageManager(stat).getLanguageList();
        languages.sort(new NamedEntityName());

        for (Language language : languages)
            ServletUtil.writeOption(language.getId(), language.getName(), resp.getWriter());
    }
}
