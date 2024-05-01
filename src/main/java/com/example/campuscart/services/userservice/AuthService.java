package com.example.campuscart.services.userservice;

import com.example.campuscart.dto.userservice.LoginRequest;
import com.example.campuscart.jwt.JwtAuthenticationHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtAuthenticationHelper jwtAuthenticationHelper;
    @Autowired
    private UserService userService;
    public String doLogin(LoginRequest loginRequest) throws BadCredentialsException {
        this.doAuthentication(loginRequest.getEmail(), loginRequest.getPassword());
        String token = jwtAuthenticationHelper.generateToken(loginRequest.getEmail());
        return token;
    }
    private void doAuthentication(String email, String password) throws BadCredentialsException {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, password);
        try{
            authenticationManager.authenticate(authenticationToken);
        } catch (AuthenticationException exception){
            throw new BadCredentialsException("Invalid Username or Password");
        }
    }
}
