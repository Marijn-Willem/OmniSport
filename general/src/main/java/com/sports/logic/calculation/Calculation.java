package com.sports.logic.calculation;

import com.sports.entity.Double;
import com.sports.entity.*;
import com.sports.entity.comparator.ParticipantStanding;
import com.sports.entity.comparator.SuperComparator;
import com.sports.entity.key.*;
import com.sports.logic.factory.*;
import com.sports.logic.util.Util;

import java.util.*;

public class Calculation {
    public static CompSeasonParticipantFactory<? extends CompSeasonParticipantKey,
            ? extends CompSeasonPhaseParticipantKey,
            ? extends Participant,
            ? extends SuperKeyEntity,
            ? extends H2HMatchKey,
            ? extends H2HMatch,
            ? extends H2HMatchPartKey,
            ? extends H2HMatchPart,
            ? extends H2HMatchPartStatKey,
            ? extends H2HMatchPartStat> getCompSeasonParticipantFactory(Competition competition, Sport sport) {
        if (competition.isH2hDouble())
            return new CompSeasonDoubleFactory();

        return sport.isTeam() ? new CompSeasonTeamFactory() : new CompSeasonPersonSportFactory();
    }

    public static EntityInstanceFactory<? extends EntityInstanceKey,
            ? extends EntityInstance> getEntityInstanceFactory(String entityName) {
        EntityInstanceFactory<? extends EntityInstanceKey, ? extends EntityInstance> factory = null;

        switch (entityName) {
            case "Club": factory = new ClubInstanceFactory(); break;
            case "Geo": factory = new GeoInstanceFactory(); break;
            case "Noc": factory = new NocInstanceFactory(); break;
            case "Person": factory = new PersonInstanceFactory(); break;
            case "Equipe": factory = new EquipeInstanceFactory();
            default:
        }

        assert factory != null;

        return factory;
    }

    public static String getWhereClauseTeamInMatches(CompSeasonKey csk, int teamId, List<TeamMatch> teamMatchList) {
        String[] clauseParts = new String[teamMatchList.size()];

        for (int i = 0; i < teamMatchList.size(); i++) {
            TeamMatch teamMatch = teamMatchList.get(i);

            clauseParts[i] = "(" + csk.getWhereClause() + " AND teammatchid = " + teamMatch.getTeamMatchId() +
                    " AND teamid = " + teamId + ")";
        }

        return Util.concatStrings(clauseParts, " OR ");
    }

    public static void setDoubleDescriptions(Collection<Double> doubles, Map<Integer, PersonSport> personSportMap) {
        for (Double dbl : doubles) {
            String[] descriptions = new String[2];

            descriptions[0] = personSportMap.get(dbl.getPersonSport1Id()).getDescription();
            descriptions[1] = personSportMap.get(dbl.getPersonSport2Id()).getDescription();

            dbl.setDescription(Util.concatStrings(descriptions, " / "));
        }
    }

    public static void setPersonSportIdsOnDouble(Double dbl, int personSport1Id, int personSport2Id,
                                                 Map<Integer, PersonSport> personSportMap) {
        DoublePersonSport1PersonSport2Key newKey = getDoubleKey(personSport1Id, personSport2Id, personSportMap);
        dbl.setPersonSport1Id(newKey.getPersonSport1Id());
        dbl.setPersonSport2Id(newKey.getPersonSport2Id());
    }

    public static <T extends Participant> void sortParticipantsAndSetRankBasedFields(List<T> participants) {
        sortParticipantsAndSetRankBasedFields(participants, new ParticipantStanding<>());
    }

    public static <T extends Participant> void sortParticipantsAndSetRankBasedFields(List<T> participants,
                                                                                     SuperComparator<T> comparator) {
        participants.sort(comparator);

        int leaderWins = !participants.isEmpty() ? participants.get(0).getWins() : 0;
        int leaderLosses = !participants.isEmpty() ? participants.get(0).getLosses() : 0;

        for (int i = 0; i < participants.size(); i++) {
            Participant participant = participants.get(i);
            participant.setRank(i + 1);

            double gamesBehind = (leaderWins - participant.getWins() + participant.getLosses() - leaderLosses) / 2D;
            participant.setGamesBehind(gamesBehind);
        }
    }

    public static void setGenderIdsOnPersonSports(Collection<PersonSport> personSports, Map<Integer, Person> personMap) {
        personSports.forEach(x -> x.setGenderId(personMap.get(x.getPersonId()).getGenderId()));
    }

    public static double getAverage(int played, int wins, int draws) {
        if (played == 0)
            return 0.0;

        return ((double)wins + 0.5 * (double)draws) / (double)played;
    }

    static DoublePersonSport1PersonSport2Key getDoubleKey(int personSport1Id, int personSport2Id,
                                                          Map<Integer, PersonSport> personSportMap) {
        PersonSport ps1 = personSportMap.get(personSport1Id);
        PersonSport ps2 = personSportMap.get(personSport2Id);

        int ps1Id, ps2Id;

        if (ps1.getGenderId() != ps2.getGenderId()) {
            ps1Id = ps1.getGenderId() == Gender.genderIdFemale ? ps1.getId() : ps2.getId();
            ps2Id = ps1.getGenderId() == Gender.genderIdFemale ? ps2.getId() : ps1.getId();
        } else {
            boolean p1First = ps1.getDescription().compareTo(ps2.getDescription()) < 0;

            ps1Id = p1First ? personSport1Id : personSport2Id;
            ps2Id = p1First ? personSport2Id : personSport1Id;
        }

        return new DoublePersonSport1PersonSport2Key(ps1Id, ps2Id);
    }
}
