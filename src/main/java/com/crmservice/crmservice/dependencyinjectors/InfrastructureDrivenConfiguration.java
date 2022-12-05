package com.crmservice.crmservice.dependencyinjectors;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.crmservice.crmservice.domain.interfaces.ICounterRepositoryService;
import com.crmservice.crmservice.domain.interfaces.ICustomerRepositoryService;
import com.crmservice.crmservice.domain.interfaces.ITokenService;
import com.crmservice.crmservice.domain.interfaces.IUserRepositoryService;
import com.crmservice.crmservice.infrastructure.drivens.externalservices.s3.AmazonS3Service;
import com.crmservice.crmservice.infrastructure.drivens.externalservices.s3.IAmazonS3Service;
import com.crmservice.crmservice.infrastructure.drivens.externalservices.token.PasetoTokenService;
import com.crmservice.crmservice.infrastructure.drivens.repositories.CounterRepository;
import com.crmservice.crmservice.infrastructure.drivens.repositories.ICounterRepository;
import com.crmservice.crmservice.infrastructure.drivens.repositories.ICustomerRepository;
import com.crmservice.crmservice.infrastructure.drivens.repositories.IUserRepository;
import com.crmservice.crmservice.infrastructure.drivens.repositoryservices.CounterRepositoryService;
import com.crmservice.crmservice.infrastructure.drivens.repositoryservices.CustomerRepositoryService;
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
    private static final String ENDPOINT_URL = "https://s3.us-east-1.amazonaws.com";
    private static final String BUCKET_NAME = "crm-service-bucket";
    private static final String ACCESS_KEY = "AKIATQ6I476KLCKJMHOV";
    private static final String SECRET_KEY = "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";
    //TODO: move the constants to environment variables

    @Bean
    PasswordEncoder getBCryptEncoder() {
        return new BCryptPasswordEncoder(ENCODER_STRENGTH, new SecureRandom());
    }

    @Bean
    public AmazonS3 getS3Client(){
        BasicAWSCredentials credentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);

        return AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(Regions.US_EAST_1)
                .build();
    }

    @Bean
    ITokenService getTokenService() {
        KeyPair keyPair = Keys.keyPairFor(Version.V2);
        return new PasetoTokenService(keyPair);
    }

    @Bean
    public IAmazonS3Service getAmazonS3Service(@Autowired AmazonS3 amazonS3Client) {
        return new AmazonS3Service(amazonS3Client, BUCKET_NAME, ENDPOINT_URL);
    }

    @Bean
    public RedisTemplate<String, Integer> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Integer> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        return template;
    }

    @Bean
    IUserRepositoryService getUserRepositoryService(@Autowired IUserRepository userRepository) {
        return new UserRepositoryService(userRepository);
    }

    @Bean
    ICustomerRepositoryService getCustomerRepositoryService(@Autowired ICustomerRepository customerRepository,
                                                            @Autowired IAmazonS3Service amazonS3Service) {
        return new CustomerRepositoryService(customerRepository, amazonS3Service);
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
