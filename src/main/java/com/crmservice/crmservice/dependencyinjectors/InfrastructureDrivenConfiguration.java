package com.crmservice.crmservice.dependencyinjectors;

import com.crmservice.crmservice.domain.interfaces.ICounterRepositoryService;
import com.crmservice.crmservice.domain.interfaces.ITokenService;
import com.crmservice.crmservice.domain.interfaces.IUserRepositoryService;
import com.crmservice.crmservice.infrastructure.drivens.externalservices.token.PasetoTokenService;
import com.crmservice.crmservice.infrastructure.drivens.repositories.CounterRepository;
import com.crmservice.crmservice.infrastructure.drivens.repositories.ICounterRepository;
import com.crmservice.crmservice.infrastructure.drivens.repositoryservices.CounterRepositoryService;
import com.crmservice.crmservice.infrastructure.drivens.repositoryservices.UserRepositoryService;
import dev.paseto.jpaseto.Version;
import dev.paseto.jpaseto.lang.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.KeyPair;
import java.security.SecureRandom;

@Configuration
@Order(1)
public class InfrastructureDrivenConfiguration {

    private static final int ENCODER_STRENGTH = 10;

    @Bean
    PasswordEncoder getBCryptEncoder() {
        return new BCryptPasswordEncoder(ENCODER_STRENGTH, new SecureRandom());
    }

    @Bean
    ITokenService getTokenService() {
        KeyPair keyPair = Keys.keyPairFor(Version.V2);
        return new PasetoTokenService(keyPair);
    }

    @Bean
    public RedisTemplate<String, Integer> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Integer> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        return template;
    }

    @Bean
    IUserRepositoryService getUserRepositoryService() {
        return new UserRepositoryService();
    }

    @Bean
    ICounterRepository getCounterRepository(@Autowired RedisTemplate<String, Integer> redisTemplate) {
        return new CounterRepository(redisTemplate);
    }

    @Bean
    ICounterRepositoryService getCounterRepositoryService(@Autowired ICounterRepository counterRepository) {
        return new CounterRepositoryService(counterRepository);
    }
}
