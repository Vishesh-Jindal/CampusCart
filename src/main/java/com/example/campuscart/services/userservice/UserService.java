package com.example.campuscart.services.userservice;

import com.example.campuscart.dto.userservice.RoleRequest;
import com.example.campuscart.dto.userservice.RoleResponse;
import com.example.campuscart.dto.userservice.UserProfileResponse;
import com.example.campuscart.dto.userservice.UserRegisterRequest;
import com.example.campuscart.entities.userservice.User;
import com.example.campuscart.exceptions.AlreadyExistsException;
import com.example.campuscart.exceptions.NotFoundException;
import com.example.campuscart.repositories.userservice.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    public UserProfileResponse registerUser(UserRegisterRequest request) throws AlreadyExistsException{
        Optional<User> optionalUser = userRepository.findByEmail(request.getEmail());
        if(optionalUser.isPresent()){
            throw new AlreadyExistsException("User with email:"+request.getEmail()+" is Already Present");
        }
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(encodedPassword);
        user.setDob(request.getDob());
        user.setGender(request.getGender());
        User createdUser = userRepository.save(user);
        return UserProfileResponse.builder()
                .accountId(createdUser.getAccountId())
                .email(createdUser.getEmail())
                .name(createdUser.getName())
                .dob(createdUser.getDob())
                .gender(createdUser.getGender())
                .build();
    }
    public UserProfileResponse getUserProfile(Long accountId) throws NotFoundException {
        User user = this.getUser(accountId);
        return UserProfileResponse.builder()
                .accountId(user.getAccountId())
                .email(user.getEmail())
                .name(user.getName())
                .dob(user.getDob())
                .gender(user.getGender())
                .build();
    }
    public RoleResponse grantRoles(Long accountId, RoleRequest roleRequest) throws NotFoundException {
        User user = this.getUser(accountId);
        user.setRoles(roleRequest.getRoles());
        User updatedUser = userRepository.save(user);
        return RoleResponse.builder()
                .accountId(updatedUser.getAccountId())
                .roles(updatedUser.getRoles())
                .build();
    }
    public RoleResponse revokeRoles(Long accountId, RoleRequest roleRequest) throws NotFoundException {
        User user = this.getUser(accountId);
        Set<String> oldRoles = Set.of(user.getRoles().split(","));
        Set<String> toRevokeRoles = Set.of(roleRequest.getRoles().split(","));
        String updatedRoles = "";
        for (String role:oldRoles) {
            if(!toRevokeRoles.contains(role)){
                updatedRoles += role;
            }
        }
        user.setRoles(updatedRoles);
        User updatedUser = userRepository.save(user);
        return RoleResponse.builder()
                .accountId(updatedUser.getAccountId())
                .roles(updatedUser.getRoles())
                .build();
    }
    public void revokeAllRoles(Long accountId) throws NotFoundException {
        User user = this.getUser(accountId);
        user.setRoles("");
        userRepository.save(user);
    }
    public void deleteUser(Long accountId) throws NotFoundException {
        User user = this.getUser(accountId);
        userRepository.delete(user);
    }
    public Page<User> getAllUsers(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> users = userRepository.findAll(pageable);
        return users;
    }
    public User getUser(Long accountId) throws NotFoundException {
        Optional<User> optionalUser = userRepository.findById(accountId);
        if(!optionalUser.isPresent()){
            throw new NotFoundException("User: "+accountId+" not found");
        }
        return optionalUser.get();
    }
}
