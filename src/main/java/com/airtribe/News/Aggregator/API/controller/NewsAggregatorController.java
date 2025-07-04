package com.airtribe.News.Aggregator.API.controller;

import com.airtribe.News.Aggregator.API.dto.AuthenticationRequest;
import com.airtribe.News.Aggregator.API.dto.AuthenticationResponse;
import com.airtribe.News.Aggregator.API.entity.User;
import com.airtribe.News.Aggregator.API.repository.UserRepository;
import com.airtribe.News.Aggregator.API.service.NewsService;
import com.airtribe.News.Aggregator.API.util.ExternalNewsApiClient;
import com.airtribe.News.Aggregator.API.util.JwtUtil;

import jakarta.validation.Valid;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import reactor.core.publisher.Mono;


import java.util.Set;
import java.util.prefs.Preferences;

import com.airtribe.News.Aggregator.API.dto.RegisterDto;

@RestController
@RequestMapping("/api")
public class NewsAggregatorController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ExternalNewsApiClient externalNewsApiClient;
    @Autowired
    private NewsService newsService;
    @Autowired
    private JwtUtil jwtUtil;
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterDto registerDto) {
        String encodedPassword = passwordEncoder.encode(registerDto.getPassword());
        newsService.saveUser(registerDto.getUsername(), encodedPassword);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest loginRequest) throws Exception{
        String token = newsService.loginRequest(loginRequest);
        return ResponseEntity.ok(new AuthenticationResponse(token));
    }

    @GetMapping("/preferences")
    public ResponseEntity<?> getPreferences() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByUsername((username))
                .orElseThrow(() -> new RuntimeException("Error: User not found"));
       newsService.GetTheUserPreferences(username);
        Set<String> preferences = user.getPreferences();
        if (preferences.isEmpty()) {
            return ResponseEntity.ok("No preferences set");
        }
        return ResponseEntity.ok(preferences);
    }

    @PutMapping("/preferences")
    public ResponseEntity<?> updatePreferences(@Valid @RequestBody Set<String> preferences) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Error: User not found"));
       newsService.SaveTheUserPreferences(username, preferences);
        return ResponseEntity.ok("Preferences updated successfully");
    }

    @GetMapping("/news")
    public ResponseEntity<?> getNews() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Error: User not found"));

        Set<String> preferences = user.getPreferences();
        return externalNewsApiClient.fetchNews(preferences)
                .map(newsResponse -> ResponseEntity.ok(newsResponse))
                .onErrorResume(e -> Mono.just(ResponseEntity.badRequest().body("Error fetching news: " + e.getMessage()))).block();
    }
    @GetMapping("/news/search/{keyword}")
    public ResponseEntity<?> getNewsSearch(@PathVariable String keyword) {
        return  externalNewsApiClient.searchNews(keyword)
                .map(newsResponse -> ResponseEntity.ok(newsResponse))
                .onErrorResume(e -> Mono.just(ResponseEntity.badRequest().body("Error fetching news: " + e.getMessage()))).block();
    }

    @PostMapping("/news/{id}/read")
    public ResponseEntity<?> readNews(@PathVariable String id) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        newsService.markAsRead(username, id);
        return ResponseEntity.ok("Article marked as read");
    }

    @PostMapping("/news/{id}/favorite")
    public ResponseEntity<?> favoriteNews(@PathVariable String id) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        newsService.markAsFavorite(username, id);
        return ResponseEntity.ok("Article marked as favorite");
    }
    @GetMapping("/news/read")
    public ResponseEntity<?> getReadNews() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Set<String> readArticles = newsService.getReadArticles(username);
        return ResponseEntity.ok(readArticles);
    }

    @GetMapping("/news/favorites")
    public ResponseEntity<?> getFavoriteNews() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Set<String> favoriteArticles = newsService.getFavoriteArticles(username);
        return ResponseEntity.ok(favoriteArticles);
    }

}
