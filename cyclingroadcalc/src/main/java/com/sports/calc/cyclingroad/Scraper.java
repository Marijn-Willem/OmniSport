package com.sports.calc.cyclingroad;

import com.sports.entity.*;
import com.sports.entity.key.*;
import com.sports.entity.manager.*;
import com.sports.logic.async.ThreadUtil;
import com.sports.logic.async.ThreadWorker;
import com.sports.logic.calculation.DbCalculation;
import com.sports.logic.util.Util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Scraper {
    private static final Pattern patTableRow = Pattern.compile("<tr.*?</tr>");
    private static final Pattern patName = Pattern.compile("<a.*?>\\s*?(.*?)\\s*?</a>");
    private static final Pattern patRank = Pattern.compile("<td>(\\d+)</td>");
    private static final Pattern patNoRes = Pattern.compile("<td>([a-zA-Z]{3})</td>");
    private static final Pattern patTimeMain = Pattern.compile("<td class=\"time ar\"\\s*>\\s*([\\d:.]+)");
    private static final Pattern patTimeDiff = Pattern.compile("<div class=\"hide\">([\\d:]+)</div>");

    private static final int clientId = Client.clientIdProcyclingStats;

    private static final List<String> infixes = new ArrayList<>() {{
        add("de");
        add("den");
        add("der");
        add("des");
        add("del");
        add("dos");
        add("du");
        add("la");
        add("le");
        add("van");
        add("von");
    }};

    private final String url;
    private final CompSeasonEventPartKey compSeasonEventPartKey;
    private final Statement stat;
    private CompSeasonEvent compSeasonEvent;
    private boolean isSucceeded;
    private final List<String> newPersonNames = new ArrayList<>();

    public Scraper(String url, CompSeasonEventPartKey compSeasonEventPartKey, Statement stat) {
        this.url = url;
        this.compSeasonEventPartKey = compSeasonEventPartKey;
        this.stat = stat;
    }

    public void scrape() throws SQLException {
        UrlScraper scraper = new UrlScraper(getTableNr());
        ThreadUtil.executeAsync(Collections.singletonList(scraper), true, 1);
        String inputTable = scraper.getInputTable();

        if (inputTable != null) {
            processInputTable(inputTable);

            int sportEventId = getCompSeasonEvent().getSportEventKey().getSportEventId();

            if (sportEventId == SportEvent.sportEventIdCyclingRoadStage)
                new com.sports.calc.cyclingroad.DbCalculation(stat)
                        .updateEventPersonSportsWithNoCountResult(compSeasonEventPartKey.getSuperKey());
        }
    }

    public boolean isSucceeded() {
        return isSucceeded;
    }

    public List<String> getNewPersonNames() {
        return newPersonNames;
    }

    private int getTableNr() throws SQLException {
        int sportEventId = getCompSeasonEvent().getSportEventKey().getSportEventId();

        return sportEventId == SportEvent.sportEventIdCyclingRoadGeneral ? 2 : 1;
    }

    private void processInputTable(String inputTable) throws SQLException {
        Map<String, Integer> ncrMap = new NoCountResultManager(stat).getNameIdMap();

        Map<String, EventPartPersonSport> entityMap = new HashMap<>();
        Map<String, String> aliasNameMap = getAliasNameMap(stat);
        processTableRows(inputTable, entityMap, aliasNameMap, ncrMap);

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
                                  Map<String, String> aliasNameMap, Map<String, Integer> ncrMap) {
        Matcher matRow = patTableRow.matcher(table);
        int millisLeader = 0;
        boolean isLeader = true;

        while (matRow.find()) {
            String row = matRow.group();
            Matcher matName = patName.matcher(row);
            Matcher matRank = patRank.matcher(row);
            Matcher matNoRes = patNoRes.matcher(row);
            Matcher matTimeMain = patTimeMain.matcher(row);
            Matcher matTimeDiff = patTimeDiff.matcher(row);

            String name = matName.find() ? matName.group(1) : null;
            String rank = matRank.find() ? matRank.group(1) : null;
            String noRes = matNoRes.find() ? matNoRes.group(1) : null;
            String timeMain = matTimeMain.find() ? matTimeMain.group(1) : null;
            String timeDiff = null;

            if (!isLeader)
                timeDiff = matTimeDiff.find() ? matTimeDiff.group(1) : timeMain;

            if (isLeader && timeMain != null)
                millisLeader = getMillisFromTimeString(timeMain);

            if (name != null) {
                appendEntity(name, rank, noRes, timeDiff, entityMap, aliasNameMap, millisLeader, ncrMap);
                isLeader = false;
            }
        }
    }

    private void appendEntity(String name, String rank, String noRes, String timeDiff,
                              Map<String, EventPartPersonSport> entityMap, Map<String, String> aliasNameMap,
                              int millisLeader, Map<String, Integer> ncrMap) {
        EventPartPersonSport eventPartPersonSport = new EventPartPersonSport();

        if (rank != null)
            eventPartPersonSport.setRank(Integer.valueOf(rank));

        if (noRes != null)
            eventPartPersonSport.setNoCountResultId(ncrMap.get(noRes));
        else {
            int time = millisLeader;

            if (timeDiff != null)
                time += getMillisFromTimeString(timeDiff);

            eventPartPersonSport.setPoints(time);
        }

        String formattedName = formatName(name);
        String key = aliasNameMap.getOrDefault(formattedName, formattedName);

        entityMap.put(key, eventPartPersonSport);
    }

    private String formatName(String name) {
        String[] split = name.split(" ");

        int indX = 0;

        while (indX < split.length && isFullUpperCase(split[indX]))
            indX++;

        StringBuilder sb = new StringBuilder();

        for (int i = indX; i < split.length; i++) {
            sb.append(formatNamePart(split[i]));
            sb.append(" ");
        }

        for (int i = 0; i < indX; i++) {
            sb.append(formatNamePart(split[i]));
            if (i < indX - 1)
                sb.append(" ");
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

    private boolean isFullUpperCase(String namePart) {
        for (int i = 0; i < namePart.length(); i++) {
            char c = namePart.charAt(i);
            if (Character.toUpperCase(c) != c)
                return false;
        }

        return true;
    }

    private Map<String, String> getAliasNameMap(Statement stat) throws SQLException {
        Map<String, Integer> nameIdMap = new HashMap<>() {{
            new AliasManager(stat).getAliasListClient(clientId).forEach(x -> {
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

    private int getMillisFromTimeString(String timeString) {
        String hmsString = timeString.replace('.', ':');
        hmsString = hmsString.length() <= 5 ? "0:" + hmsString : hmsString;

        return Util.getMillisFromHMSString(hmsString);
    }

    private class UrlScraper implements ThreadWorker {
        private static final Pattern patTableOpen = Pattern.compile("<table.*>");
        private static final Pattern patTableClose = Pattern.compile("</table>");

        private final int tableNr;

        public UrlScraper(int tableNr) {
            this.tableNr = tableNr;
        }

        private String inputTable;

        @Override
        public void doWork() throws Exception {
            URLConnection conn = new URI(url).toURL().openConnection();
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));

            int tableCount = 0;
            StringBuilder currentTable = null;

            while (tableCount < tableNr) {
                String line = br.readLine();
                Matcher matOpen = patTableOpen.matcher(line);
                Matcher matClose = patTableClose.matcher(line);

                if (matOpen.find() && tableCount == tableNr - 1)
                    currentTable = new StringBuilder();
                else if (matClose.find())
                    tableCount++;

                if (currentTable != null)
                    currentTable.append(line);
            }

            if (tableCount == tableNr && currentTable != null)
                inputTable = currentTable.toString();
            else
                throw new RuntimeException("Table not fully read");
        }

        public String getInputTable() {
            return inputTable;
        }
    }
}
