package com.cts.edusphere.config.security;

import com.cts.edusphere.enums.Status;
import com.cts.edusphere.modules.user.User;
import com.cts.edusphere.repositories.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Custom implementation of Spring Security's {@link UserDetailsService} for the EduSphere
 * application.
 *
 * <p>This service is called by the Spring Security authentication infrastructure whenever
 * credential-based authentication is performed (e.g. during login via
 * {@link org.springframework.security.authentication.dao.DaoAuthenticationProvider}).
 * It loads a {@link User} entity from the database using the supplied email address and
 * translates it into a Spring Security {@link UserDetails} object.
 *
 * <p>Account activation state is derived from the user's {@link Status}: only users with
 * {@code Status.ACTIVE} are treated as enabled and non-locked. All other account flags
 * ({@code accountNonExpired}, {@code credentialsNonExpired}) are unconditionally set to
 * {@code true}.
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    /**
     * Locates a user by their email address and returns a populated {@link UserDetails} object.
     *
     * <p>The {@code username} parameter is treated as the user's email address. If no matching
     * user is found in the database, a {@link UsernameNotFoundException} is thrown, which
     * Spring Security will translate into an authentication failure.
     *
     * <p>The returned {@link UserDetails} reflects the user's active status for both the
     * {@code enabled} and {@code accountNonLocked} flags. Granted authorities are built by
     * delegating to {@link #getAuthorities(User)}.
     *
     * @param email the email address identifying the user (used as the Spring Security username)
     * @return a fully populated {@link UserDetails} instance for the located user
     * @throws UsernameNotFoundException if no user with the given email exists in the data store
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User with email " + email + " not found"));
        boolean isActive = user.getStatus() == Status.ACTIVE;
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                isActive, // enabled
                true, // accountNonExpired
                true, // credentialsNonExpired
                isActive, // accountNonLocked
                getAuthorities(user));
    }

    /**
     * Converts the roles assigned to the given {@link User} into a collection of Spring Security
     * {@link GrantedAuthority} instances.
     *
     * <p>Each role is prefixed with {@code "ROLE_"} to conform to the Spring Security convention
     * expected by role-based access expressions such as {@code hasRole("ADMIN")}.
     *
     * @param user the {@link User} entity whose roles are to be converted
     * @return a collection of {@link SimpleGrantedAuthority} objects representing the user's roles
     */
    private Collection<? extends GrantedAuthority> getAuthorities(User user) {
        return user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                .collect(Collectors.toList());
    }
}
