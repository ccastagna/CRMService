package com.crmservice.crmservice.infrastructure.drivens.repositoryservices;

import com.crmservice.crmservice.domain.entities.User;
import com.crmservice.crmservice.domain.interfaces.IUserRepositoryService;
import com.crmservice.crmservice.infrastructure.drivens.repositories.IUserRepository;
import com.crmservice.crmservice.domain.enums.UserState;
import com.crmservice.crmservice.infrastructure.drivens.repositorydtos.UserRepositoryDTO;

import java.util.Optional;

public class UserRepositoryService implements IUserRepositoryService {

    private final IUserRepository userRepository;

    public UserRepositoryService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> getActiveUserByUsername(String requestUsername) {
        return this.userRepository.findByUsername(requestUsername)
                .filter(userDTO -> userDTO.state() == UserState.ACTIVE).map(UserRepositoryDTO::toEntity);
    }

    @Override
    public Optional<Object> getUserByUsername(String requestUsername) {
        return this.userRepository.findByUsername(requestUsername)
                .map(UserRepositoryDTO::toEntity);
    }

    @Override
    public User saveUser(User newUser) {
        return this.userRepository.save(UserRepositoryDTO.from(newUser)).toEntity();
    }

}
