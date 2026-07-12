package com.sentimentanalysis.service.impl;

import com.sentimentanalysis.dto.UserDto;
import com.sentimentanalysis.entity.User;
import com.sentimentanalysis.exception.BadRequestException;
import com.sentimentanalysis.repository.UserRepository;
import com.sentimentanalysis.service.UserService;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDto getByUsername(String username) {
        return userRepository.findByUsername(username)
            .map(this::toDto)
            .orElseThrow(() -> new BadRequestException("User not found"));
    }

    @Override
    public UserDto getById(@NonNull Long id) {
        return userRepository.findById(id)
            .map(this::toDto)
            .orElseThrow(() -> new BadRequestException("User not found"));
    }

    @Override
    public User updateProfile(String username, UserDto dto) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new BadRequestException("User not found"));

        if (!user.getUsername().equals(dto.getUsername()) && userRepository.existsByUsername(dto.getUsername())) {
            throw new BadRequestException("Username is already taken");
        }
        if (!user.getEmail().equals(dto.getEmail()) && userRepository.existsByEmail(dto.getEmail())) {
            throw new BadRequestException("Email is already registered");
        }

        user.setFullName(dto.getFullName());
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        return userRepository.save(user);
    }

    @Override
    public void changePassword(String username, String currentPassword, String newPassword) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new BadRequestException("User not found"));

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new BadRequestException("Current password is incorrect");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    public List<UserDto> findAllUsers() {
        return userRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    private UserDto toDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setFullName(user.getFullName());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        dto.setEnabled(user.isEnabled());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setTotalReviews(user.getReviews() != null ? user.getReviews().size() : 0);
        return dto;
    }
}
