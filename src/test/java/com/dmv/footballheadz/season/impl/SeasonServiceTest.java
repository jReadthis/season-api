package com.dmv.footballheadz.season.impl;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.collection.IsEmptyCollection.emptyCollectionOf;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class SeasonServiceTest {

    @Mock
    private SeasonRepository repository;

    @InjectMocks
    private SeasonService service;

    @Test
    public void readShouldReturnEmptyOptionalWhenNoSeasonFound() throws Exception {

        when(repository.read("1d")).thenReturn(Optional.empty());
        Optional<Season> result = service.read("1d");
        assertThat(result, is(Optional.empty()));
    }

    @Test
    public void readShouldReturnResultWhenSeasonFound() throws Exception {

        Season customer = new Season().withId("1d");
        when(repository.read("1d")).thenReturn(Optional.of(customer));
        Season result = service.read("1d").get();
        assertThat(result, is(equalTo(customer)));
    }

    @Test
    public void createShouldReturnEmptyOptionalWhenSeasonAlreadyExists() throws Exception {

        Season existingSeason = new Season().withId("1d").withTeamName("GimmyDaLoot");
        when(repository.read("1d")).thenReturn(Optional.of(existingSeason));
        Season newSeason = new Season().withId("1d");
        Optional<Season> result = service.create(newSeason);
        assertThat(result, is(Optional.empty()));
        verify(repository, never()).save(newSeason);
    }

    @Test
    public void createShouldReturnNewSeasonWhenSeasonNotYetExists() throws Exception {

        Season newSeason = new Season().withId("1d");
        when(repository.read("1d")).thenReturn(Optional.empty());
        Season result = service.create(newSeason).get();
        assertThat(result, is(equalTo(newSeason)));
        verify(repository).save(newSeason);
    }

    @Test
    public void replaceShouldReturnEmptyOptionalWhenSeasonNotFound() throws Exception {

        Season newSeasonData = new Season().withId("1d").withTeamName("GimmyDatLoot");
        when(repository.read("1d")).thenReturn(Optional.empty());
        Optional<Season> result = service.replace(newSeasonData);
        assertThat(result, is(Optional.empty()));
        verify(repository, never()).save(newSeasonData);
    }

    @Test
    public void replaceShouldOverwriteAndReturnNewDataWhenSeasonExists() throws Exception {

        Season oldSeasonData = new Season().withId("1d").withTeamName("GimmyDatLoot");
        Season newSeasonData = new Season().withId("1d").withRank("1");
        when(repository.read("1d")).thenReturn(Optional.of(oldSeasonData));
        Season result = service.replace(newSeasonData).get();
        assertThat(result, is(equalTo(newSeasonData)));
        verify(repository).save(newSeasonData);
    }

    @Test
    public void updateShouldReturnEmptyOptionalWhenSeasonNotFound() throws Exception {

        Season newSeasonData = new Season().withId("1d").withRank("1");
        when(repository.read("1d")).thenReturn(Optional.empty());
        Optional<Season> result = service.update(newSeasonData);
        assertThat(result, is(Optional.empty()));
        verify(repository, never()).save(newSeasonData);
    }

    @Test
    public void updateShouldOverwriteExistingFieldAndReturnNewDataWhenSeasonExists() throws Exception {

        Season oldSeasonData = new Season().withId("1d").withRank("2");
        Season newSeasonData = new Season().withId("1d").withRank("1");
        when(repository.read("1d")).thenReturn(Optional.of(oldSeasonData));
        Season result = service.update(newSeasonData).get();
        assertThat(result, is(equalTo(newSeasonData)));
        verify(repository).save(newSeasonData);
    }

    @Test
    public void updateShouldNotOverwriteExistingFieldIfNoNewValuePassedAndShouldReturnNewDataWhenSeasonExists() throws Exception {

        Season oldSeasonData = new Season().withId("1d").withRank("2");
        Season newSeasonData = new Season().withId("1d").withTeamName("GimmyDatLoot");
        Season expectedResult = new Season().withId("1d").withRank("2").withTeamName("GimmyDatLoot");
        when(repository.read("1d")).thenReturn(Optional.of(oldSeasonData));
        Season result = service.update(newSeasonData).get();
        assertThat(result, is(equalTo(expectedResult)));
        verify(repository).save(expectedResult);
    }

    @Test
    public void deleteShouldReturnFalseWhenSeasonNotFound() throws Exception {

        when(repository.read("1d")).thenReturn(Optional.empty());
        boolean result = service.delete("1d");
        assertThat(result, is(false));
    }

    @Test
    public void deleteShouldReturnTrueWhenSeasonDeleted() throws Exception {

        when(repository.read("1d")).thenReturn(Optional.of(new Season().withId("1d")));
        boolean result = service.delete("1d");
        assertThat(result, is(true));
        verify(repository).delete("1d");
    }

    @Test
    public void listShouldReturnEmptyListWhenNothingFound() throws Exception {

        when(repository.readAll()).thenReturn(emptyList());
        List<Season> result = service.list();
        assertThat(result, is(emptyCollectionOf(Season.class)));
    }

    @Test
    public void listShouldReturnAllSeasons() throws Exception {

        Season customer1 = new Season().withId("1d");
        Season customer2 = new Season().withId("2d");
        when(repository.readAll()).thenReturn(asList(customer1, customer2));
        List<Season> result = service.list();
        assertThat(result, containsInAnyOrder(customer1, customer2));
    }

    @Test
    public void listExpressionShouldReturnEmptyListWhenNothingFound() throws Exception {

        when(repository.readExpression(any(DynamoDBScanExpression.class))).thenReturn(emptyList());
        List<Season> result = service.listByYear("2012");
        assertThat(result, is(emptyCollectionOf(Season.class)));
    }

    @Test
    public void listExpressionShouldReturnSeasonsForYear() throws Exception {

        String year = "2012";
        Season season1 = new Season().withId("1d").withYear("2012").withTeamName("DaHogPit");
        Season season2 = new Season().withId("2d").withYear("2012").withTeamName("GimmyDaLoot");

        when(repository.readExpression(any(DynamoDBScanExpression.class)))
                .thenReturn(asList(season1, season2));
        List<Season> result = service.listByYear(year);
        assertThat(result, containsInAnyOrder(season1, season2));
    }

}