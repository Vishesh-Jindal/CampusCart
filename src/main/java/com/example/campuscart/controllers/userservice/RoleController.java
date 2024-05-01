package com.example.campuscart.controllers.userservice;

import com.example.campuscart.dto.userservice.RoleRequest;
import com.example.campuscart.dto.userservice.RoleResponse;
import com.example.campuscart.exceptions.NotFoundException;
import com.example.campuscart.services.userservice.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@PreAuthorize("hasRole('ADMIN')")
public class RoleController {
    @Autowired
    private UserService userService;
    @PutMapping("/grant/roles/{accountId}")
    public ResponseEntity<?> grantRoles(@PathVariable("accountId") Long accountId, @RequestBody @Valid RoleRequest roleRequest, BindingResult bindingResult){
        // roles will be replaced
        log.info("Request received to add roles:"+roleRequest.getRoles()+" for user:"+accountId);
        if(bindingResult.hasFieldErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(bindingResult.getFieldError());
        }
        try{
            RoleResponse response = userService.grantRoles(accountId, roleRequest);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (NotFoundException notFoundException){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(notFoundException.getMessage());
        } catch (Exception e){
            log.error("Grant Roles Request failed due to:" + e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @PutMapping("/revoke/roles/{accountId}")
    public ResponseEntity<?> revokeRoles(@PathVariable("accountId") Long accountId, @RequestBody @Valid RoleRequest roleRequest, BindingResult bindingResult){
        // only mentioned roles will be revoked
        log.info("Request received to revoke roles:"+roleRequest.getRoles()+" for user:"+accountId);
        if(bindingResult.hasFieldErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(bindingResult.getFieldError());
        }
        try{
            RoleResponse response = userService.revokeRoles(accountId, roleRequest);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (NotFoundException notFoundException){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(notFoundException.getMessage());
        } catch (Exception e){
            log.error("Revoke Roles Request failed due to:" + e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @DeleteMapping("/revoke/allRoles/{accountId}")
    public ResponseEntity<?> revokeAllRoles(@PathVariable("accountId") Long accountId){
        log.info("Request received to revoke all roles for user:" + accountId);
        try{
            userService.revokeAllRoles(accountId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (NotFoundException notFoundException){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(notFoundException.getMessage());
        } catch (Exception e){
            log.error("Revoke All Roles Request failed due to:" + e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
