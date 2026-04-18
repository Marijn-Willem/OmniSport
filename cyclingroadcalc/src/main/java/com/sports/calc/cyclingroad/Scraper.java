package com.sports.calc.cyclingroad;

import com.sports.entity.*;
import com.sports.entity.key.*;
import com.sports.entity.manager.*;
import com.sports.logic.calculation.DbCalculation;
import com.sports.logic.util.Util;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Scraper {
    private static final Pattern patTable = Pattern.compile("<table.*?>(.*?)</table>");
    private static final Pattern patTableRow = Pattern.compile("<tr.*?</tr>");
    private static final Pattern patName = Pattern.compile("<a.*?><span.*?>(.*?)</span>\\s*(.*?)</a>");
    private static final Pattern patRank = Pattern.compile("<td>(\\d+)</td>");
    private static final Pattern patNoRes = Pattern.compile("<td>([a-zA-Z]{3})</td>");
    private static final Pattern patTime = Pattern.compile("<span\\sclass=\"hide\">\\s*([\\d:.]+)</span>");
    private static final Pattern patTimeTT = Pattern.compile("<td\\sclass=\"time\\sar\\s\"\\s>([\\d:.]+)");

    private static final List<String> infixes = new ArrayList<>() {{
        add("da");
        add("de");
        add("den");
        add("der");
        add("des");
        add("del");
        add("dos");
        add("do");
        add("du");
        add("la");
        add("le");
        add("van");
        add("von");
        add("vanden");
        add("delle");
    }};

    private final String htmlSource;
    private final CompSeasonEventPartKey compSeasonEventPartKey;
    private final Statement stat;
    private CompSeasonEvent compSeasonEvent;
    private CompSeasonEventPart compSeasonEventPart;
    private boolean isSucceeded;
    private final List<String> newPersonNames = new ArrayList<>();

    public Scraper(String htmlSource, CompSeasonEventPartKey compSeasonEventPartKey, Statement stat) {
        this.htmlSource = htmlSource;
        this.compSeasonEventPartKey = compSeasonEventPartKey;
        this.stat = stat;
    }

    public void scrape() throws SQLException {
        String inputTable = getInputTable();

        if (inputTable != null) {
            processInputTable(inputTable);

            CompSeasonEventKey compSeasonEventKey = compSeasonEventPartKey.getSuperKey();
            com.sports.calc.cyclingroad.DbCalculation dbCalculation = new com.sports.calc.cyclingroad.DbCalculation(stat);

            int sportEventId = getCompSeasonEvent().getSportEventKey().getSportEventId();

            if (sportEventId == SportEvent.sportEventIdCyclingRoadStage)
                dbCalculation.updateEventPersonSportsWithNoCountResult(compSeasonEventKey);
            else if (getCompSeasonEventPart().isFinal())
                dbCalculation.updateEventPersonSportsFromFinalEventPart(compSeasonEventKey);
        }
    }

    public boolean isSucceeded() {
        return isSucceeded;
    }

    public List<String> getNewPersonNames() {
        return newPersonNames;
    }

    private void processInputTable(String inputTable) throws SQLException {
        Map<String, Integer> ncrMap = new NoCountResultManager(stat).getNameIdMap();

        Map<String, EventPartPersonSport> entityMap = new HashMap<>();
        Map<String, String> aliasNameMap = getAliasNameMap(stat);
        boolean isTimeTrial = getCompSeasonEventPart().getSportDisciplineId() ==
                SportDiscipline.sportDisciplineIdCyclingRoadTimeTrial;
        processTableRows(inputTable, entityMap, aliasNameMap, ncrMap, isTimeTrial);

        DbCalculation dbCalculation = new DbCalculation(stat);

        Map<String, Person> personNameMap = dbCalculation.getPersonNameMapWithNewPersons(
                entityMap.keySet().stream().toList(), getCompSeasonEvent());
        List<PersonSport> personSports = dbCalculation.getPersonSportsWithNewInstances(Sport.sportIdCyclingRoad,
                personNameMap.values().stream().toList());

        Set<CompSeasonPersonSportKey> compSeasonPersonSportKeys = personSports.stream()
                .map(x -> new CompSeasonPersonSportKey(compSeasonEventPartKey.getSuperKey().getSuperKey(), x.getId()))
                .collect(Collectors.toSet());

        Map<EventPersonSportKey, EventPersonSport> eventPersonSportMap = new HashMap<>() {{
            personSports.forEach(x -> put(new EventPersonSportKey(compSeasonEventPartKey.getSuperKey(), x.getId()),
                    new EventPersonSport()));
        }};

        Map<EventPartPersonSportKey, EventPartPersonSport> eventPartPersonSportMap =
                getEventPartPersonSportMap(personSports, entityMap, personNameMap);

        new CompSeasonPersonSportManager(stat).insertNonExistingPersonSports(compSeasonPersonSportKeys);
        new EventPersonSportManager(stat).insertNonExistingEventPersonSports(eventPersonSportMap);
        new EventPartPersonSportManager(stat).insertEventPartPersonSportMap(eventPartPersonSportMap);

        personNameMap.values().forEach(x -> {
            if (x.isNewlyCreated())
                newPersonNames.add(x.getName());
        });

        isSucceeded = true;
    }

    private Map<EventPartPersonSportKey, EventPartPersonSport> getEventPartPersonSportMap(List<PersonSport> personSports,
                                                                                          Map<String, EventPartPersonSport> entityMap,
                                                                                          Map<String, Person> personNameMap) {
        Map<Integer, Integer> personIdPersonSportIdMap = new HashMap<>() {{
            personSports.forEach(x -> put(x.getPersonId(), x.getId()));
        }};

        return new HashMap<>() {{
            entityMap.forEach((k, v) -> {
                int personSportId = personIdPersonSportIdMap.get(personNameMap.get(k).getId());
                EventPartPersonSportKey eppsKey = new EventPartPersonSportKey(compSeasonEventPartKey, personSportId);
                put(eppsKey, v);
            });
        }};
    }

    private void processTableRows(String table, Map<String, EventPartPersonSport> entityMap,
                                  Map<String, String> aliasNameMap, Map<String, Integer> ncrMap,
                                  boolean isTimeTrial) {
        Matcher matRow = patTableRow.matcher(table);
        int millisLeader = 0;
        boolean isLeader = true;

        while (matRow.find()) {
            String row = matRow.group();
            Matcher matName = patName.matcher(row);
            Matcher matRank = patRank.matcher(row);
            Matcher matNoRes = patNoRes.matcher(row);
            Matcher matTime = isTimeTrial ? patTimeTT.matcher(row) : patTime.matcher(row);

            boolean isName = matName.find();
            String nameLast = isName ? matName.group(1) : null;
            String nameFirst = isName ? matName.group(2) : null;
            String rank = matRank.find() ? matRank.group(1) : null;
            String noRes = matNoRes.find() ? matNoRes.group(1) : null;
            String time = matTime.find() ? matTime.group(1) : null;

            int timeDiff = 0;

            if (time != null) {
                int timeInMillis = getMillisFromTimeString(time);

                if (isLeader)
                    millisLeader = timeInMillis;
                else
                    timeDiff = timeInMillis;
            }

            if (nameLast != null && nameFirst != null) {
                appendEntity(nameLast, nameFirst, rank, noRes, entityMap, aliasNameMap, millisLeader, timeDiff, ncrMap);
                isLeader = false;
            }
        }
    }

    private void appendEntity(String nameLast, String nameFirst, String rank, String noRes,
                              Map<String, EventPartPersonSport> entityMap, Map<String, String> aliasNameMap,
                              int millisLeader, int timeDiff, Map<String, Integer> ncrMap) {
        EventPartPersonSport eventPartPersonSport = new EventPartPersonSport();

        if (rank != null)
            eventPartPersonSport.setRank(Integer.valueOf(rank));

        if (noRes != null)
            eventPartPersonSport.setNoCountResultId(ncrMap.get(noRes));
        else
            eventPartPersonSport.setPoints(millisLeader + timeDiff);

        String formattedName = formatName(nameLast, nameFirst);
        String key = aliasNameMap.getOrDefault(formattedName, formattedName);

        entityMap.put(key, eventPartPersonSport);
    }

    private String formatName(String nameLast, String nameFirst) {
        String[] nameFirstSplit = nameFirst.split(" ");
        String[] nameLastSplit = nameLast.split(" ");

        StringBuilder sb = new StringBuilder();

        for (String namePart : nameFirstSplit) {
            sb.append(formatNamePart(namePart));
            sb.append(" ");
        }

        for (int i = 0; i < nameLastSplit.length; i++) {
            String namePart = nameLastSplit[i];

            if (!"".equals(namePart)) {
                sb.append(formatNamePart(namePart));
                if (i < nameLastSplit.length - 1)
                    sb.append(" ");
            }
        }

        return sb.toString();
    }

    private String formatNamePart(String namePart) {
        StringBuilder sb = new StringBuilder();

        String namePartLowerCase = namePart.toLowerCase();
        String firstLetter = Character.toString(namePart.charAt(0));

        if (infixes.contains(namePartLowerCase))
            sb.append(firstLetter.toLowerCase());
        else
            sb.append(firstLetter.toUpperCase());

        for (int i = 1; i < namePart.length(); i++) {
            String letter = Character.toString(namePart.charAt(i));
            if (namePart.charAt(i - 1) == '-')
                sb.append(letter.toUpperCase());
            else
                sb.append(letter.toLowerCase());
        }

        return sb.toString();
    }

    private Map<String, String> getAliasNameMap(Statement stat) throws SQLException {
        Map<String, Integer> nameIdMap = new HashMap<>() {{
            new AliasManager(stat).getAliasListClient(Client.clientIdProcyclingStats).forEach(x -> {
                if (x.getAliasEntityId() == AliasEntity.aliasEntityIdPersonInstance) {
                    int personId = Integer.parseInt(x.getEntityId().split("_")[0]);
                    put(x.getAlias(), personId);
                }
            });
        }};

        Map<Integer, Person> personMap = new PersonManager(stat).getPersonMap(nameIdMap.values().stream().toList());

        return new HashMap<>() {{
            nameIdMap.forEach((k, v) -> put(k, personMap.get(v).getName()));
        }};
    }

    private CompSeasonEvent getCompSeasonEvent() throws SQLException {
        if (compSeasonEvent == null)
            compSeasonEvent = new CompSeasonEventManager(stat).getEntityFromSuperKey(compSeasonEventPartKey.getSuperKey());

        return compSeasonEvent;
    }

    private CompSeasonEventPart getCompSeasonEventPart() throws SQLException {
        if (compSeasonEventPart == null)
            compSeasonEventPart = new CompSeasonEventPartManager(stat).getEntityFromSuperKey(compSeasonEventPartKey);

        return compSeasonEventPart;
    }

    private int getMillisFromTimeString(String timeString) {
        String hmsString = timeString.replace('.', ':');
        hmsString = hmsString.length() <= 5 ? "0:" + hmsString : hmsString;

        return Util.getMillisFromHMSString(hmsString);
    }

    private String getInputTable() throws SQLException {
        String result = null;

        Matcher mat = patTable.matcher(htmlSource);
        int tableNr = getTableNr();
        int currentTable = 0;

        while (currentTable < tableNr-1 && mat.find())
            currentTable++;

        if (currentTable == tableNr-1 && mat.find())
            result = mat.group(1);

        return result;
    }

    private int getTableNr() throws SQLException {
        int sportEventId = getCompSeasonEvent().getSportEventKey().getSportEventId();

        return sportEventId == SportEvent.sportEventIdCyclingRoadGeneral ? 2 : 1;
    }
}
