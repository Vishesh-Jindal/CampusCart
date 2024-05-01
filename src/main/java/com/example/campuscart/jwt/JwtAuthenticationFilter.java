package com.example.campuscart.jwt;

import com.example.campuscart.constants.Constants;
import com.example.campuscart.entities.userservice.User;
import com.example.campuscart.exceptions.InvalidCookieException;
import com.example.campuscart.exceptions.InvalidTokenException;
import com.example.campuscart.services.userservice.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    JwtAuthenticationHelper jwtAuthenticationHelper;
    @Autowired
    CustomUserDetailsService userDetailsService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Cookie[] cookies = request.getCookies();
        Cookie authCookie = null;
        if(cookies != null){
            for(Cookie cookie:cookies){
                if(cookie.getName().equals(Constants.AUTH_COOKIE)){
                    authCookie = cookie;
                    break;
                }
            }
        }
        if(authCookie != null && !request.getRequestURI().startsWith("/public")){
            if(authCookie.getValue() == null || authCookie.getValue().length() == 0){
                throw new InvalidCookieException("Invalid Auth Cookie");
            }
            String token = authCookie.getValue();
            if(jwtAuthenticationHelper.isTokenExpired(token)){
                throw new InvalidTokenException("Token is Expired");
            }
            String email = jwtAuthenticationHelper.getClaims(token).getSubject();
            User user = userDetailsService.loadUserByUsername(email);
            if(SecurityContextHolder.getContext().getAuthentication() == null){
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(token, null, user.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
