package com.capcredit.ms_loan.config.security;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String userId = request.getHeader("X-User-ID");
        List<SimpleGrantedAuthority> roles = getRoles(request);
        if(userId != null && !roles.isEmpty()) {
            var authentication = new UsernamePasswordAuthenticationToken(userId,  null, roles);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }

	private List<SimpleGrantedAuthority> getRoles(HttpServletRequest request) {
        String role = request.getHeader("X-User-Role");
        if(role == null) {
            return List.of();
        }
        return List.of(new SimpleGrantedAuthority(role));
	}
}