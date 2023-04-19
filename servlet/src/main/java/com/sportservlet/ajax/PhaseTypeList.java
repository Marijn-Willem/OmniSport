package com.sportservlet.ajax;

import com.sports.entity.PhaseType;
import com.sports.entity.comparator.NamedEntityName;
import com.sports.entity.manager.PhaseTypeManager;
import com.sportservlet.SuperResponseServlet;
import com.sportservlet.util.ServletUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class PhaseTypeList extends SuperResponseServlet {
    @Override
    protected void processBody(Statement stat, HttpServletRequest req, HttpServletResponse resp)
            throws IOException, SQLException {
        List<PhaseType> phaseTypeList = new PhaseTypeManager(stat).getPhaseTypeList();
        phaseTypeList.sort(new NamedEntityName());

        Writer w = resp.getWriter();

        for (PhaseType phaseType : phaseTypeList)
            ServletUtil.writeOption(phaseType.getId(), phaseType.getName(), w);
    }
}
