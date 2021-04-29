package com.dmv.footballheadz.season.impl;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.*;

@ExtendWith(SpringExtension.class)
class SeasonControllerTest {

    @InjectMocks
    private SeasonController controller;

    @Mock
    private SeasonService service;

    @Mock
    private SeasonValidation validation;

    @Test
    public void listShouldRespondWithNoContentWhenNothingInDatabase() throws Exception {

        when(service.list()).thenReturn(emptyList());
        ResponseEntity<List<Season>> result = controller.list();
        assertThat(result, is(responseEntityWithStatus(NO_CONTENT)));
    }

    @Test
    public void listShouldRespondWithOkAndResultsFromService() throws Exception {

        Season season1 = new Season().withId("1d");
        Season season2 = new Season().withId("1d2");
        when(service.list()).thenReturn(asList(season1, season2));
        ResponseEntity<List<Season>> result = controller.list();
        assertThat(result, is(allOf(
                responseEntityWithStatus(OK),
                responseEntityThat(containsInAnyOrder(season1, season2)))));
    }

    @Test
    public void listExpressionShouldRespondWithNoContentWhenNothingInDatabase() throws Exception {

        String year = "2012";
        when(service.listByYear(year)).thenReturn(emptyList());
        when(validation.isValidYear(year)).thenReturn(true);
        ResponseEntity<List<Season>> result = controller.listOfYear(year);
        assertThat(result, is(responseEntityWithStatus(NO_CONTENT)));
    }

    @Test
    public void listExpressionShouldRespondWithOkAndResultsFromService() throws Exception {

        String year = "2020";
        Season season1 = new Season().withId("1d");
        Season season2 = new Season().withId("1d2");
        when(service.listByYear(year)).thenReturn(asList(season1, season2));
        when(validation.isValidYear(year)).thenReturn(true);
        ResponseEntity<List<Season>> result = controller.listOfYear(year);
        assertThat(result, is(allOf(
                responseEntityWithStatus(OK),
                responseEntityThat(containsInAnyOrder(season1, season2)))));
    }

    @Test
    public void readShouldReplyWithNotFoundIfNoSuchSeason() throws Exception {

        when(service.read("1d2")).thenReturn(Optional.empty());
        ResponseEntity<Season> result = controller.read("1d2");
        assertThat(result, is(responseEntityWithStatus(NOT_FOUND)));
    }

    @Test
    public void readShouldReplyWithSeasonIfSeasonExists() throws Exception {

        Season season = new Season().withId("1d2");
        when(service.read("1d2")).thenReturn(Optional.of(season));
        ResponseEntity<Season> result = controller.read("1d2");
        assertThat(result, is(allOf(
                responseEntityWithStatus(OK),
                responseEntityThat(equalTo(season)))));
    }

    @Test
    public void createShouldReplyWithConflictIfSeasonAlreadyExists() throws Exception {

        Season season = new Season().withId("1d2");
        when(service.create(season)).thenReturn(Optional.empty());
        ResponseEntity<Season> result = controller.create(season);
        assertThat(result, is(responseEntityWithStatus(CONFLICT)));
    }

    @Test
    public void createShouldReplyWithCreatedAndSeasonData() throws Exception {

        Season season = new Season().withId("1d2");
        when(service.create(season)).thenReturn(Optional.of(season));
        ResponseEntity<Season> result = controller.create(season);
        assertThat(result, is(allOf(
                responseEntityWithStatus(CREATED),
                responseEntityThat(equalTo(season)))));
    }

    @Test
    public void putShouldReplyWithNotFoundIfSeasonDoesNotExist() throws Exception {

        Season newSeasonData = new Season().withId("1d2").withTeamName("GimmyDaLoot");
        when(service.replace(newSeasonData)).thenReturn(Optional.empty());
        ResponseEntity<Season> result = controller.put("1d2", new Season().withTeamName("GimmyDaLoot"));
        assertThat(result, is(responseEntityWithStatus(NOT_FOUND)));
    }

    @Test
    public void putShouldReplyWithUpdatedSeasonAndOkIfSeasonExists() throws Exception {

        Season newSeasonData = new Season().withId("1d2").withTeamName("GimmyDaLoot");
        when(service.replace(newSeasonData)).thenReturn(Optional.of(newSeasonData));
        ResponseEntity<Season> result = controller.put("1d2", new Season().withTeamName("GimmyDaLoot"));
        assertThat(result, is(allOf(
                responseEntityWithStatus(OK),
                responseEntityThat(equalTo(newSeasonData)))));
    }

    @Test
    public void patchShouldReplyWithNotFoundIfSeasonDoesNotExist() throws Exception {

        Season newSeasonData = new Season().withId("1d2").withTeamName("GimmyDaLoot");
        when(service.update(newSeasonData)).thenReturn(Optional.empty());
        ResponseEntity<Season> result = controller.patch("1d2", new Season().withTeamName("GimmyDaLoot"));
        assertThat(result, is(responseEntityWithStatus(NOT_FOUND)));
    }

    @Test
    public void patchShouldReplyWithUpdatedSeasonAndOkIfSeasonExists() throws Exception {

        Season newSeasonData = new Season().withId("1d2").withTeamName("GimmyDaLoot");
        when(service.update(newSeasonData)).thenReturn(Optional.of(newSeasonData));
        ResponseEntity<Season> result = controller.patch("1d2", new Season().withTeamName("GimmyDaLoot"));
        assertThat(result, is(allOf(
                responseEntityWithStatus(OK),
                responseEntityThat(equalTo(newSeasonData)))));
    }

    @Test
    public void deleteShouldRespondWithNotFoundIfSeasonDoesNotExist() throws Exception {

        when(service.delete("1d2")).thenReturn(false);
        ResponseEntity<Void> result = controller.delete("1d2");
        assertThat(result, is(responseEntityWithStatus(NOT_FOUND)));
    }

    @Test
    public void deleteShouldRespondWithNoContentIfDeleteSuccessful() throws Exception {

        when(service.delete("1d2")).thenReturn(true);
        ResponseEntity<Void> result = controller.delete("1d2");
        assertThat(result, is(responseEntityWithStatus(NO_CONTENT)));
    }

    private Matcher<ResponseEntity> responseEntityWithStatus(HttpStatus status) {

        return new TypeSafeMatcher<ResponseEntity>() {

            @Override
            protected boolean matchesSafely(ResponseEntity item) {

                return status.equals(item.getStatusCode());
            }

            @Override
            public void describeTo(Description description) {

                description.appendText("ResponseEntity with status ").appendValue(status);
            }
        };
    }

    private <T> Matcher<ResponseEntity<? extends T>> responseEntityThat(Matcher<T> categoryMatcher) {

        return new TypeSafeMatcher<ResponseEntity<? extends T>>() {
            @Override
            protected boolean matchesSafely(ResponseEntity<? extends T> item) {

                return categoryMatcher.matches(item.getBody());
            }

            @Override
            public void describeTo(Description description) {

                description.appendText("ResponseEntity with ").appendValue(categoryMatcher);
            }
        };
    }

}