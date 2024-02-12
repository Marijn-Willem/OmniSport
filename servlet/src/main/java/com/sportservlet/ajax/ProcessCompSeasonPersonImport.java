package com.sportservlet.ajax;

import com.sports.entity.CompSeasonPhase;
import com.sports.entity.Person;
import com.sports.entity.PersonMatch;
import com.sports.entity.comparator.H2HMatchKnockoutOrder;
import com.sports.entity.key.CompSeasonPhaseKey;
import com.sports.entity.key.CompSeasonPhasePersonSportKey;
import com.sports.entity.key.PersonMatchKey;
import com.sports.entity.manager.CompSeasonPhaseManager;
import com.sports.entity.manager.CompSeasonPhasePersonSportManager;
import com.sports.entity.manager.PersonMatchManager;
import com.sports.logic.calculation.DbCalculation;
import jakarta.servlet.http.HttpServletRequest;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ProcessCompSeasonPersonImport extends ProcessPersonImport {
    private CompSeasonPhaseKey cspkRound1;

    private CompSeasonPhasePersonSportManager csppsm;
    private PersonMatchManager psm;

    private List<PersonMatch> matchesFirstRound;
    private int matchIndX;

    @Override
    protected void initSpecific(Statement stat, HttpServletRequest req) throws SQLException {
        cspkRound1 = null;
        csppsm = new CompSeasonPhasePersonSportManager(stat);

        CompSeasonPhaseManager phaseManager = new CompSeasonPhaseManager(stat);

        List<CompSeasonPhase> knockoutParents = phaseManager.getKnockoutCompSeasonPhases(compSeasonKey);

        if (knockoutParents.size() == 1) {
            CompSeasonPhase csp = phaseManager.getCompSeasonPhaseForRound(compSeasonKey, 1);

            if (csp != null) {
                cspkRound1 = csp.getCompSeasonPhaseKey();
                psm = new PersonMatchManager(stat);

                matchesFirstRound = psm.getPersonMatchesFromCompSeasonPhases(Collections.singletonList(cspkRound1));
                matchesFirstRound.sort(new H2HMatchKnockoutOrder());

                matchIndX = 0;
            }
        }
    }

    @Override
    protected List<Integer> getParticipantIds() throws SQLException {
        return cspsm.getParticipantIdsCompSeason(compSeasonKey);
    }

    @Override
    protected void processPersonSport(int personSportId, int nameIndX) throws SQLException {
        if (cspkRound1 != null) {
            csppsm.insert(new CompSeasonPhasePersonSportKey(cspkRound1, personSportId));

            if (matchIndX < matchesFirstRound.size()) {
                PersonMatch currentMatch = matchesFirstRound.get(matchIndX);

                if (currentMatch.getParticipant1Id() == null)
                    currentMatch.setParticipant1Id(personSportId);
                else {
                    currentMatch.setParticipant2Id(personSportId);
                    PersonMatchKey pmKey = new PersonMatchKey(compSeasonKey, currentMatch.getSpecificId());
                    psm.updatePersonMatch(pmKey, currentMatch);
                    matchIndX++;
                }
            }
        }
    }

    @Override
    protected Map<String, Person> getPersonNameMap(Statement stat, List<String> names) throws SQLException {
        return new DbCalculation(stat).getPersonNameMapWithNewPersons(names, compSeasonKey.getCompetitionId());
    }
}
