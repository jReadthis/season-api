package com.dmv.footballheadz.season;

import com.dmv.footballheadz.Application;
import com.dmv.footballheadz.season.impl.Season;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.net.URI;
import java.util.Arrays;

import static java.util.UUID.randomUUID;
import static org.hamcrest.MatcherAssert.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class, webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SeasonIntegrationTest {

    @Value("${local.server.port}")
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void postShouldCreateSeasonAndRespondWithCreated() throws Exception {
        Season season = new Season().withId(randomUUID().toString());
        ResponseEntity<Season> result = restTemplate.postForEntity(url("/v1/season"), season, Season.class);
        assertThat(result.getStatusCode(), CoreMatchers.is(HttpStatus.CREATED));
        assertThat(result.getBody(), CoreMatchers.is(CoreMatchers.equalTo(season)));
    }

    @Test
    public void postShouldNotCreateSeasonIfAlreadyExistsAndRespondWithConflict() throws Exception {
        Season season = new Season().withId(randomUUID().toString());
        restTemplate.postForEntity(url("/v1/season"), season, Season.class);
        ResponseEntity<Season> result = restTemplate.postForEntity(url("/v1/season"), season, Season.class);
        assertThat(result.getStatusCode(), CoreMatchers.is(HttpStatus.CONFLICT));
    }

    @Test
    public void postShouldRespondWithBadRequestIfSeasonNameNotPassed() throws Exception {
        Season season = new Season().withRank(randomUUID().toString());
        restTemplate.postForEntity(url("/v1/season"), season, Season.class);
        ResponseEntity<Season> result = restTemplate.postForEntity(url("/v1/season"), season, Season.class);
        assertThat(result.getStatusCode(), CoreMatchers.is(HttpStatus.BAD_REQUEST));
    }

    @Test
    public void getShouldReturnPreviouslyCreatedSeasons() throws Exception {
        Season season1 = new Season().withId(randomUUID().toString());
        Season season2 = new Season().withId(randomUUID().toString());
        restTemplate.postForEntity(url("/v1/season"), season1, Season.class);
        restTemplate.postForEntity(url("/v1/season"), season2, Season.class);
        ResponseEntity<Season[]> result = restTemplate.getForEntity(url("/v1/season"), Season[].class);
        assertThat(result.getStatusCode(), CoreMatchers.is(HttpStatus.OK));
        assertThat(Arrays.asList(result.getBody()), CoreMatchers.hasItems(season1, season2));
    }

    @Test
    public void getByNameShouldRespondWithNotFoundForSeasonThatDoesNotExist() throws Exception {
        String seasonName = randomUUID().toString();
        ResponseEntity<Season> result = restTemplate.getForEntity(url("/v1/season/" + seasonName), Season.class);
        assertThat(result.getStatusCode(), CoreMatchers.is(HttpStatus.NOT_FOUND));
    }

    @Test
    public void getByNameShouldReturnPreviouslyCreatedSeason() throws Exception {
        String seasonId = randomUUID().toString();
        Season season = new Season().withId(seasonId).withTeamName("GimmyDatLoot");
        restTemplate.postForEntity(url("/v1/season"), season, Season.class);
        ResponseEntity<Season> result = restTemplate.getForEntity(url("/v1/season/" + seasonId), Season.class);
        assertThat(result.getStatusCode(), CoreMatchers.is(HttpStatus.OK));
        assertThat(result.getBody(), CoreMatchers.is(CoreMatchers.equalTo(season)));
    }

    @Test
    public void putShouldReplyWithNotFoundForSeasonThatDoesNotExist() throws Exception {
        String seasonName = randomUUID().toString();
        Season season = new Season().withId(seasonName);
        RequestEntity<Season> request = new RequestEntity<>(season, HttpMethod.PUT, url("/v1/season/" + seasonName));
        ResponseEntity<Season> result = restTemplate.exchange(request, Season.class);
        assertThat(result.getStatusCode(), CoreMatchers.is(HttpStatus.NOT_FOUND));
    }

    @Test
    public void putShouldReplaceExistingSeasonValues() throws Exception {
        String seasonName = randomUUID().toString();
        Season oldSeasonData = new Season().withId(seasonName).withRank("10");
        Season newSeasonData = new Season().withId(seasonName).withTeamName("GimmyDaLoot");
        restTemplate.postForEntity(url("/v1/season"), oldSeasonData, Season.class);
        RequestEntity<Season> request = new RequestEntity<>(newSeasonData, HttpMethod.PUT, url("/v1/season/" + seasonName));
        ResponseEntity<Season> result = restTemplate.exchange(request, Season.class);
        assertThat(result.getStatusCode(), CoreMatchers.is(HttpStatus.OK));
        assertThat(result.getBody(), CoreMatchers.is(CoreMatchers.equalTo(newSeasonData)));
    }

    @Test
    public void patchShouldReplyWithNotFoundForSeasonThatDoesNotExist() throws Exception {
        String seasonName = randomUUID().toString();
        Season season = new Season().withId(seasonName);
        RequestEntity<Season> request = new RequestEntity<>(season, HttpMethod.PATCH, url("/v1/season/" + seasonName));
        ResponseEntity<Season> result = restTemplate.exchange(request, Season.class);
        assertThat(result.getStatusCode(), CoreMatchers.is(HttpStatus.NOT_FOUND));
    }

    @Test
    public void patchShouldAddNewValuesToExistingSeasonValues() throws Exception {
        String seasonName = randomUUID().toString();
        Season oldSeasonData = new Season().withId(seasonName).withRank("10");
        Season newSeasonData = new Season().withId(seasonName).withTeamName("GimmyDaLoot");
        Season expectedNewSeasonData = new Season().withId(seasonName).withTeamName("GimmyDaLoot").withRank("10");
        restTemplate.postForEntity(url("/v1/season"), oldSeasonData, Season.class);
        RequestEntity<Season> request = new RequestEntity<>(newSeasonData, HttpMethod.PATCH, url("/v1/season/" + seasonName));
        ResponseEntity<Season> result = restTemplate.exchange(request, Season.class);
        assertThat(result.getStatusCode(), CoreMatchers.is(HttpStatus.OK));
        assertThat(result.getBody(), CoreMatchers.is(CoreMatchers.equalTo(expectedNewSeasonData)));
    }

    @Test
    public void deleteShouldReturnNotFoundWhenSeasonDoesNotExist() throws Exception {
        String seasonName = randomUUID().toString();
        RequestEntity<Void> request = new RequestEntity<>(HttpMethod.DELETE, url("/v1/season/" + seasonName));
        ResponseEntity<Void> result = restTemplate.exchange(request, Void.class);
        assertThat(result.getStatusCode(), CoreMatchers.is(HttpStatus.NOT_FOUND));
    }

    @Test
    public void deleteShouldRemoveExistingSeasonAndRespondWithNoContent() throws Exception {
        String seasonName = randomUUID().toString();
        Season season = new Season().withId(seasonName);
        restTemplate.postForEntity(url("/v1/season"), season, Season.class);
        RequestEntity<Void> request = new RequestEntity<>(HttpMethod.DELETE, url("/v1/season/" + seasonName));
        ResponseEntity<Void> result = restTemplate.exchange(request, Void.class);
        assertThat(result.getStatusCode(), CoreMatchers.is(HttpStatus.NO_CONTENT));
    }

    private URI url(String url) {

        return URI.create("http://localhost:" + port + url);
    }

}
