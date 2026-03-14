package com.cts.edusphere.services.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.cts.edusphere.common.dto.auth.RegisterRequest;
import com.cts.edusphere.common.dto.user.UserRequestDto;
import com.cts.edusphere.config.security.UserPrincipal;
import com.cts.edusphere.enums.Role;
import com.cts.edusphere.enums.Status;
import com.cts.edusphere.exceptions.genericexceptions.InsufficientPermissionException;
import com.cts.edusphere.exceptions.genericexceptions.PasswordCannotBeChangedException;
import com.cts.edusphere.exceptions.genericexceptions.ResourceNotFoundException;
import com.cts.edusphere.exceptions.genericexceptions.UserNotCreatedException;
import com.cts.edusphere.modules.user.User;
import com.cts.edusphere.repositories.user.UserRepository;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private UUID userId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        user = User.builder()
                .id(userId)
                .name("Test User")
                .email("test@cts.com")
                .password("encoded_pass")
                .roles(Set.of(Role.STUDENT))
                .status(Status.ACTIVE)
                .build();
    }

    @Test
    void getAllUsers_ShouldReturnList() {
        when(userRepository.findAll()).thenReturn(Collections.singletonList(user));
        
        List<User> result = userService.getAllUsers();
        
        assertEquals(1, result.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void getUserById_ShouldReturnUser_WhenExists() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        
        User result = userService.getUserById(userId);
        
        assertEquals("test@cts.com", result.getEmail());
    }

    @Test
    void getUserById_ShouldThrowException_WhenNotFound() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        
        assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(userId));
    }

    @Test
    void registerUser_ShouldSaveUser_WhenEmailIsUnique() {
        RegisterRequest req = new RegisterRequest("New", "new@cts.com", "12345", "pass", Set.of(Role.STUDENT));
        when(userRepository.existsByEmail(req.email())).thenReturn(false);
        when(passwordEncoder.encode(req.password())).thenReturn("hashed");
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        User saved = userService.registerUser(req);

        assertEquals(req.email(), saved.getEmail());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void registerUser_ShouldThrowUserNotCreatedException_WhenEmailExists() {
        RegisterRequest req = new RegisterRequest("New", "existing@cts.com", "12345", "pass", null);
        when(userRepository.existsByEmail(req.email())).thenReturn(true);

        UserNotCreatedException ex = assertThrows(UserNotCreatedException.class, 
            () -> userService.registerUser(req));
        assertTrue(ex.getMessage().contains("Email existing@cts.com is already in use"));
    }

    @Test
    void updateUserById_AsAdmin_ShouldAllowFullUpdate() {
        UserRequestDto req = new UserRequestDto(userId, "Admin Update", null, Set.of(Role.ADMIN), Status.INACTIVE);
        UserPrincipal admin = new UserPrincipal(UUID.randomUUID(), "Admin", 
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN")));

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        User updated = userService.updateUserById(userId, req, admin);

        assertEquals("Admin Update", updated.getName());
        assertTrue(updated.getRoles().contains(Role.ADMIN));
        assertEquals(Status.INACTIVE, updated.getStatus());
    }

    @Test
    void updateUserById_AsSelf_ShouldRestrictRolesAndStatus() {
        UserRequestDto req = new UserRequestDto(userId, "Self Update", null, Set.of(Role.ADMIN), null);
        UserPrincipal self = new UserPrincipal(userId, "Self", 
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_STUDENT")));

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        InsufficientPermissionException ex = assertThrows(InsufficientPermissionException.class, 
            () -> userService.updateUserById(userId, req, self));
        assertTrue(ex.getMessage().contains("Only administrators can modify roles"));
    }

    @Test
    void changePassword_ShouldSucceed_WhenCurrentPasswordMatches() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("current", user.getPassword())).thenReturn(true);
        when(passwordEncoder.encode("new")).thenReturn("new_encoded");

        userService.changePassword(userId, "current", "new");

        verify(userRepository).save(user);
        assertEquals("new_encoded", user.getPassword());
    }

    @Test
    void changePassword_ShouldThrowPasswordCannotBeChangedException_WhenCurrentPasswordIsWrong() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrong", user.getPassword())).thenReturn(false);

        PasswordCannotBeChangedException ex = assertThrows(PasswordCannotBeChangedException.class, 
            () -> userService.changePassword(userId, "wrong", "new"));
        assertTrue(ex.getMessage().contains("Current password is incorrect"));
    }

    @Test
    void updateUserStatus_ShouldPreventAdminFromDeactivatingSelf() {
        UserPrincipal admin = new UserPrincipal(userId, "Admin", 
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN")));
        
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, 
            () -> userService.updateUserStatus(userId, Status.INACTIVE, admin));
        assertTrue(ex.getMessage().contains("Admins cannot deactivate their own account"));
    }

    @Test
    void deleteUserById_ShouldInvokeDelete_WhenUserExists() {
        when(userRepository.existsById(userId)).thenReturn(true);
        
        userService.deleteUserById(userId);
        
        verify(userRepository, times(1)).deleteById(userId);
    }
}