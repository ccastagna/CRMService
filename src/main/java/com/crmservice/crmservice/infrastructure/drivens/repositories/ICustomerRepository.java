package com.crmservice.crmservice.infrastructure.drivens.repositories;

import com.crmservice.crmservice.infrastructure.drivens.repositorydtos.CustomerRepositoryDTO;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ICustomerRepository extends MongoRepository<CustomerRepositoryDTO, String> {
}
