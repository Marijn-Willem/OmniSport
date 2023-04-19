package com.sportservlet.html;

import com.sports.entity.Gender;
import com.sports.entity.Person;
import com.sports.entity.PersonSport;
import com.sports.entity.Sport;
import com.sports.entity.manager.PersonManager;
import com.sports.entity.manager.PersonSportManager;
import com.sports.entity.manager.SportManager;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.List;

public abstract class ManagePerson extends ManageEntity {
    private Person person;

    protected void initAbstractProperties(HttpServletRequest req) {
        jsList.add("person");
    }

    @Override
    protected String getEntityIdName() {
        return null;
    }

    @Override
    protected String getUpdateId(HttpServletRequest req) {
        return person != null ? Integer.toString(person.getId()) : "";
    }

    @Override
    protected void preProcessSpecific(Statement stat, HttpServletRequest req, HttpServletResponse res) throws SQLException {
        String name = req.getParameter("nm");
        person = new PersonManager(stat).getPersonByName(name);
    }

    protected void processSpecific(Statement stat, HttpServletRequest req, HttpServletResponse res)
            throws IOException, SQLException {
        if (person != null) {
            LinkedHashMap<Integer, String> genderSelMap = new LinkedHashMap<>();
            genderSelMap.put(Gender.genderIdMale, Gender.getGenderNameFromId(Gender.genderIdMale));
            genderSelMap.put(Gender.genderIdFemale, Gender.getGenderNameFromId(Gender.genderIdFemale));
            genderSelMap.put(Gender.genderIdMixed, Gender.getGenderNameFromId(Gender.genderIdMixed));

            Writer w = res.getWriter();

            writeTextFieldWithLabel("Name", "nm", person.getName(), w);
            writeSelectWithLabel("Geo", "geid", getGeoMapWithCountries(stat), person.getGeoId(), w);
            writeSelectWithLabel("Gender", "gid", genderSelMap, person.getGenderId(), w);

            writePersonSports(stat, w);
        }
        else
            dispatchToReturnPath(req, res);
    }

    private void writePersonSports(Statement stat, Writer w) throws IOException, SQLException {
        List<PersonSport> personSports = new PersonSportManager(stat).getPersonSportsForPerson(person.getId());

        if (personSports.size() > 0) {
            List<Sport> sportList = new SportManager(stat).getFullSportList();

            setSportNames(personSports, sportList);

            w.append("<h3>Sport Info</h3>\n");
            for (PersonSport personSport : personSports)
                writePersonSport(personSport, w);
        }
    }

    private void setSportNames(List<PersonSport> personSports, List<Sport> sports) {
        for (PersonSport personSport : personSports)
            for (Sport sport : sports)
                if (sport.getId() == personSport.getSportId()) {
                    personSport.setSportName(sport.getName());
                    break;
                }
    }

    private void writePersonSport(PersonSport personSport, Writer w) throws IOException {
        writeSpanWithLabel("Sport", personSport.getSportName(), w);
        writeSpanWithLabel("Elo", Integer.toString(personSport.getElo()), w);
        w.append("<br/>\n");
    }
}
