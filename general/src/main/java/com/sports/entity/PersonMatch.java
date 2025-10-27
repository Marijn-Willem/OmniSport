package com.sports.entity;

import com.sports.db.util.QueryUtil;

public class PersonMatch extends H2HMatch {
    private Integer personSport1Id;
    private Integer personSport2Id;
    private boolean person1Start;
    private Integer person1score;
    private Integer person2score;
    private Integer person1ncrId;
    private Integer person2ncrId;

    private int personMatchId;
    private String person1Name;
    private String person2Name;
    private int score2Person1;
    private int score2Person2;
    private int maxPersonMatchPart1Id;
    private int maxPersonMatchPart2Id;
    private Integer bestOf1;
    private Integer bestOf2;
    private Integer bestOfDec;

    @Override
    String[] getSpecificPropertiesInSQLStrings() {
        return new String[] {
                QueryUtil.convertBooleanToDbValue(person1Start)
            };
    }

    @Override
    void copySpecific(H2HMatch other) {
        ((PersonMatch) other).person1Start = person1Start;
    }

    public void increaseScore1Person1() {
        person1score++;
    }

    public void increaseScore2Person1() {
        score2Person1++;
    }

    public void clearScores2() {
        score2Person1 = score2Person2 = 0;
    }

    public void increaseScore1Person2() {
        person2score++;
    }

    public void increaseScore2Person2() {
        score2Person2++;
    }

    public void increaseMaxPersonMatchPart2Id() {
        maxPersonMatchPart2Id++;
    }

    void setSpecificId(int specificId) { personMatchId = specificId; }

    public int getSpecificId() {
        return personMatchId;
    }

    public void setParticipant1Id(Integer participant1id) {
        personSport1Id = participant1id;
    }

    public Integer getParticipant1Id() {
        return personSport1Id;
    }

    public void setParticipant2Id(Integer participant2id) {
        personSport2Id = participant2id;
    }

    public Integer getParticipant2Id() {
        return personSport2Id;
    }

    public boolean isParticipant1Start() {
        return isPerson1Start();
    }

    public void setParticipant1Start(boolean participant1Start) {
        setPerson1Start(participant1Start);
    }

    public Integer getPerson1score() {
        return person1score;
    }

    public void setPerson1score(Integer person1score) {
        this.person1score = person1score;
    }

    public Integer getPerson2score() {
        return person2score;
    }

    public void setPerson2score(Integer person2score) {
        this.person2score = person2score;
    }

    public Integer getPerson1ncrId() {
        return person1ncrId;
    }

    public void setPerson1ncrId(Integer person1ncrId) {
        this.person1ncrId = person1ncrId;
    }

    public Integer getPerson2ncrId() {
        return person2ncrId;
    }

    public void setPerson2ncrId(Integer person2ncrId) {
        this.person2ncrId = person2ncrId;
    }

    public void setScore1_1(Integer score1_1) {
        setPerson1score(score1_1);
    }

    public Integer getScore1_1() {
        return getPerson1score();
    }

    public void setScore1_2(Integer score1_2) {
        setPerson2score(score1_2);
    }

    public Integer getScore1_2() {
        return getPerson2score();
    }

    @Override
    public Integer getParticipant1NcrId() {
        return getPerson1ncrId();
    }

    @Override
    public void setParticipant1NcrId(Integer participant1NcrId) {
        setPerson1ncrId(participant1NcrId);
    }

    @Override
    public Integer getParticipant2NcrId() {
        return getPerson2ncrId();
    }

    @Override
    public void setParticipant2NcrId(Integer participant2NcrId) {
        setPerson2ncrId(participant2NcrId);
    }

    public Integer getPersonSport1Id() {
        return personSport1Id;
    }

    public void setPersonSport1Id(Integer personSport1Id) {
        this.personSport1Id = personSport1Id;
    }

    public Integer getPersonSport2Id() {
        return personSport2Id;
    }

    public boolean isPerson1Start() {
        return person1Start;
    }

    public void setPerson1Start(boolean person1Start) {
        this.person1Start = person1Start;
    }

    public int getPersonMatchId() {
        return personMatchId;
    }

    public void setPersonMatchId(int personMatchId) {
        this.personMatchId = personMatchId;
    }

    public String getPerson1Name() {
        return person1Name;
    }

    public void setPerson1Name(String person1Name) {
        this.person1Name = person1Name;
    }

    public String getPerson2Name() {
        return person2Name;
    }

    public void setPerson2Name(String person2Name) {
        this.person2Name = person2Name;
    }

    public int getScore2Person1() {
        return score2Person1;
    }

    public int getScore2Person2() {
        return score2Person2;
    }

    public int getMaxPersonMatchPart1Id() {
        return maxPersonMatchPart1Id;
    }

    public void setMaxPersonMatchPart1Id(int maxPersonMatchPart1Id) {
        this.maxPersonMatchPart1Id = maxPersonMatchPart1Id;
    }

    public int getMaxPersonMatchPart2Id() {
        return maxPersonMatchPart2Id;
    }

    public void setMaxPersonMatchPart2Id(int maxPersonMatchPart2Id) {
        this.maxPersonMatchPart2Id = maxPersonMatchPart2Id;
    }

    public Integer getBestOf1() {
        return bestOf1;
    }

    public void setBestOf1(Integer bestOf1) {
        this.bestOf1 = bestOf1;
    }

    public Integer getBestOf2() {
        return bestOf2;
    }

    public void setBestOf2(Integer bestOf2) {
        this.bestOf2 = bestOf2;
    }

    public Integer getBestOfDec() {
        return bestOfDec;
    }

    public void setBestOfDec(Integer bestOfDec) {
        this.bestOfDec = bestOfDec;
    }
}
