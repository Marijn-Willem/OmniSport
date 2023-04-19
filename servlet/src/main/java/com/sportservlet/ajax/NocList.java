package com.sportservlet.ajax;

import com.sports.entity.Noc;
import com.sports.entity.comparator.NamedEntityName;
import com.sports.entity.manager.NocManager;
import com.sportservlet.SuperResponseServlet;
import com.sportservlet.util.ServletUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class NocList extends SuperResponseServlet {
    @Override
    protected void processBody(Statement stat, HttpServletRequest req, HttpServletResponse resp)
            throws IOException, SQLException {
        List<Noc> nocList = new NocManager(stat).getAllNocs();
        nocList.sort(new NamedEntityName());

        for (Noc noc : nocList)
            ServletUtil.writeOption(noc.getId(), noc.getName(), resp.getWriter());
    }
}
