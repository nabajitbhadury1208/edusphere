package com.cts.edusphere.services.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.cts.edusphere.common.dto.auth.RegisterRequest;
import com.cts.edusphere.common.dto.user.UserRequestDto;
import com.cts.edusphere.common.dto.user.UserResponseDto;
import com.cts.edusphere.enums.Role;
import com.cts.edusphere.enums.Status;
import com.cts.edusphere.exceptions.genericexceptions.ResourceNotFoundException;
import com.cts.edusphere.modules.User;
import com.cts.edusphere.repositories.UserRepository;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.hibernate.type.descriptor.jdbc.UuidAsBinaryJdbcType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private PasswordEncoder passwordEncoder;

  @InjectMocks
  private UserServiceImpl userServiceImpl;

  private UserRequestDto userRequestDto;
  private UserResponseDto userResponseDto;
  private RegisterRequest registerRequest;
  private UUID userId;
  private User user;

  @BeforeEach
  void setUp() {
    userId = UUID.randomUUID();

    user = User.builder()
      .id(userId)
      .name("Rohan Das")
      .email("rohandas@gmail.com")
      .roles(Set.of(Role.ADMIN))
      .phone("1234567890")
      .status(Status.ACTIVE)
      .password("egiojgoiwjgoierkoi3535")
      .build();
  }

  @Test
  void testGetUsers_Success() {
    List<User> expectedUsers = List.of(user);

    when(userRepository.findAll()).thenReturn(expectedUsers);

    List<User> returnedUsers = userServiceImpl.getAllUsers();

    assertEquals(expectedUsers.size(), returnedUsers.size());
    assertEquals(
      expectedUsers.get(0).getEmail(),
      returnedUsers.get(0).getEmail()
    );
    assertEquals(expectedUsers, returnedUsers);

    verify(userRepository, times(1)).findAll();
  }

  @Test
  void testGetUsers_EmptyList() {
    List<User> expectedUsers = List.of();

    when(userRepository.findAll()).thenReturn(List.of());

    List<User> returnedUsers = userServiceImpl.getAllUsers();

    assertEquals(expectedUsers.size(), returnedUsers.size());
    assertTrue(expectedUsers.size() == 0);
    assertTrue(returnedUsers.size() == 0);
    // assertEquals(expectedUsers.get(0).getEmail(), returnedUsers.get(0).getEmail());
    assertEquals(expectedUsers, returnedUsers);

    verify(userRepository, times(1)).findAll();
  }

  @Test
  void testGetUserById_Sucess() {
    when(userRepository.findById(userId)).thenReturn(Optional.of(user));

    User returnedUser = userServiceImpl.getUserById(userId);

    assertEquals(user, returnedUser);
    assertEquals(user.getEmail(), returnedUser.getEmail());

    verify(userRepository, times(1)).findById(userId);
  }

  @Test
  void testGetUserById_NotFound() {
    when(userRepository.findById(userId)).thenReturn(Optional.empty());

    ResourceNotFoundException rsnfe = assertThrows(
      ResourceNotFoundException.class,
      () -> {
        userServiceImpl.getUserById(userId);
      }
    );

    assertEquals("User with id " + userId + " not found", rsnfe.getMessage());

    verify(userRepository, times(1)).findById(userId);
  }

  @Test
  void testCreateUser_Success() {
    when(userRepository.save(user)).thenReturn(user);

    User returnedUser = userServiceImpl.createUser(user);

    assertEquals(user, returnedUser);
    
    verify(userRepository, times(1)).save(user);
  }

  @Test
  void testCreateUser_Failure() {
    when(userRepository.save(null)).thenThrow(new RuntimeException("Database "));

    User returnedUser = userServiceImpl.createUser(user);

    assertEquals(user, returnedUser);
    
    verify(userRepository, times(1)).save(user);
  }
}
