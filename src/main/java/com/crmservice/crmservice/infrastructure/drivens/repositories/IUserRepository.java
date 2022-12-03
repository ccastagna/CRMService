package com.crmservice.crmservice.infrastructure.drivens.repositories;

import com.crmservice.crmservice.infrastructure.drivens.repositorydtos.UserRepositoryDTO;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface IUserRepository extends MongoRepository<UserRepositoryDTO, String> {
    Optional<UserRepositoryDTO> findByUsername(String username);
}
