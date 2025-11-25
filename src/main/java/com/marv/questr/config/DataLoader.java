package com.marv.questr.config;

import com.marv.questr.domain.entities.User;
import com.marv.questr.domain.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;

    @Override
    public void run(String... args) {
        userRepository.findByEmail("test@example.com")
                .ifPresentOrElse(
                        user -> {
                            // user already exists, do nothing
                        },
                        () -> {
                            User user = User.builder()
                                    // ❌ no .id(...) here
                                    .username("testuser")
                                    .email("test@example.com")
                                    .password("password123") // plain for now
                                    .build();

                            userRepository.save(user); // Hibernate generates UUID
                        }
                );
    }
}

