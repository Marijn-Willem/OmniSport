package com.sports.calc.darts.stat;

import com.sports.logic.util.Util;

public class StatObject {
    private int p1Score;
    private int p2Score;
    private int p1Legs;
    private int p2Legs;
    private int p1ThrowTot;
    private int p2ThrowTot;
    private int p1ThrowDartTot;
    private int p2ThrowDartTot;
    private int p1MisDub;
    private int p2MisDub;
    private int p1Throw180;
    private int p2Throw180;
    private int p1Throw140;
    private int p2Throw140;
    private int p1Throw100;
    private int p2Throw100;

    public String getStatsTableRows() {
        return "<tr><td>" + p1Score + "</td><td>Score</td><td>" + p2Score + "</td></tr>\n" +
                "<tr><td>" + getFormattedAverage(p1ThrowTot, p1ThrowDartTot) +
                "</td><td>Average</td><td>" + getFormattedAverage(p2ThrowTot, p2ThrowDartTot) +
                "</td></tr>\n" +
                "<tr><td>" + getCheckOutString(p1Legs, p1MisDub) +
                "</td><td>Checkout Avg</td><td>" + getCheckOutString(p2Legs, p2MisDub) +
                "</td></tr>\n" +
                "<tr><td>" + p1Throw180 + "</td><td>180</td><td>" + p2Throw180 + "</td></tr>\n" +
                "<tr><td>" + p1Throw140 + "</td><td>140+</td><td>" + p2Throw140 + "</td></tr>\n" +
                "<tr><td>" + p1Throw100 + "</td><td>100+</td><td>" + p2Throw100 + "</td></tr>\n";
    }

    public String getPerson1StatsXmlTags() {
        return getPersonStatsXmlTags(p1Score, p1Legs, p1ThrowTot, p1ThrowDartTot, p1MisDub,
                p1Throw180, p1Throw140, p1Throw100);
    }

    public String getPerson2StatsXmlTags() {
        return getPersonStatsXmlTags(p2Score, p2Legs, p2ThrowTot, p2ThrowDartTot, p2MisDub,
                p2Throw180, p2Throw140, p2Throw100);
    }

    public String getPerson1StatsJsonEntries() {
        return getPersonStatsJsonEntries(p1Score, p1Legs, p1ThrowTot, p1ThrowDartTot, p1MisDub,
                p1Throw180, p1Throw140, p1Throw100);
    }

    public String getPerson2StatsJsonEntries() {
        return getPersonStatsJsonEntries(p2Score, p2Legs, p2ThrowTot, p2ThrowDartTot, p2MisDub,
                p2Throw180, p2Throw140, p2Throw100);
    }

    public String getPerson1StatsYamlEntries(int nestingLevel) {
        return getPersonStatsYamlEntries(p1Score, p1Legs, p1ThrowTot, p1ThrowDartTot, p1MisDub,
                p1Throw180, p1Throw140, p1Throw100, nestingLevel);
    }

    public String getPerson2StatsYamlEntries(int nestingLevel) {
        return getPersonStatsYamlEntries(p2Score, p2Legs, p2ThrowTot, p2ThrowDartTot, p2MisDub,
                p2Throw180, p2Throw140, p2Throw100, nestingLevel);
    }

    private String getPersonStatsXmlTags(int score, int legs, int throwTot, int throwDartTot, int misDub,
                                         int throw180, int throw140, int throw100) {
        return "<score>" + score + "</score><average>" +
                getFormattedAverage(throwTot, throwDartTot) + "</average>" +
                "<checkOutAverage>" + getCheckOutString(legs, misDub) +
                "</checkOutAverage><score180>" +
                throw180 + "</score180><score140>" + throw140 + "</score140><score100>" + throw100 + "</score100>";
    }

    private String getPersonStatsJsonEntries(int score, int legs, int throwTot, int throwDartTot, int misDub,
                                             int throw180, int throw140, int throw100) {
        return "\"score\":" + score + ",\"average\":\"" +
                getFormattedAverage(throwTot, throwDartTot) +
                "\",\"checkOutAverage\":\"" + getCheckOutString(legs, misDub) +
                "\",\"score180\":" +
                throw180 + ",\"score140\":" + throw140 + ",\"score100\":" + throw100;
    }

    private String getPersonStatsYamlEntries(int score, int legs, int throwTot, int throwDartTot, int misDub,
                                             int throw180, int throw140, int throw100, int nestingLevel) {
        String prefix = Util.padCharacter("", ' ', nestingLevel);

        return formatYamlEntry("score: " + score, prefix) +
                formatYamlEntry("average: " + getFormattedAverage(throwTot, throwDartTot), prefix) +
                formatYamlEntry("checkOutAverage: " + getCheckOutString(legs, misDub), prefix) +
                formatYamlEntry("score180: " + throw180, prefix) +
                formatYamlEntry("score140: " + throw140, prefix) +
                formatYamlEntry("score100: " + throw100, prefix);
    }

    private String getCheckOutString(int legs, int misDub) {
        double percentage = 0.0;

        if (legs + misDub > 0)
            percentage = 100.0 * (double)legs / (double)(legs + misDub);

        return Util.getDoubleAsStringWith2Digits(percentage) + "% (" + legs + " / " + (legs + misDub) + ")";
    }

    private String getFormattedAverage(int throwTot, int throwDartTot) {
        return Util.getDoubleAsStringWith2Digits(throwDartTot > 0 ? 3.0 * (double)throwTot / (double)throwDartTot : 0.0);
    }

    private String formatYamlEntry(String entry, String prefix) {
        return prefix + entry + "\n";
    }

    public void increaseP1Score() {
        this.p1Score += 1;
    }

    public void increaseP2Score() {
        this.p2Score += 1;
    }

    public void addP1Legs(int p1Legs) {
        this.p1Legs += p1Legs;
    }

    public void addP2Legs(int p2Legs) {
        this.p2Legs += p2Legs;
    }

    public void addP1ThrowTot(int p1ThrowTot) {
        this.p1ThrowTot += p1ThrowTot;
    }

    public void addP2ThrowTot(int p2ThrowTot) {
        this.p2ThrowTot += p2ThrowTot;
    }

    public void addP1ThrowDartTot(int p1ThrowDartTot) {
        this.p1ThrowDartTot += p1ThrowDartTot;
    }

    public void addP2ThrowDartTot(int p2ThrowDartTot) {
        this.p2ThrowDartTot += p2ThrowDartTot;
    }

    public void addP1MisDub(int p1MisDub) {
        this.p1MisDub += p1MisDub;
    }

    public void addP2MisDub(int p2MisDub) {
        this.p2MisDub += p2MisDub;
    }

    public void addP1Throw180() {
        p1Throw180 += 1;
    }

    public void addP2Throw180() {
        p2Throw180 += 1;
    }

    public void addP1Throw140() {
        p1Throw140 += 1;
    }

    public void addP2Throw140() {
        p2Throw140 += 1;
    }

    public void addP1Throw100() {
        p1Throw100 += 1;
    }

    public void addP2Throw100() {
        p2Throw100 += 1;
    }
}
