package com.airtribe.News.Aggregator.API.service;

import com.airtribe.News.Aggregator.API.entity.User;
import com.airtribe.News.Aggregator.API.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Set;

import com.airtribe.News.Aggregator.API.dto.AuthenticationRequest;
import com.airtribe.News.Aggregator.API.util.JwtUtil;

@Service
public class NewsService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtil jwtUtil;
   
    public void saveUser(String username, String password) {
        if (userRepository.findByUsername(username).isPresent()&& username!=null) {
            throw new RuntimeException("Username already exists");
        }
        User user = new User(username, passwordEncoder.encode(password));
        user.setPreferences(java.util.Collections.emptySet()); // Initialize with empty preferences
        userRepository.save(user);
    }
    public String loginRequest(AuthenticationRequest loginRequest) {
        User user = userRepository.findByUsername(loginRequest.getUsername())
        .orElseThrow(() -> new UsernameNotFoundException("User not found: " + loginRequest.getUsername()));

    if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
        throw new BadCredentialsException("Invalid username or password");
    }

    UserDetails userDetails = new org.springframework.security.core.userdetails.User(
        user.getUsername(),
        user.getPassword(),
        new ArrayList<>()
    );

    return jwtUtil.generateToken(userDetails);
}
    public void SaveTheUserPreferences(String username, Set<String> preferences) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setPreferences(preferences);
        userRepository.save(user);
    }
    public void GetTheUserPreferences(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Set<String> preferences = user.getPreferences();
        // Return or process the preferences as needed
    }

    public void markAsRead(String username, String articleId){
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.getReadArticles().add(articleId);
        userRepository.save(user);
    }
    public void markAsFavorite(String username, String articleId){
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.getFavoriteArticles().add(articleId);
        userRepository.save(user);
    }

    public Set<String> getReadArticles(String username){
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return user.getReadArticles();
    }
    public Set<String> getFavoriteArticles(String username){
        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->new RuntimeException("User not find"));
        return user.getFavoriteArticles();
    }
}
