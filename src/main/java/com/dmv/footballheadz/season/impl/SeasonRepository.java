package com.dmv.footballheadz.season.impl;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedList;
import com.dmv.footballheadz.season.IRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class SeasonRepository implements IRepository<Season> {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private DynamoDBMapper dbMapper;

    @Override
    public List<Season> readExpression(DynamoDBScanExpression dynamoDBScanExpression) {
        log.trace("Entering readQuery()");
        PaginatedList<Season> results = dbMapper.scan(Season.class, dynamoDBScanExpression);
        results.loadAllResults();
        return results;
    }

    @Override
    public List<Season> readAll() {
        log.trace("Entering readAll()");
        PaginatedList<Season> results = dbMapper.scan(Season.class, new DynamoDBScanExpression());
        results.loadAllResults();
        return results;
    }

    @Override
    public Optional<Season> read(String name) {
        log.trace("Entering read() with {}", name);
        return Optional.ofNullable(dbMapper.load(Season.class, name));
    }

    @Override
    public void save(Season season) {
        log.trace("Entering save() with {}", season);
        dbMapper.save(season);
    }

    @Override
    public void delete(String id) {
        dbMapper.delete(new Season().withId(id), new DynamoDBMapperConfig(DynamoDBMapperConfig.SaveBehavior.CLOBBER));
    }
}
