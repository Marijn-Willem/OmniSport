package com.sportservlet.ajax;

import com.sports.entity.Equipe;
import com.sports.entity.comparator.NamedEntityName;
import com.sports.entity.manager.EquipeManager;
import com.sportservlet.SuperResponseServlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class EquipeListByName extends SuperResponseServlet {
    @Override
    protected void processBody(Statement stat, HttpServletRequest req, HttpServletResponse resp)
            throws IOException, SQLException {
        String nm = req.getParameter("nm");

        List<Equipe> equipes = new EquipeManager(stat).getEquipeListNameLike(nm);
        equipes.sort(new NamedEntityName());

        Writer w = resp.getWriter();

        for (Equipe equipe : equipes) {
            w.append("<tr onclick=\"handleClickEquipeName(this);\"><td>");
            w.append(equipe.getName());
            w.append("</td></tr>\n");
        }
    }
}
