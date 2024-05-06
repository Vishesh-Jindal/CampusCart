package com.example.campuscart.services.userservice;

import com.example.campuscart.dto.userservice.RoleRequest;
import com.example.campuscart.dto.userservice.RoleResponse;
import com.example.campuscart.dto.userservice.UserProfileResponse;
import com.example.campuscart.dto.userservice.UserRegisterRequest;
import com.example.campuscart.entities.userservice.User;
import com.example.campuscart.enums.Gender;
import com.example.campuscart.exceptions.AlreadyExistsException;
import com.example.campuscart.exceptions.NotFoundException;
import com.example.campuscart.repositories.userservice.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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
    public void testRegisterUser(){
        UserRegisterRequest request = new UserRegisterRequest();
        request.setEmail("test@example.com");
        request.setPassword("password");
        request.setName("Test User");
        request.setDob(LocalDate.parse("1990-01-01"));
        request.setGender(Gender.MALE);

        Mockito.when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");
        Mockito.when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
        Mockito.when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserProfileResponse actualUser = userService.registerUser(request);

        Assertions.assertEquals(request.getName(), actualUser.getName());
        Assertions.assertEquals(request.getEmail(), actualUser.getEmail());
        Assertions.assertEquals(request.getDob(), actualUser.getDob());
        Assertions.assertEquals(request.getGender(), actualUser.getGender());

        Mockito.verify(userRepository).save(any(User.class));
    }
    @Test
    public void testRegisterUser_UserAlreadyExists(){
        UserRegisterRequest request = new UserRegisterRequest();
        request.setEmail("test@example.com");
        request.setPassword("password");
        request.setName("Test User");
        request.setDob(LocalDate.parse("1990-01-01"));
        request.setGender(Gender.MALE);

        User existingUser = new User();
        existingUser.setAccountId(123L);
        existingUser.setEmail("test@example.com");
        existingUser.setName("Test User");
        existingUser.setPassword("encodedPassword");
        existingUser.setDob(LocalDate.parse("1990-01-01"));
        existingUser.setGender(Gender.MALE);

        Mockito.when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(existingUser));
        Mockito.when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");
        Mockito.when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        AlreadyExistsException alreadyExistsException = Assertions.assertThrows(AlreadyExistsException.class, () -> userService.registerUser(request));
        Assertions.assertEquals("User with email:"+request.getEmail()+" is Already Present", alreadyExistsException.getMessage());
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
    @Test
    public void testUserProfile_ProfileNotFound(){
        Long accountId = 123L;
        User expectedUser = new User();
        expectedUser.setAccountId(accountId);
        expectedUser.setEmail("test@example.com");
        expectedUser.setPassword("password");
        expectedUser.setName("Test User");
        expectedUser.setDob(LocalDate.parse("1990-01-01"));
        expectedUser.setGender(Gender.MALE);

        Mockito.when(userRepository.findById(any(Long.class))).thenReturn(Optional.empty());
        NotFoundException notFoundException = Assertions.assertThrows(NotFoundException.class, () -> userService.getUserProfile(accountId));
        Assertions.assertEquals("User: "+accountId+" not found", notFoundException.getMessage());

        Mockito.verify(userRepository).findById(any(Long.class));
    }
    @Test
    public void testGrantRoles(){
        Long accountId = 123L;
        RoleRequest roleRequest = new RoleRequest();
        roleRequest.setRoles("ADMIN,SELLER");

        User existingUser = new User();
        existingUser.setAccountId(accountId);
        existingUser.setEmail("test@example.com");
        existingUser.setPassword("password");
        existingUser.setName("Test User");
        existingUser.setDob(LocalDate.parse("1990-01-01"));
        existingUser.setGender(Gender.MALE);
        existingUser.setRoles("");


        Mockito.when(userRepository.findById(accountId)).thenReturn(Optional.of(existingUser));
        Mockito.when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        RoleResponse roleResponse = userService.grantRoles(accountId, roleRequest);

        Assertions.assertEquals(roleRequest.getRoles(), roleResponse.getRoles());
        Assertions.assertEquals(accountId, roleResponse.getAccountId());

        Mockito.verify(userRepository).findById(any(Long.class));
        Mockito.verify(userRepository).save(any(User.class));
    }
    @Test
    public void testRevokeRoles(){
        Long accountId = 123L;
        RoleRequest roleRequest = new RoleRequest();
        roleRequest.setRoles("ADMIN");

        User existingUser = new User();
        existingUser.setAccountId(accountId);
        existingUser.setEmail("test@example.com");
        existingUser.setPassword("password");
        existingUser.setName("Test User");
        existingUser.setDob(LocalDate.parse("1990-01-01"));
        existingUser.setGender(Gender.MALE);
        existingUser.setRoles("ADMIN,SELLER");

        Mockito.when(userRepository.findById(accountId)).thenReturn(Optional.of(existingUser));
        Mockito.when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        RoleResponse roleResponse = userService.revokeRoles(accountId, roleRequest);

        Assertions.assertEquals("SELLER", roleResponse.getRoles());
        Assertions.assertEquals(accountId, roleResponse.getAccountId());

        Mockito.verify(userRepository).findById(any(Long.class));
        Mockito.verify(userRepository).save(any(User.class));
    }
    @Test
    public void testRevokeAllRoles(){
        Long accountId = 123L;
        User existingUser = new User();
        existingUser.setAccountId(accountId);
        existingUser.setEmail("test@example.com");
        existingUser.setPassword("password");
        existingUser.setName("Test User");
        existingUser.setDob(LocalDate.parse("1990-01-01"));
        existingUser.setGender(Gender.MALE);
        existingUser.setRoles("ADMIN,SELLER");


        Mockito.when(userRepository.findById(accountId)).thenReturn(Optional.of(existingUser));
        Mockito.when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        userService.revokeAllRoles(accountId);

        Assertions.assertEquals("", existingUser.getRoles());

        Mockito.verify(userRepository).findById(any(Long.class));
        Mockito.verify(userRepository).save(any(User.class));
    }
    @Test
    public void testDeleteUser() throws NotFoundException {
        Long accountId = 123L;
        User existingUser = new User();
        existingUser.setAccountId(accountId);
        existingUser.setEmail("test@example.com");
        existingUser.setPassword("password");
        existingUser.setName("Test User");
        existingUser.setDob(LocalDate.parse("1990-01-01"));
        existingUser.setGender(Gender.MALE);
        existingUser.setRoles("ADMIN,SELLER");

        Mockito.when(userRepository.findById(accountId)).thenReturn(Optional.of(existingUser));

        userService.deleteUser(accountId);

        Mockito.verify(userRepository).findById(any(Long.class));
        Mockito.verify(userRepository).delete(any(User.class));
    }
    @Test
    public void testGetAllUsers() {
        int page = 0;
        int size = 10;
        List<User> userList = new ArrayList<>();
        userList.add(new User());
        userList.add(new User());
        userList.add(new User());
        Page<User> userPage = new PageImpl<>(userList);
        Mockito.when(userRepository.findAll(any(Pageable.class))).thenReturn(userPage);

        Page<User> result = userService.getAllUsers(page, size);

        Assertions.assertEquals(userPage.getContent(), result.getContent());
        Assertions.assertEquals(userPage.getNumber(), result.getNumber());
        Assertions.assertEquals(userPage.getSize(), result.getSize());
        Assertions.assertEquals(userPage.getTotalElements(), result.getTotalElements());
        Assertions.assertEquals(userPage.getTotalPages(), result.getTotalPages());

        Mockito.verify(userRepository).findAll(any(Pageable.class));
    }
    @Test
    public void testGetUser(){
        Long accountId = 123L;
        User expectedUser = new User();
        expectedUser.setAccountId(accountId);
        expectedUser.setEmail("test@example.com");
        expectedUser.setPassword("password");
        expectedUser.setName("Test User");
        expectedUser.setDob(LocalDate.parse("1990-01-01"));
        expectedUser.setGender(Gender.MALE);

        Mockito.when(userRepository.findById(accountId)).thenReturn(Optional.of(expectedUser));

        User actualUser = userService.getUser(accountId);

        Assertions.assertEquals(expectedUser.getAccountId(), actualUser.getAccountId());
        Assertions.assertEquals(expectedUser.getName(), actualUser.getName());
        Assertions.assertEquals(expectedUser.getEmail(), actualUser.getEmail());
        Assertions.assertEquals(expectedUser.getPassword(), actualUser.getPassword());
        Assertions.assertEquals(expectedUser.getDob(), actualUser.getDob());
        Assertions.assertEquals(expectedUser.getGender(), actualUser.getGender());

        Mockito.verify(userRepository).findById(accountId);
    }
    @Test
    public void testGetUser_UserNotFound(){
        Long accountId = 123L;

        Mockito.when(userRepository.findById(accountId)).thenReturn(Optional.empty());

        NotFoundException notFoundException = Assertions.assertThrows(NotFoundException.class, () -> userService.getUser(accountId));
        Assertions.assertEquals("User: "+accountId+" not found", notFoundException.getMessage());

        Mockito.verify(userRepository).findById(accountId);
    }
}

