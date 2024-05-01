package com.example.campuscart.controllers.userservice;

import com.example.campuscart.dto.userservice.*;
import com.example.campuscart.entities.userservice.User;
import com.example.campuscart.exceptions.AlreadyExistsException;
import com.example.campuscart.exceptions.NotFoundException;
import com.example.campuscart.services.userservice.AddressService;
import com.example.campuscart.services.userservice.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private AddressService addressService;
    @PostMapping("/public/user/register")
    public ResponseEntity<?> registerUser(@RequestBody @Valid UserRegisterRequest request, BindingResult bindingResult){
        log.info("Register User request Received");
        if(bindingResult.hasFieldErrors()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(bindingResult.getFieldError());
        }
        try {
            UserProfileResponse response = userService.registerUser(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (AlreadyExistsException alreadyExistsException){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(alreadyExistsException.getMessage());
        } catch (Exception e){
            log.error("User Registeration failed due to:" + e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @GetMapping("/user/profile/{accountId}")
    public ResponseEntity<?> getUserProfile(@PathVariable("accountId") Long accountId){
        log.info("Get User Profile request Received for userId:" + accountId);
        try {
            UserProfileResponse response = userService.getUserProfile(accountId);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (NotFoundException notFoundException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(notFoundException.getMessage());
        } catch (Exception e){
            log.error("User Profile Fetch Failed due to:" + e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @PostMapping("/user/address/{accountId}")
    public ResponseEntity<?> createAddress(@PathVariable("accountId") Long accountId, @RequestBody @Valid AddressRequest request, BindingResult bindingResult){
        log.info("Create Address Request Received for userId:" + accountId);
        if(bindingResult.hasFieldErrors()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(bindingResult.getFieldError());
        }
        try {
            AddressResponse response = addressService.addAddress(accountId, request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (NotFoundException notFoundException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(notFoundException.getMessage());
        } catch (Exception e){
            log.error("Address Creation failed due to:" + e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @GetMapping("/user/address/{accountId}")
    public ResponseEntity<?> getUserAddresses(@PathVariable("accountId") Long accountId){
        log.info("Get User Addresses Request Received for userId" + accountId);
        try {
            AllAddressesResponse response = addressService.getUserAddresses(accountId);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e){
            log.error("User Profile Fetch Failed due to:" + e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @PutMapping("/user/address/{addressId}")
    public ResponseEntity<?> updateUserAddress(@PathVariable("addressId") Long addressId, @RequestBody @Valid AddressRequest request, BindingResult bindingResult){
        log.info("Update User Address Request Received for addressId:" + addressId);
        if(bindingResult.hasFieldErrors()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(bindingResult.getFieldError());
        }
        try {
            AddressResponse response = addressService.updateAddress(addressId, request);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (NotFoundException notFoundException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(notFoundException.getMessage());
        } catch (Exception e){
            log.error("Address Updation failed due to:" + e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @DeleteMapping("/user/address/{addressId}")
    public ResponseEntity<?> deleteUserAddress(@PathVariable("addressId") Long addressId){
        log.info("Delete User Address Request Received for addressId:" + addressId);
        try {
            addressService.deleteAddress(addressId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (NotFoundException notFoundException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(notFoundException.getMessage());
        } catch (Exception e){
            log.error("Address Deletion failed due to:" + e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @DeleteMapping("/user/{accountId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable("accountId") Long accountId){
        log.info("Request received to delete user:" + accountId);
        try{
            userService.deleteUser(accountId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (NotFoundException notFoundException){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(notFoundException.getMessage());
        } catch (Exception e){
            log.error("Delete User Request failed due to:" + e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @GetMapping("/users/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllUsers(@RequestParam(value = "page", defaultValue = "0") Integer page, @RequestParam(value = "size", defaultValue = "10") Integer size){
        log.info("Request received to fetch all users");
        try{
            Page<User> response = userService.getAllUsers(page, size);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e){
            log.error("Get All Users Request failed due to:" + e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
