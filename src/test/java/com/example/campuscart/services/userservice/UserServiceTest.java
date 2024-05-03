package com.example.campuscart.services.userservice;

import com.example.campuscart.dto.userservice.UserProfileResponse;
import com.example.campuscart.entities.userservice.User;
import com.example.campuscart.enums.Gender;
import com.example.campuscart.repositories.userservice.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

public class UserServiceTest {
    @InjectMocks
    private UserService userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.initMocks(this);
    }
    @Test
    public void testGetUserProfile(){
        Long accountId = 123L;
        User expectedUser = new User();
        expectedUser.setAccountId(accountId);
        expectedUser.setEmail("test@example.com");
        expectedUser.setPassword("password");
        expectedUser.setName("Test User");
        expectedUser.setDob(LocalDate.parse("1990-01-01"));
        expectedUser.setGender(Gender.MALE);

        Mockito.when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(expectedUser));
        UserProfileResponse actualUser = userService.getUserProfile(accountId);

        Assertions.assertEquals(expectedUser.getAccountId(), actualUser.getAccountId());
        Assertions.assertEquals(expectedUser.getEmail(), actualUser.getEmail());
        Assertions.assertEquals(expectedUser.getName(), actualUser.getName());
        Assertions.assertEquals(expectedUser.getDob(), actualUser.getDob());
        Assertions.assertEquals(expectedUser.getGender(), actualUser.getGender());

        Mockito.verify(userRepository).findById(any(Long.class));
    }
}
