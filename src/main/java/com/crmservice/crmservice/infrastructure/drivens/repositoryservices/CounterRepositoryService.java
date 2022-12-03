package com.crmservice.crmservice.infrastructure.drivens.repositoryservices;

import com.crmservice.crmservice.domain.interfaces.ICounterRepositoryService;
import com.crmservice.crmservice.infrastructure.drivens.repositories.ICounterRepository;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

public class CounterRepositoryService implements ICounterRepositoryService {

    private final ICounterRepository counterRepository;

    public CounterRepositoryService(ICounterRepository counterRepository) {
        this.counterRepository = counterRepository;
    }

    @Override
    public Optional<Integer> getValueById(String counterId) {
        return Optional.ofNullable(this.counterRepository.get(counterId));
    }

    @Override
    public void save(String counterId, Integer value, Integer ttl) {
        this.counterRepository.save(counterId, value, Duration.of(ttl, ChronoUnit.SECONDS));
    }
}
