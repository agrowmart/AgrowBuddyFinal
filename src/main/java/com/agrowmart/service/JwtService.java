package com.agrowmart.service;

import com.agrowmart.entity.User;
import com.agrowmart.entity.customer.Customer;
import com.agrowmart.util.JwtUtil;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    private final JwtUtil jwtUtil;

    public JwtService(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    // For Vendors
    public String issueToken(User user) {
        return jwtUtil.generateTokenForVendor(user);
    }

    // For Customers
    public String issueTokenForCustomer(Customer customer) {
        return jwtUtil.generateTokenForCustomer(customer);
    }

    // Common extraction & validation
    public String extractRole(String token) {
        return jwtUtil.extractRole(token);
    }

    public Long extractUserId(String token) {
        return jwtUtil.extractUserId(token);
    }

    public String extractUserType(String token) {
        return jwtUtil.extractUserType(token);
    }

    public String extractSubject(String token) {
        return jwtUtil.extractSubject(token);
    }

    public boolean validateToken(String token, Long userId) {
        return jwtUtil.validateToken(token, userId);
    }

    public boolean validateToken(String token, String email) {
        return jwtUtil.validateToken(token, email);
    }

    public boolean isTokenExpired(String token) {
        return jwtUtil.isTokenExpired(token);
    }
}