package org.lumeninvestiga.backend.repositorio.tpi.services;

import lombok.extern.slf4j.Slf4j;
import org.lumeninvestiga.backend.repositorio.tpi.dto.mapper.UserMapper;
import org.lumeninvestiga.backend.repositorio.tpi.dto.request.UserLoginRequest;
import org.lumeninvestiga.backend.repositorio.tpi.dto.request.UserRegistrationRequest;
import org.lumeninvestiga.backend.repositorio.tpi.dto.request.UserUpdateRequest;
import org.lumeninvestiga.backend.repositorio.tpi.dto.response.UserResponse;
import org.lumeninvestiga.backend.repositorio.tpi.entities.user.User;
import org.lumeninvestiga.backend.repositorio.tpi.exceptions.InvalidResourceException;
import org.lumeninvestiga.backend.repositorio.tpi.exceptions.NotFoundResourceException;
import org.lumeninvestiga.backend.repositorio.tpi.repositories.UserRepository;
import org.lumeninvestiga.backend.repositorio.tpi.utils.Utility;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
@Slf4j
@Service
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public Optional<UserResponse> saveUser(UserRegistrationRequest request) {
        Optional<User> userOptional = userRepository.findByEmailAddress(request.emailAddress());
        if(userOptional.isPresent()) {
            throw new InvalidResourceException("invalid request");
        }
        User userRequest = new User();
        userRequest.setUsername(request.username());
        userRequest.setPassword(request.password());
        userRequest.getUserDetail().setName(request.name());
        userRequest.getUserDetail().setLastName(request.lastName());
        userRequest.getUserDetail().setEmailAddress(request.emailAddress());
        log.info("save data user {}", userRequest);
        userRepository.save(userRequest);
        return Optional.of(UserMapper.INSTANCE.toUserResponse(userRequest));
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers(Pageable pageable) {
        int page = Utility.getCurrentPage(pageable);
        log.info("query users by page {}", page);
        return userRepository.findAll(PageRequest.of(page, pageable.getPageSize())).stream()
                .map(UserMapper.INSTANCE::toUserResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserResponse> getUserById(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        if(userOptional.isEmpty()) {
            throw new NotFoundResourceException("requested resource was not found");
        }
        log.info("get user by id {}", userOptional.get());
        return Optional.of(UserMapper.INSTANCE.toUserResponse(userOptional.get()));
    }

    @Override
    @Transactional
    public Optional<UserResponse> updateUserById(Long id, UserUpdateRequest request) {
        Optional<User> userOptional = userRepository.findById(id);
        userOptional.ifPresentOrElse(userDb -> {
            userDb.getUserDetail().setName(request.name());
            userDb.getUserDetail().setLastName(request.lastName());
            userDb.getUserDetail().setEmailAddress(request.emailAddress());
        },
                () -> new NotFoundResourceException("requested resource was not found")
        );
        log.info("update user data {}", userOptional.get());
        return Optional.of(UserMapper.INSTANCE.toUserResponse(userOptional.get()));
    }

    @Override
    @Transactional
    public boolean deleteUserById(Long id) {
        if(!existUserById(id)) {
            return false;
        }
        userRepository.deleteById(id);
        log.info("user deleted");
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existUserById(Long id) {
        return userRepository.existsById(id);
    }

    @Override
    public boolean existUserByUsername(String username) {
        return userRepository.existsByUsername(username);
    }
}
