package com.crmservice.crmservice.infrastructure.drivens.repositoryservices;

import com.crmservice.crmservice.domain.entities.User;
import com.crmservice.crmservice.domain.interfaces.IUserRepositoryService;
import com.crmservice.crmservice.infrastructure.drivens.repositories.IUserRepository;
import com.crmservice.crmservice.infrastructure.drivens.repositorydtos.RepositoryDTOState;

import java.util.Optional;

public class UserRepositoryService implements IUserRepositoryService {

    private IUserRepository userRepository;

    @Override
    public Optional<User> getUserByUsername(String requestUsername) {
        return this.userRepository.findById("user_" + requestUsername)
                .filter(userDTO -> userDTO.state() == RepositoryDTOState.ACTIVE)
                .map(userDTO -> userDTO.toEntity());
    }
}
