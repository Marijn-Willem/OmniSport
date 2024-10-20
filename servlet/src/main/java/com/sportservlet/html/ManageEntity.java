package com.sportservlet.html;

import com.sports.db.type.Point;
import com.sports.entity.*;
import com.sports.entity.comparator.NamedEntityName;
import com.sports.entity.manager.GeoManager;
import com.sports.entity.manager.LanguageManager;
import com.sports.entity.manager.LocationRoleManager;
import com.sports.entity.manager.NocManager;
import com.sports.logic.util.Util;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.Writer;
import java.lang.Double;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public abstract class ManageEntity extends SuperHtmlServlet implements AbstractHtmlServlet {
    protected abstract void initAbstractProperties(HttpServletRequest req);

    protected abstract String getEntityIdName();

    protected abstract String getBasicReturnPath(Statement stat, HttpServletRequest req) throws SQLException;

    protected abstract void processSpecific(Statement stat, HttpServletRequest req, HttpServletResponse res)
            throws IOException, SQLException;

    protected abstract void initSpecific(Statement stat, HttpServletRequest req) throws SQLException;

    @Override
    protected String getReturnPath(Statement stat, HttpServletRequest req) throws SQLException {
        String returnPath = null;
        String basicReturnPath = getBasicReturnPath(stat, req);

        if (basicReturnPath != null) {
            returnPath = basicReturnPath;

            String entityIdName = getEntityIdName();
            String updateId = getUpdateId(req);

            if (!Util.isEmptyString(entityIdName) && !Util.isEmptyString(updateId)) {
                String delim = basicReturnPath.contains("?") ? "&" : "?";
                returnPath += delim + entityIdName + "=" + updateId;
            }
        }

        return returnPath;
    }

    protected String mode;

    @Override
    protected void init(Statement stat, HttpServletRequest req) throws SQLException {
        mode = getMode(req);
        initSpecific(stat, req);
    }

    @Override
    protected void initProperties(HttpServletRequest req) {
        jsList.add("general");
        jsList.add("entity");

        initAbstractProperties(req);
        initSpecificProperties(req);
    }

    @Override
    protected void processScriptTag(Statement stat, HttpServletRequest req, Writer w) throws IOException, SQLException {
        String line = "let md = '" + getMode(req) + "';\n";
        w.append(line);
        String entityIdName = getEntityIdName();
        line = "const entIdNm = " + (entityIdName != null ? "'" + entityIdName + "'" : "null") + ";\n";
        w.append(line);
    }

    protected String getProcessUrl() {
        return "getProcessUrl";
    }

    protected String getValidateFunction() {
        return "checkInput";
    }

    protected String getUpdateId(HttpServletRequest req) {
        return convertRequestParameterToString(req, getEntityIdName());
    }

    protected void preProcessSpecific(Statement stat, HttpServletRequest req, HttpServletResponse res)
            throws IOException, SQLException {

    }

    protected void processHtmlBody(Statement stat, HttpServletRequest req, HttpServletResponse res)
            throws IOException, SQLException {
        preProcessSpecific(stat, req, res);
        processSpecific(stat, req, res);

        Writer w = res.getWriter();
        if (!"r".equals(mode)) {
            String valueText = "u".equals(mode) ? "Update" : "Insert";
            writeProcessButton(valueText, w);
        }
        w.append("<div id=\"divUpd\"></div>\n");
        w.append("<input id=\"inpUpd\" name=\"inpUpd\" type=\"hidden\" value=\"");
        w.append(getUpdateId(req));
        w.append("\" /><br/>\n");
    }

    protected void writeSpanWithLabel(String label, String value, Writer w) throws IOException {
        w.append("<span>");
        w.append(label);
        w.append(": ");
        if (value != null)
            w.append(value);
        w.append("</span><br/>\n");
    }

    protected void writeTextFieldWithLabel(String label, String name, String value, Writer w) throws IOException {
        writeTextFieldWithOnKeypress(label, name, value, null, w);
    }

    protected void writeTextFieldWithLabel(String label, String name, String value, boolean disabled, Writer w) throws IOException {
        writeTextFieldWithOnKeypress(label, name, value, null, disabled, w);
    }

    protected void writeSelectWithLabel(String label, String name, LinkedHashMap<Integer, String> options,
                              Integer selOption, Writer w) throws IOException {
        writeSelectWithLabel(label, name, options, selOption, false, w);
    }

    protected void writeSelectWithLabel(String label, String name, LinkedHashMap<Integer, String> options,
                              Integer selOption, boolean isDisabled, Writer w) throws IOException {
        writeSelectWithLabel(label, name, options, selOption, isDisabled, null, w);
    }

    protected void writeSelectWithLabel(String label, String name, LinkedHashMap<Integer, String> options,
                              Integer selOption, boolean isDisabled, String onChange, Writer w) throws IOException {
        w.append("<span>");
        w.append(label);
        w.append(": <select name=\"");
        w.append(name);
        w.append("\"");
        if (onChange != null) {
            w.append(" onchange=\"");
            w.append(onChange);
            w.append(";\"");
        }
        if ("r".equals(mode) || isDisabled)
            w.append(" disabled=\"true\"");
        w.append(">\n");

        String intValSelOpt = getIntValueForScriptTag(selOption);

        for (Map.Entry<Integer, String> me : options.entrySet()) {
            String intValKey = getIntValueForScriptTag(me.getKey());

            w.append("<option value=\"");
            w.append(intValKey);
            w.append("\"");
            if (intValSelOpt.equals(intValKey))
                w.append(" selected");
            w.append(">");
            w.append(me.getValue());
            w.append("</option>\n");
        }

        w.append("</select></span><br/>\n");

    }

    protected void writeNumericTextField(String label, String name, Integer numVal, Writer w) throws IOException {
        writeNumericTextField(label, name, numVal, false, w);
    }

    protected void writeNumericTextField(String label, String name, Integer numVal, boolean isDisabled, Writer w)
            throws IOException {
        w.append("<span>");
        w.append(label);
        w.append(": ");
        w.append(getNumTextButton(name, false, isDisabled));
        w.append("<input type=\"text\" class=\"ntf\" name=\"");
        w.append(name);
        w.append("\" value=\"");
        w.append(getIntValueForTextField(numVal));
        w.append("\"");
        if (isDisabled)
            w.append(" disabled=\"true\"");
        w.append(" />");
        w.append(getNumTextButton(name, true, isDisabled));
        w.append("</span><br/>\n");
    }

    protected void writeDateTimeField(String label, String name, LocalDateTime dtTm, Writer w) throws IOException {
        writeDateTimeField(label, name, dtTm, false, w);
    }

    protected void writeDateTimeField(String label, String name, LocalDateTime dtTm, boolean disabled, Writer w) throws IOException {
        writeTextFieldWithLabel(label, name, getDatetimeString(dtTm), disabled, w);
    }

    protected void writeCheckbox(String label, String name, boolean value, Writer w) throws IOException {
        writeCheckbox(label, name, value, false, w);
    }

    protected void writeCheckbox(String label, String name, boolean value, boolean isDisabled, Writer w) throws IOException {
        writeCheckbox(label, name, value, isDisabled, null, w);
    }

    protected void writeCheckbox(String label, String name, boolean value, boolean isDisabled, String onClick, Writer w) throws IOException {
        w.append("<span>");
        w.append(label);
        w.append(": <input type=\"checkbox\" name=\"");
        w.append(name);
        w.append("\"");
        if (value)
            w.append(" checked");
        if ("r".equals(mode) || isDisabled)
            w.append(" disabled=\"true\"");
        if (onClick != null) {
            w.append(" onclick=\"");
            w.append(onClick);
            w.append(";\"");
        }
        w.append(" /></span><br/>\n");
    }

    protected void writeTextFieldWithOnKeypress(String label, String name, String value, String onKeyPress, Writer w)
            throws IOException {
        writeTextFieldWithOnKeypress(label, name, value, onKeyPress, "r".equals(mode), w);
    }

    protected void writeTextFieldWithOnKeypress(String label, String name, String value, String onKeyPress, boolean disabled,
                                      Writer w) throws IOException {
        w.append("<span>");
        w.append(label);
        w.append(": <input type=\"text\" name=\"");
        w.append(name);
        w.append("\" value=\"");
        if (value != null)
            w.append(value);
        w.append("\"");
        if (onKeyPress != null) {
            w.append(" onkeypress=\"");
            w.append(onKeyPress);
            w.append(";\"");
        }
        if (disabled)
            w.append(" disabled=\"true\"");
        w.append(" /></span><br/>\n");
    }

    protected void writePasswordField(String value, Writer w) throws IOException {
        w.append("<span>Password: <input type=\"password\" name=\"pwd\" value=\"");
        if (value != null)
            w.append(value);
        w.append("\" /></span><br/>\n");
    }

    protected void writeGeoSpatialField(String label, String name, Point point, Writer w) throws IOException {
        w.append("<span>");
        w.append(label);
        w.append(": <input type=\"text\" name=\"");
        w.append(name);
        w.append("_1\" value=\"");
        if (point != null)
            w.append(Double.toString(point.x()));
        w.append("\" /><input type=\"text\" name=\"");
        w.append(name);
        w.append("_2\" value=\"");
        if (point != null)
            w.append(Double.toString(point.y()));
        w.append("\" />");
        w.append("<input type=\"button\" onclick=\"showCoordinatesInGoogleMaps('");
        w.append(name);
        w.append("');\" value=\"View\" />");
        w.append("</span><br/>\n");
    }

    protected LinkedHashMap<Integer, String> getGeoMapWithCountries(Statement stat) throws SQLException {
        List<Geo> geoList = new GeoManager(stat).getAllGeosByType(GeoType.geoTypeIdCountry);

        return getLinkedHashMapFromNamedIntEntities(geoList, true);
    }

    protected LinkedHashMap<Integer, String> getLanguageMap(Statement stat) throws SQLException {
        List<Language> languageList = new LanguageManager(stat).getLanguageList();

        return getLinkedHashMapFromNamedIntEntities(languageList, true);
    }

    protected LinkedHashMap<Integer, String> getFullNocMap(Statement stat) throws SQLException {
        List<Noc> allNocs = new NocManager(stat).getAllNocs();

        return getLinkedHashMapFromNamedIntEntities(allNocs, true);
    }

    protected LinkedHashMap<Integer, String> getLocationRoleMap(Statement stat) throws SQLException {
        List<LocationRole> locationRoles = new LocationRoleManager(stat).getAllLocationRoles();

        return getLinkedHashMapFromNamedIntEntities(locationRoles, false);
    }

    protected LinkedHashMap<Integer, String> getLinkedHashMapFromNamedIntEntities(List<? extends NamedIntEntity> entities,
                                                                                  boolean isNullable) {
        return getLinkedHashMapFromNamedEntities(entities, isNullable, NamedIntEntity::getId);
    }

    protected <T extends NamedEntity> LinkedHashMap<Integer, String> getLinkedHashMapFromNamedEntities(List<T> entities,
                                                                                                       boolean isNullable,
                                                                                                       Function<T, Integer> func) {
        entities.sort(new NamedEntityName());

        LinkedHashMap<Integer, String> map = new LinkedHashMap<>();
        if (isNullable)
            map.put(0, "-");

        for (T entity : entities)
            map.put(func.apply(entity), entity.getName());

        return map;
    }

    protected String convertRequestParameterToString(HttpServletRequest req, String paramName) {
        return Util.convertNullStringToEmpty(req.getParameter(paramName));
    }

    protected String getIntValueForScriptTag(Integer i) {
        return Util.convertEmptyInteger(i, "0");
    }

    private String getIntValueForTextField(Integer i) {
        return Util.convertEmptyInteger(i, "-");
    }

    private String getDatetimeString(LocalDateTime ldt) {
        return Util.convertEmptyDateTimeToString(ldt, "-");
    }

    private String getNumTextButton(String name, boolean forward, boolean isDisabled) {
        String symbol = forward ? ">" : "<";

        return "<input type=\"button\" value=\"" + symbol + "\" onclick=\"changeNumericText('" + name +
                "', " + forward + ");\"" + (isDisabled ? " disabled=\"true\"" : "") + " />";
    }

    private String getMode(HttpServletRequest req) {
        String mdStr = req.getParameter("md");

        if ("i".equals(mdStr) || "r".equals(mdStr))
            return mdStr;

        return "u";
    }

    private void writeProcessButton(String valueText, Writer w) throws IOException {
        w.append("<input id=\"btnUpd\" type=\"button\" value=\"");
        w.append(valueText);
        w.append("\" onclick=\"update(");
        w.append(getValidateFunction());
        w.append(", ");
        w.append(getProcessUrl());
        w.append(");\" />\n");
    }
}
