package com.dmv.footballheadz.season.impl;

import com.dmv.footballheadz.util.SeasonUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
public class SeasonValidationTest {

    @InjectMocks
    SeasonValidation seasonValidation;

    @Test
    public void testSeasonValidation_Year(){
        String year = "2012";
        boolean isValid = seasonValidation.isValidYear(year);
        assertTrue(isValid);
    }

    @Test
    public void testSeasonValidation_YearNotLengthOf4(){
        String year = "20121";
        boolean isValid = seasonValidation.isValidYear(year);
        assertFalse(isValid);
    }

    @Test
    public void testSeasonValidation_YearNotNumeric(){
        String year = "201X";
        boolean isValid = seasonValidation.isValidYear(year);
        assertFalse(isValid);
    }

    @Test
    public void testSeasonValidation_IdIsValid(){
        Season season = new Season().withId("2012|GimmyDaLoot")
                .withTeamName("GimmyDaLoot")
                .withYear("2012");
        boolean isValid = seasonValidation.isValidId(season);
        assertTrue(isValid);
    }

    @Test
    public void testSeasonValidation_IdIsNull(){
        Season season = new Season().withId(null)
                .withTeamName("GimmyDaLoot")
                .withYear("2012");
        boolean isValid = seasonValidation.isValidId(season);
        assertFalse(isValid);
    }

    @Test
    public void testSeasonValidation_IdIsEmpty(){
        Season season = new Season().withId(" ")
                .withTeamName("GimmyDaLoot")
                .withYear("2012");
        boolean isValid = seasonValidation.isValidId(season);
        assertFalse(isValid);
    }

    @Test
    public void testSeasonValidation_IdWithDifferentYear(){
        Season season = new Season().withId("2012|GimmyDaLoot")
                .withTeamName("GimmyDaLoot")
                .withYear("2013");
        boolean isValid = seasonValidation.isValidId(season);
        assertFalse(isValid);
    }

    @Test
    public void testSeasonValidation_IdWithDifferentTeamName(){
        Season season = new Season().withId("2012|GimmyDaLoot")
                .withTeamName("Gimmy")
                .withYear("2012");
        boolean isValid = seasonValidation.isValidId(season);
        assertFalse(isValid);
    }

    @Test
    public void testSeasonValidation_RankIsValid(){
        int rank = SeasonUtils.LEAGUE_SIZE;
        boolean isValid = seasonValidation.isValidRank(Integer.toString(rank));
        assertTrue(isValid);
    }

    @Test
    public void testSeasonValidation_RankIsNull(){
        String rank = null;
        boolean isValid = seasonValidation.isValidRank(rank);
        assertFalse(isValid);
    }

    @Test
    public void testSeasonValidation_RankIsEmpty(){
        String rank = " ";
        boolean isValid = seasonValidation.isValidRank(rank);
        assertFalse(isValid);
    }

    @Test
    public void testSeasonValidation_RankIsNotNumeric(){
        String rank = "rank";
        boolean isValid = seasonValidation.isValidRank(rank);
        assertFalse(isValid);
    }

    @Test
    public void testSeasonValidation_RankIsLessThan1(){
        String rank = "0";
        boolean isValid = seasonValidation.isValidRank(rank);
        assertFalse(isValid);
    }

    @Test
    public void testSeasonValidation_RankIsGreaterThanLeagueSize(){
        int rank = SeasonUtils.LEAGUE_SIZE + 1;
        boolean isValid = seasonValidation.isValidRank(Integer.toString(rank));
        assertFalse(isValid);
    }
}
