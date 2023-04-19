package com.sportservlet.ajax;

import com.sports.entity.Person;
import com.sports.entity.PersonSport;
import com.sports.entity.comparator.PersonSportPersonId;
import com.sports.entity.key.CompSeasonPersonSportKey;
import com.sports.entity.manager.CompSeasonPersonSportManager;
import com.sports.entity.manager.CompetitionManager;
import com.sports.logic.calculation.DbCalculation;
import com.sportservlet.SuperResponseServlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public abstract class ProcessPersonImport extends SuperResponseServlet {
    private static final Comparator<PersonSport> personSportComparator = new PersonSportPersonId();
    private static final PersonSport personSportSearchObject = new PersonSport();

    CompSeasonPersonSportManager cspsm;

    protected abstract List<Integer> getParticipantIds() throws SQLException;
    protected abstract void processPersonSport(int personSportId, int nameIndX) throws SQLException;
    protected abstract Map<String, Person> getPersonNameMap(Statement stat, List<String> names) throws SQLException;
    protected void initSpecific(Statement stat, HttpServletRequest req) throws SQLException {

    }

    @Override
    protected void processBody(Statement stat, HttpServletRequest req, HttpServletResponse resp)
            throws IOException, SQLException {
        Writer w = resp.getWriter();
        Object sessionObj = req.getSession().getAttribute("names");

        if (sessionObj != null) {
            String[] names = (String[]) sessionObj;

            cspsm = new CompSeasonPersonSportManager(stat);
            initSpecific(stat, req);
            List<Integer> existingIds = getParticipantIds();

            if (existingIds.size() == 0) {
                Map<String, Person> personNameMap = getPersonNameMap(stat, Arrays.asList(names));
                Set<Integer> existingPersonSportIds = new HashSet<>(cspsm.getParticipantIdsCompSeason(compSeasonKey));

                int sportId = new CompetitionManager(stat).getCompetition(competitionId).getSportId();
                List<PersonSport> personSports = new DbCalculation(stat).getPersonSportsWithNewInstances(sportId,
                        new ArrayList<>(personNameMap.values()));
                personSports.sort(personSportComparator);

                for (int i = 0; i < names.length; i++)
                    processPerson(names, i, personNameMap, personSports, existingPersonSportIds);

                req.getSession().removeAttribute("names");

                w.append("<div>Persons successfully added!</div>\n");

                List<Person> newPersons = new ArrayList<>();

                for (String name : names) {
                    Person p = personNameMap.get(name);
                    if (p.isNewlyCreated())
                        newPersons.add(p);
                }

                if (newPersons.size() > 0) {
                    w.append("<div>The following persons are newly created:</div>\n<br/>\n");

                    for (Person p : newPersons) {
                        String line = "<div>" + p.getName() + "</div>\n";
                        resp.getWriter().append(line);
                    }
                }
            }
            else
                resp.getWriter().append("Already Persons in this event!");
        }
        else
            resp.getWriter().append("No persons selected!");
    }

    private void processPerson(String[] names, int nameIndX, Map<String, Person> personNameMap,
                               List<PersonSport> personSports, Set<Integer> existingPersonSportIds) throws SQLException {
        Person person = personNameMap.get(names[nameIndX]);
        PersonSport personSport = getPersonSportFromPersonId(person.getId(), personSports);

        if (personSport != null) {
            if (!existingPersonSportIds.contains(personSport.getId())) {
                CompSeasonPersonSportKey cspsk = new CompSeasonPersonSportKey(compSeasonKey, personSport.getId());
                cspsm.insertCompSeasonPersonSport(cspsk);
            }

            processPersonSport(personSport.getId(), nameIndX);
        }
    }

    private PersonSport getPersonSportFromPersonId(int personId, List<PersonSport> personSports) {
        personSportSearchObject.setPersonId(personId);
        int indX = Collections.binarySearch(personSports, personSportSearchObject, personSportComparator);

        if (indX >= 0)
            return personSports.get(indX);

        return null;
    }
}
