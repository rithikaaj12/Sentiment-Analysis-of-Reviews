package com.sentimentanalysis.controller;

import com.sentimentanalysis.dto.PasswordChangeRequest;
import com.sentimentanalysis.dto.UserDto;
import com.sentimentanalysis.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> listUsers() {
        return ResponseEntity.ok(userService.findAllUsers());
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> currentUser(Authentication authentication) {
        return ResponseEntity.ok(userService.getByUsername(authentication.getName()));
    }

    @PutMapping("/me")
    public ResponseEntity<UserDto> updateProfile(Authentication authentication, @Valid @RequestBody UserDto userDto) {
        userService.updateProfile(authentication.getName(), userDto);
        return ResponseEntity.ok(userService.getByUsername(userDto.getUsername()));
    }

    @PutMapping("/me/password")
    public ResponseEntity<Void> changePassword(Authentication authentication, @Valid @RequestBody PasswordChangeRequest request) {
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            return ResponseEntity.badRequest().build();
        }
        userService.changePassword(authentication.getName(), request.getCurrentPassword(), request.getNewPassword());
        return ResponseEntity.noContent().build();
    }
}
