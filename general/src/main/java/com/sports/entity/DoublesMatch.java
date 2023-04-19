package com.sports.entity;

import com.sports.db.util.QueryUtil;
import com.sports.entity.manager.*;

import java.sql.Statement;

public class DoublesMatch extends H2HMatch {
    private Integer double1Id;
    private Integer double2Id;
    private boolean double1Start;
    private Integer double1score;
    private Integer double2score;
    private Integer double1ncrId;
    private Integer double2ncrId;

    private int doublesMatchId;

    @Override
    public String[] getSpecificPropertiesInSQLStrings() {
        return new String[] {
                QueryUtil.convertBooleanToDbValue(double1Start)
            };
    }

    public H2HMatchManager getManager(Statement stat) {
        return new DoublesMatchManager(stat);
    }

    public H2HMatchPartManager getMatchPartManager(Statement stat) {
        return new DoublesMatchPartManager(stat);
    }

    public ParticipantManager getParticipantManager(Statement stat) {
        return new DoubleManager(stat);
    }

    public CompSeasonParticipantManager getCompSeasonParticManager(Statement stat) {
        return new CompSeasonDoubleManager(stat);
    }

    public CompSeasonPhaseParticipantManager getPhaseParticManager(Statement stat) {
        return new CompSeasonPhaseDoubleManager(stat);
    }

    public int getSpecificId() {
        return doublesMatchId;
    }

    public void setParticipant1Id(Integer participant1id) {
        double1Id = participant1id;
    }

    public Integer getParticipant1Id() {
        return double1Id;
    }

    public void setParticipant2Id(Integer participant2id) {
        double2Id = participant2id;
    }

    public Integer getParticipant2Id() {
        return double2Id;
    }

    public boolean isParticipant1Start() {
        return isDouble1Start();
    }

    public void setParticipant1Start(boolean participant1Start) {
        setDouble1Start(participant1Start);
    }

    public Integer getDouble1score() {
        return double1score;
    }

    public void setDouble1score(Integer double1score) {
        this.double1score = double1score;
    }

    public Integer getDouble2score() {
        return double2score;
    }

    public void setDouble2score(Integer double2score) {
        this.double2score = double2score;
    }

    public void setScore1_1(Integer score1_1) {
        setDouble1score(score1_1);
    }

    public Integer getScore1_1() {
        return getDouble1score();
    }

    public void setScore1_2(Integer score1_2) {
        setDouble2score(score1_2);
    }

    public Integer getScore1_2() {
        return getDouble2score();
    }

    public Integer getDouble1ncrId() {
        return double1ncrId;
    }

    public void setDouble1ncrId(Integer double1ncrId) {
        this.double1ncrId = double1ncrId;
    }

    public Integer getDouble2ncrId() {
        return double2ncrId;
    }

    public void setDouble2ncrId(Integer double2ncrId) {
        this.double2ncrId = double2ncrId;
    }

    @Override
    public Integer getParticipant1NcrId() {
        return getDouble1ncrId();
    }

    @Override
    public void setParticipant1NcrId(Integer participant1NcrId) {
        setDouble1ncrId(participant1NcrId);
    }

    @Override
    public Integer getParticipant2NcrId() {
        return getDouble2ncrId();
    }

    @Override
    public void setParticipant2NcrId(Integer participant2NcrId) {
        setDouble2ncrId(participant2NcrId);
    }

    public Integer getDouble1Id() {
        return double1Id;
    }

    public void setDouble1Id(Integer double1Id) {
        this.double1Id = double1Id;
    }

    public Integer getDouble2Id() {
        return double2Id;
    }

    public void setDouble2Id(Integer double2Id) {
        this.double2Id = double2Id;
    }

    public boolean isDouble1Start() {
        return double1Start;
    }

    public void setDouble1Start(boolean double1Start) {
        this.double1Start = double1Start;
    }

    public int getDoublesMatchId() {
        return doublesMatchId;
    }

    public void setDoublesMatchId(int doublesMatchId) {
        this.doublesMatchId = doublesMatchId;
    }
}
