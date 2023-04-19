package com.sportservlet.ajax;

import com.sports.entity.Person;
import com.sports.entity.manager.IntSuperManager;
import com.sports.entity.manager.PersonManager;

import jakarta.servlet.http.HttpServletRequest;
import java.sql.Statement;

public class ProcessManagePerson extends ProcessManageIntEntity<Person> {
    protected Person getNewEntity() {
        return new Person();
    }

    protected void processEntityFromRequest(Statement stat, HttpServletRequest req) {
        String name = req.getParameter("nm");
        Integer geoId = convertRequestParamToIdInteger(req, "geid");
        int genderId = Integer.parseInt(req.getParameter("gid"));

        entity.setName(name);
        entity.setGenderId(genderId);
        entity.setGeoId(geoId);
    }

    protected IntSuperManager<Person> getSuperManager(Statement stat) {
        return new PersonManager(stat);
    }

    protected Integer getIdFromRequest(HttpServletRequest req) {
        return convertRequestParamToIdInteger(req, "pid");
    }
}
