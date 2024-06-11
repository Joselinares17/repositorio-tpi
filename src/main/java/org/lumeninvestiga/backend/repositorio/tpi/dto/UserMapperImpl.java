package org.lumeninvestiga.backend.repositorio.tpi.dto;

import org.lumeninvestiga.backend.repositorio.tpi.entities.user.User;

public class UserMapperImpl implements UserMapper{
    @Override
    public UserRegistrationRequest toUserRegisterRequest(User user) {
        return new UserRegistrationRequest(
                user.getUserDetail().getName(),
                user.getUserDetail().getLastName(),
                user.getUserDetail().getEmailAddress(),
                user.getUserDetail().getPassword()
        );
    }

    @Override
    public UserDTO toUserProfileDTO(User user) {
        return new UserDTO(
                user.getUserDetail().getName(),
                user.getUserDetail().getLastName(),
                user.getUserDetail().getEmailAddress()
        );
    }

}
