package com.sportservlet.ajax;

import com.sports.db.type.Point;
import com.sports.entity.Entity;
import com.sports.logic.util.Util;
import com.sportservlet.SuperResponseServlet;
import com.sportservlet.flush.CacheFlusher;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public abstract class ProcessManageEntity<T extends Entity> extends SuperResponseServlet {
    private final List<String> invalidParamIdIntValues = Arrays.asList("", "0", "-");
    private final List<String> invalidParamNonIdIntValues = Arrays.asList("", "-");

    protected abstract T getNewEntity();
    protected abstract void processEntityFromRequest(Statement stat, HttpServletRequest req) throws SQLException;
    abstract void processSpecific(Statement stat, HttpServletRequest req, HttpServletResponse res)
            throws IOException, SQLException;

    protected void initSpecific(Statement stat, HttpServletRequest req) throws SQLException {}
    protected void postMortemSpecific(Statement stat) throws SQLException {}
    protected CacheFlusher getCacheFlusher() { return null; }

    protected String mode;
    protected String updateIdStr = "";

    protected void processBody(Statement stat, HttpServletRequest req, HttpServletResponse resp) throws IOException, SQLException {
        mode = "i".equals(req.getParameter("md")) ? "i" : "u";

        initSpecific(stat, req);
        processSpecific(stat, req, resp);
        postMortemSpecific(stat);

        cacheFlusher = getCacheFlusher();
        resp.getWriter().append(updateIdStr);
    }

    protected Integer convertRequestParamToIdInteger(HttpServletRequest req, String param) {
        return convertRequestParamToInteger(req, param, invalidParamIdIntValues);
    }

    protected Integer convertRequestParamToNonIdInteger(HttpServletRequest req, String param) {
        return convertRequestParamToInteger(req, param, invalidParamNonIdIntValues);
    }

    protected LocalDateTime convertRequestParameterToDatetime(HttpServletRequest req, String param) {
        String reqVal = req.getParameter(param);

        return reqVal != null && !"-".equals(reqVal) ? Util.convertStringToDateTime(reqVal) : null;
    }

    protected Point getPointFromParameter(HttpServletRequest req, String param) {
        String[] strSplit = req.getParameter(param).split("\\|");

        return strSplit.length > 0 ? new Point(Double.parseDouble(strSplit[0]), Double.parseDouble(strSplit[1])) : null;
    }

    private Integer convertRequestParamToInteger(HttpServletRequest req, String param, List<String> invalidValues) {
        String reqVal = Util.convertNullStringToEmpty(req.getParameter(param));

        return !invalidValues.contains(reqVal) ? Integer.parseInt(reqVal) : null;
    }
}
