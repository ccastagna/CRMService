package com.crmservice.crmservice.infrastructure.drivens.repositories;

import java.time.Duration;

public interface ICounterRepository {
    Integer get(String key);
    void save(String key, Integer value, Duration ttl);
}
