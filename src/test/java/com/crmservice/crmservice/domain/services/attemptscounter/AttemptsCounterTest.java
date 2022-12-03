package com.crmservice.crmservice.domain.services.attemptscounter;

import com.crmservice.crmservice.domain.interfaces.ICounterRepositoryService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.text.MessageFormat;
import java.util.Optional;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DataMongoTest
class AttemptsCounterTest {

    private static final String EVENT_TYPE = "eventType";
    private static final String ID = "id";
    private static final String COUNTER_KEY = MessageFormat.format("{0}:{1}", ID, EVENT_TYPE);
    private static final Integer TTL = 3600;

    private IAttemptsCounter attemptsCounter;

    @Mock
    private ICounterRepositoryService counterRepositoryService;

    @BeforeEach
    void setUp() {
        this.attemptsCounter = new AttemptsCounter(counterRepositoryService);
    }

    @Test
    void givenNonExistentCounter_whenCount_thenReturnOne() {
        when(this.counterRepositoryService.getValueById(COUNTER_KEY)).thenReturn(Optional.empty());
        doNothing().when(this.counterRepositoryService).save(COUNTER_KEY, 1, TTL);

        Integer counterResult = this.attemptsCounter.count(ID, EVENT_TYPE, TTL);

        Assertions.assertThat(counterResult).isEqualTo(1);

        verify(this.counterRepositoryService).getValueById(COUNTER_KEY);
        verify(this.counterRepositoryService).save(COUNTER_KEY, 1, TTL);
    }

    @Test
    void givenExistentCounterWhoseValueIsEqualToTwo_whenCount_thenReturnThree() {

        when(this.counterRepositoryService.getValueById(COUNTER_KEY)).thenReturn(Optional.of(2));
        doNothing().when(this.counterRepositoryService).save(COUNTER_KEY, 3, TTL);

        Integer counterResult = this.attemptsCounter.count(ID, EVENT_TYPE, TTL);

        Assertions.assertThat(counterResult).isEqualTo(3);

        verify(this.counterRepositoryService).getValueById(COUNTER_KEY);
        verify(this.counterRepositoryService).save(COUNTER_KEY, 3, TTL);
    }

}