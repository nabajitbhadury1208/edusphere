package com.cts.edusphere.services.user;

import com.cts.edusphere.common.dto.RegisterRequest;
import com.cts.edusphere.exceptions.genericexceptions.PasswordCannotBeChangedException;
import com.cts.edusphere.exceptions.genericexceptions.ResourceNotFoundException;
import com.cts.edusphere.exceptions.genericexceptions.UserNotCreatedException;
import com.cts.edusphere.modules.User;
import com.cts.edusphere.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public User getUserById(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " not found"));
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public void deleteUserById(UUID id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User with id " + id + " not found");
        }
        userRepository.deleteById(id);
    }

    public User updateUserById(UUID id, User updatedUser) {
        return userRepository.findById(id).map(existingUser -> {
            existingUser.setName(updatedUser.getName());
            existingUser.setEmail(updatedUser.getEmail());
            existingUser.setPhone(updatedUser.getPhone());
            return userRepository.save(existingUser);
        }).orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " not found"));
    }

    boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    Optional <User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional
    public User registerUser(RegisterRequest request) {

        try {
            if (userRepository.existsByEmail(request.email())) {
                throw new IllegalArgumentException("Email already in use");
            }
            User user = User.builder().name(request.name()).email(request.email()).phone(request.phone()).password(passwordEncoder.encode(request.password())).role(request.role()).build();
            return userRepository.save(user);
        } catch (Exception e) {
            throw new UserNotCreatedException("Failed to create user: " + e.getMessage());
        }
    }

    @Transactional
    public void changePassword(UUID userId, String currentPassword, String newPassword) {
        try {
            User user = getUserById(userId);
            if(user == null){
                throw new ResourceNotFoundException("User with id " + userId + " not found");
            }
            if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
                throw new IllegalArgumentException("Current password is incorrect");
            }

            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
        } catch (Exception e) {
            throw new PasswordCannotBeChangedException("Failed to change password: " + e.getMessage());
        }
    }
}
