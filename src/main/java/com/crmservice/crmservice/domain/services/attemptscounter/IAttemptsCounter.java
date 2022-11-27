package com.crmservice.crmservice.domain.services.attemptscounter;

import java.util.Optional;

public interface IAttemptsCounter {
    Integer count(String id, String eventType, Integer ttl);

    void reset(String id, String eventType, Integer ttl);

    Optional<Integer> get(String id, String eventType);
}
