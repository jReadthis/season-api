package com.dmv.footballheadz.season.impl;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

@DynamoDBTable(tableName = "Season")
public class Season implements Serializable {

    private static final long serialVersionUID = -3534650012619938612L;

    private String id;
    private String year;
    private String rank;
    private String playoffRank;
    private String teamName;
    private String record;
    private Double pct;
    private String streak;
    private Double pointsFor;
    private Double pointsAgainst;

    @DynamoDBHashKey(attributeName = "Id")
    @NotNull(message = "Id must not be empty")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Season withId(String id) {
        setId(id);
        return this;
    }

    @DynamoDBAttribute(attributeName = "Year")
    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public Season withYear(String year) {
        setYear(year);
        return this;
    }

    @DynamoDBAttribute(attributeName = "Rank")
    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public Season withRank(String rank) {
        setRank(rank);
        return this;
    }

    @DynamoDBAttribute(attributeName = "PlayoffRank")
    public String getPlayoffRank() {
        return playoffRank;
    }

    public void setPlayoffRank(String playoffRank) {
        this.playoffRank = playoffRank;
    }

    public Season withPlayoffRank(String playoffRank) {
        setPlayoffRank(playoffRank);
        return this;
    }

    @DynamoDBAttribute(attributeName = "TeamName")
    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public Season withTeamName(String teamName) {
        setTeamName(teamName);
        return this;
    }

    @DynamoDBAttribute(attributeName = "Record")
    public String getRecord() {
        return record;
    }

    public void setRecord(String record) {
        this.record = record;
    }

    public Season withRecord(String record) {
        setRecord(record);
        return this;
    }

    @DynamoDBAttribute(attributeName = "Pct")
    public Double getPct() {
        return pct;
    }

    public void setPct(Double pct) {
        this.pct = pct;
    }

    public Season withPct(Double pct) {
        setPct(pct);
        return this;
    }

    @DynamoDBAttribute(attributeName = "Streak")
    public String getStreak() {
        return streak;
    }

    public void setStreak(String streak) {
        this.streak = streak;
    }

    public Season withStreak(String streak) {
        setStreak(streak);
        return this;
    }

    @DynamoDBAttribute(attributeName = "PointsFor")
    public Double getPointsFor() {
        return pointsFor;
    }

    public void setPointsFor(Double pointsFor) {
        this.pointsFor = pointsFor;
    }

    public Season withPointsFor(Double pointsFor) {
        setPointsFor(pointsFor);
        return this;
    }

    @DynamoDBAttribute(attributeName = "PointsAgainst")
    public Double getPointsAgainst() {
        return pointsAgainst;
    }

    public void setPointsAgainst(Double pointsAgainst) {
        this.pointsAgainst = pointsAgainst;
    }

    public Season withPointAgainst(Double pointsAgainst) {
        setPointsAgainst(pointsAgainst);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Season season = (Season) o;
        return id.equals(season.id) && Objects.equals(year, season.year)
                && Objects.equals(rank, season.rank) && Objects.equals(playoffRank, season.playoffRank)
                && Objects.equals(teamName, season.teamName) && Objects.equals(record, season.record)
                && Objects.equals(pct, season.pct) && Objects.equals(streak, season.streak)
                && Objects.equals(pointsFor, season.pointsFor) && Objects.equals(pointsAgainst, season.pointsAgainst);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, year, rank, playoffRank, teamName, record, pct, streak, pointsFor, pointsAgainst);
    }
}
