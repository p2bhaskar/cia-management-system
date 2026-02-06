package com.cia.management_system.config;

import com.cia.management_system.model.Role;
import com.cia.management_system.model.User;
import com.cia.management_system.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner createAdmin() {
        return args -> {

            String adminUsername = "admin";

            // Check if admin exists
            if (userRepository.findByUsername(adminUsername).isPresent()) {
                System.out.println("Admin already exists — skipping creation");
                return;
            }

            User admin = new User();
            admin.setUsername(adminUsername);
            admin.setEmail("admin@cia.com");
            admin.setFullName("System Administrator");
            admin.setPassword(passwordEncoder.encode("admin123"));

            // Add ADMIN role
            admin.addRole(Role.ADMIN);

            userRepository.save(admin);

            System.out.println("=================================");
            System.out.println(" ADMIN CREATED ");
            System.out.println(" Username: admin");
            System.out.println(" Password: admin123");
            System.out.println("=================================");
        };
    }
}
