package com.crmservice.crmservice.domain.services.attemptscounter;

import com.crmservice.crmservice.domain.interfaces.ICounterRepositoryService;

import java.text.MessageFormat;
import java.util.Optional;

public class AttemptsCounter implements IAttemptsCounter {

    private static final String COUNTER_KEY_TEMPLATE = "{0}:{1}";

    private ICounterRepositoryService counterRepositoryService;

    public AttemptsCounter(ICounterRepositoryService counterRepositoryService) {
        this.counterRepositoryService = counterRepositoryService;
    }

    @Override
    public Integer count(String id, String eventType, Integer ttl) {
        String counterId = MessageFormat.format(COUNTER_KEY_TEMPLATE, id, eventType);


        Integer counterValue = counterRepositoryService.getValueById(counterId)
                .map(value -> ++value)
                .orElse(1);

        counterRepositoryService.save(counterId, counterValue, ttl);

        return counterValue;
    }

    @Override
    public void reset(String id, String eventType, Integer ttl) {
        String counterId = MessageFormat.format(COUNTER_KEY_TEMPLATE, id, eventType);
        counterRepositoryService.save(counterId, 0, ttl);
    }

    @Override
    public Optional<Integer> get(String id, String eventType) {
        String counterId = MessageFormat.format(COUNTER_KEY_TEMPLATE, id, eventType);
        return Optional.empty();
    }
}
