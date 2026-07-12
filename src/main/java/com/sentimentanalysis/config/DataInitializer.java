package com.sentimentanalysis.config;

import com.sentimentanalysis.entity.User;
import com.sentimentanalysis.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initDatabase(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (!userRepository.existsByUsername("admin")) {
                User admin = new User();
                admin.setFullName("Admin User");
                admin.setUsername("admin");
                admin.setEmail("admin@example.com");
                admin.setPassword(passwordEncoder.encode("Admin@123"));
                admin.setRole("ROLE_ADMIN");
                admin.setEnabled(true);
                userRepository.save(admin);
            }
            if (!userRepository.existsByUsername("jane")) {
                User user = new User();
                user.setFullName("Jane Doe");
                user.setUsername("jane");
                user.setEmail("jane@mail.com");
                user.setPassword(passwordEncoder.encode("Password@123"));
                user.setRole("ROLE_USER");
                user.setEnabled(true);
                userRepository.save(user);
            }
        };
    }
}
