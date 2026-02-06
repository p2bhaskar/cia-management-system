package com.cia.management_system.security;



import com.cia.management_system.model.Role;
import com.cia.management_system.model.User;
import com.cia.management_system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Custom UserDetailsService Implementation
 * Loads user data from database for Spring Security
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Find user by username or email
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "User not found with username: " + username
                ));

        return build(user);
    }

    /**
     * Load user by ID (useful for some operations)
     */
    @Transactional
    public UserDetails loadUserById(Long id) throws UsernameNotFoundException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "User not found with id: " + id
                ));

        return build(user);
    }

    /**
     * Build Spring Security UserDetails from User entity
     */
    public static UserDetails build(User user) {
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.getEnabled(),
                true, // accountNonExpired
                true, // credentialsNonExpired
                user.getAccountNonLocked(),
                mapRolesToAuthorities(user.getRoles())
        );
    }

    /**
     * Convert Role enum to GrantedAuthority
     */
    private static Collection<? extends GrantedAuthority> mapRolesToAuthorities(Set<Role> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getAuthority()))
                .collect(Collectors.toList());
    }
}
