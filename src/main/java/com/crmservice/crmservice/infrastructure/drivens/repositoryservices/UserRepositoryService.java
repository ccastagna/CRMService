package com.crmservice.crmservice.infrastructure.drivens.repositoryservices;

import com.crmservice.crmservice.domain.entities.User;
import com.crmservice.crmservice.domain.interfaces.IUserRepositoryService;
import com.crmservice.crmservice.infrastructure.drivens.repositories.IUserRepository;
import com.crmservice.crmservice.domain.enums.UserState;
import com.crmservice.crmservice.infrastructure.drivens.repositorydtos.UserRepositoryDTO;
import com.crmservice.crmservice.infrastructure.drivens.repositoryservices.examplebuilders.UserRepositoryDTOExampleBuilder;
import com.mongodb.Function;
import org.springframework.data.domain.Example;

import java.util.Optional;

public class UserRepositoryService implements IUserRepositoryService {

    private IUserRepository userRepository;

    public UserRepositoryService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> getUserByUsername(String requestUsername) {
        return this.userRepository.findByUsername(requestUsername)
                .filter(userDTO -> userDTO.state() == UserState.ACTIVE).map(userDTO -> userDTO.toEntity());
    }

    @Override
    public User createUser(User newUser) {
        return this.userRepository.save(UserRepositoryDTO.from(newUser)).toEntity();
    }
}
