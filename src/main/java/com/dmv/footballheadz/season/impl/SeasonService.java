package com.dmv.footballheadz.season.impl;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.dmv.footballheadz.season.IService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.amazonaws.util.StringUtils.isNullOrEmpty;

@Service
public class SeasonService implements IService<Season> {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private SeasonRepository repository;

    public Optional<Season> read(String id) {

        log.trace("Entering read() with {}", id);
        return repository.read(id);
    }

    public Optional<Season> create(Season season) {
        log.trace("Entering create() with {}", season);
        if (repository.read(season.getId()).isPresent()) {
            log.warn("Customer {} not found", season.getId());
            return Optional.empty();
        }
        repository.save(season);
        return Optional.of(season);
    }

    public Optional<Season> replace(Season newSeasonData) {
        log.trace("Entering replace() with {}", newSeasonData);
        Optional<Season> existingSeason = repository.read(newSeasonData.getId());
        if (!existingSeason.isPresent()) {
            log.warn("Customer {} not found", newSeasonData.getId());
            return Optional.empty();
        }
        Season season = existingSeason.get();
        season.setYear(newSeasonData.getYear());
        season.setRank(newSeasonData.getRank());
        season.setPlayoffRank(newSeasonData.getPlayoffRank());
        season.setTeamName(newSeasonData.getTeamName());
        season.setRecord(newSeasonData.getRecord());
        season.setPct(newSeasonData.getPct());
        season.setStreak(newSeasonData.getStreak());
        season.setPointsFor(newSeasonData.getPointsFor());
        season.setPointsAgainst(newSeasonData.getPointsAgainst());
        repository.save(season);
        return Optional.of(season);
    }

    public Optional<Season> update(Season newSeasonData) {

        log.trace("Entering update() with {}", newSeasonData);
        Optional<Season> existingCustomer = repository.read(newSeasonData.getId());
        if (!existingCustomer.isPresent()) {
            log.warn("Season {} not found", newSeasonData.getId());
            return Optional.empty();
        }
        Season season = existingCustomer.get();
        if (!isNullOrEmpty(newSeasonData.getYear())){
            season.setYear(newSeasonData.getYear());
        }
        if (!isNullOrEmpty(newSeasonData.getRank())){
            season.setRank(newSeasonData.getRank());
        }
        if (!isNullOrEmpty(newSeasonData.getPlayoffRank())){
            season.setPlayoffRank(newSeasonData.getPlayoffRank());
        }
        if (!isNullOrEmpty(newSeasonData.getTeamName())){
            season.setTeamName(newSeasonData.getTeamName());
        }
        if (!isNullOrEmpty(newSeasonData.getRecord())){
            season.setRecord(newSeasonData.getRecord());
        }
        if (null != newSeasonData.getPct()){
            season.setPct(newSeasonData.getPct());
        }
        if (!isNullOrEmpty(newSeasonData.getStreak())){
            season.setStreak(newSeasonData.getStreak());
        }
        if (null != newSeasonData.getPointsFor()){
            season.setPointsFor(newSeasonData.getPointsFor());
        }
        if (null != newSeasonData.getPointsAgainst()){
            season.setPointsAgainst(newSeasonData.getPointsAgainst());
        }
        repository.save(season);
        return Optional.of(season);
    }

    public boolean delete(String id) {
        log.trace("Entering delete() with {}", id);
        if (!repository.read(id).isPresent()) {
            log.warn("Manager {} not found", id);
            return false;
        }
        repository.delete(id);
        return true;
    }

    public List<Season> list() {
        log.trace("Entering list()");
        return repository.readAll();
    }

    @Override
    public List<Season> listByYear(String key) {
        log.trace("Entering listByYear()");
        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":val", new AttributeValue().withS(key));

        Map<String, String> ean = new HashMap<>();
        ean.put("#Year", "Year");

        DynamoDBScanExpression dynamoDBScanExpression = new DynamoDBScanExpression()
                .withExpressionAttributeNames(ean)
                .withFilterExpression("#Year = :val")
                .withExpressionAttributeValues(eav);

        return repository.readExpression(dynamoDBScanExpression);
    }

}
