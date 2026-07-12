package com.sentimentanalysis.service;

import com.sentimentanalysis.dto.UserDto;
import com.sentimentanalysis.entity.User;
import org.springframework.lang.NonNull;

import java.util.List;

public interface UserService {
    UserDto getByUsername(String username);
    UserDto getById(@NonNull Long id);
    User updateProfile(String username, UserDto dto);
    void changePassword(String username, String currentPassword, String newPassword);
    List<UserDto> findAllUsers();
}
