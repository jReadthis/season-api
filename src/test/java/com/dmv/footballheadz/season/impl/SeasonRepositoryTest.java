package com.dmv.footballheadz.season.impl;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedScanList;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class SeasonRepositoryTest {

    @Mock
    private DynamoDBMapper dbMapper;

    @InjectMocks
    private SeasonRepository repository;

    @Test
    public void readExpressionShouldFilterTheScan() throws Exception {
        PaginatedScanList expectedResult = mock(PaginatedScanList.class);
        when(dbMapper.scan(eq(Season.class), any(DynamoDBScanExpression.class))).thenReturn(expectedResult);

        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":val1", new AttributeValue().withS("2012"));

        DynamoDBScanExpression dynamoDBScanExpression = new DynamoDBScanExpression()
                .withFilterExpression("Year = :val1")
                .withExpressionAttributeValues(eav);

        List<Season> result = repository.readExpression(dynamoDBScanExpression);
        assertThat(result, is(expectedResult));
        verify(expectedResult).loadAllResults();
    }

    @Test
    @SuppressWarnings("unchecked")
    public void readAllShouldScanTheTable() throws Exception {
        PaginatedScanList expectedResult = mock(PaginatedScanList.class);
        when(dbMapper.scan(eq(Season.class), any(DynamoDBScanExpression.class))).thenReturn(expectedResult);
        List<Season> result = repository.readAll();
        assertThat(result, is(expectedResult));
        verify(expectedResult).loadAllResults();
    }

    @Test
    public void readShouldReturnEmptyOptionalWhenNoResult() throws Exception {
        when(dbMapper.load(Season.class, "Id12x4")).thenReturn(null);
        Optional<Season> result = repository.read("Id12x4");
        assertThat(result, is(Optional.empty()));
    }

    @Test
    public void readShouldWrapResultIntoOptional() throws Exception {
        Season customer = new Season().withId("Id12x4");
        when(dbMapper.load(Season.class, "Id12x4")).thenReturn(customer);
        Season result = repository.read("Id12x4").get();
        assertThat(result, is(equalTo(customer)));
    }

    @Test
    public void saveShouldPersistCustomer() throws Exception {
        Season customer = new Season().withId("Id12x4");
        repository.save(customer);
        verify(dbMapper).save(customer);
    }

    @Test
    public void deleteShouldDeleteCustomerByName() throws Exception {
        repository.delete("1d");
        verify(dbMapper).delete(eq(new Season().withId("1d")), any(DynamoDBMapperConfig.class));
    }
}