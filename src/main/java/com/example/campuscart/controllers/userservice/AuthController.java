package com.example.campuscart.controllers.userservice;

import com.example.campuscart.constants.Constants;
import com.example.campuscart.dto.userservice.LoginRequest;
import com.example.campuscart.services.userservice.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Slf4j
@RestController
public class AuthController {
    @Autowired
    private AuthService authService;
    @PostMapping("/public/auth/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest loginRequest, BindingResult bindingResult, HttpServletResponse response){
        log.info("Login Request Received");
        if(bindingResult.hasFieldErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(bindingResult.getFieldError());
        }
        try{
            String token = authService.doLogin(loginRequest);
            Cookie cookie = new Cookie(Constants.AUTH_COOKIE, token);
            cookie.setMaxAge(Constants.COOKIE_VALIDITY);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            response.addCookie(cookie);
            return ResponseEntity.status(HttpStatus.OK).body(Constants.LOGIN_SUCCESS);
        } catch (BadCredentialsException badCredentialsException){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(badCredentialsException.getMessage());
        } catch (Exception e){
            log.error("Login Request failed due to:"+e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @DeleteMapping("/auth/logout/{accountId}")
    public ResponseEntity<?> logout(@PathVariable("accountId") Long accountId, HttpServletResponse response){
        log.info("Logout Request Received for user: " + accountId);
        try {
            Cookie cookie = new Cookie(Constants.AUTH_COOKIE, "");
            cookie.setMaxAge(-1);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            response.addCookie(cookie);
            return ResponseEntity.status(HttpStatus.OK).body(Constants.LOGOUT_SUCCESS);
        } catch (Exception e){
            log.error("Logout Request failed due to:"+e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
