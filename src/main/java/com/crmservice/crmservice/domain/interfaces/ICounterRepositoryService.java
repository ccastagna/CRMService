package com.crmservice.crmservice.domain.interfaces;

import io.netty.util.AsyncMapping;

import java.util.Optional;

public interface ICounterRepositoryService {
    Optional<Integer> getValueById(String counterId);

    void save(String counterId, Integer currentCounterValue, Integer ttl);
}
