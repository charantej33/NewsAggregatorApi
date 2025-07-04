package com.airtribe.News.Aggregator.API.Service;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.airtribe.News.Aggregator.API.dto.AuthenticationRequest;
import com.airtribe.News.Aggregator.API.entity.User;
import com.airtribe.News.Aggregator.API.repository.UserRepository;
import com.airtribe.News.Aggregator.API.service.NewsService;
import com.airtribe.News.Aggregator.API.util.JwtUtil;

public class UserServiceTest {
    @InjectMocks
    private NewsService newsService;

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock 
    private AuthenticationRequest loginRequest;
    @Mock
    private JwtUtil jwtUtil;
     @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
     void loginUser_ShouldReturnToken_WhenCredentialsAreValid() {
         // Arrange
        AuthenticationRequest loginRequest = new AuthenticationRequest();
        loginRequest.setUsername("john");
        loginRequest.setPassword("password123");

        User user = new User();
        user.setUsername("john");
        user.setPassword("1234512345");
        when(userRepository.findByUsername("john")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password123", user.getPassword())).thenReturn(true);
        when(jwtUtil.generateToken(any(UserDetails.class))).thenReturn("mockedToken");

         // Act
         String token = newsService.loginRequest(loginRequest);
         // Assert
            assert token.equals("mockedToken");
            verify(userRepository,times(1)).findByUsername("john");
            verify(passwordEncoder, times(1)).matches("password123", user.getPassword());
            verify(jwtUtil, times(1)).generateToken(any(UserDetails.class));
     }
     @Test
     void loginUser_ShouldThrowException_WhenUserNotFound() {
        //Arrange
        AuthenticationRequest loginRequest = new AuthenticationRequest();
        loginRequest.setUsername("nonexistentUser");
        loginRequest.setPassword("password123");

        when(userRepository.findByUsername("nonexistentUser")).thenReturn(Optional.empty());
        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> {
            newsService.loginRequest(loginRequest);
        });
        verify(userRepository, times(1)).findByUsername("nonexistentUser");
        verify(passwordEncoder, times(0)).matches(any(), any());
        verify(jwtUtil, times(0)).generateToken(any(UserDetails.class));

     }
        @Test
        void loginUser_ShouldThrowException_WhenPasswordDoesNotMatch() {
            // Arrange
            AuthenticationRequest loginRequest = new AuthenticationRequest();
            loginRequest.setUsername("john");
            loginRequest.setPassword("wrongPassword");

            User user = new User();
            user.setUsername("john");
            user.setPassword("1234512345");

            when(userRepository.findByUsername("john")).thenReturn(Optional.of(user));
            when(passwordEncoder.matches("wrongPassword", user.getPassword())).thenReturn(false);

            // Act & Assert
            assertThrows(BadCredentialsException.class, () -> {
    
                    newsService.loginRequest(loginRequest);
            });
            verify(userRepository, times(1)).findByUsername("john");
            verify(passwordEncoder, times(1)).matches("wrongPassword", user.getPassword());
            verify(jwtUtil, times(0)).generateToken(any(UserDetails.class));
        }
    @Test
    void saveUser_ShouldSaveUser_WhenUsernameIsUnique() {
    // Arrange
        User user = new User();
        user.setUsername("john");
        user.setPassword("password123");

        when(userRepository.findByUsername("john")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");

        // Act
        newsService.saveUser(user.getUsername(), user.getPassword());

        // Assert
        verify(userRepository, times(1)).save(any(User.class));
      
    }
    @Test
    void SaveTheUserPreferences_ShouldUpdatePreferences_WhenUserExists() {
        // Arrange
        String username = "john";
        Set<String> preferences = Set.of("technology", "sports");

        User user = new User();
        user.setUsername(username);
        user.setPreferences(Set.of());

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        // Act
        newsService.SaveTheUserPreferences(username, preferences);

        // Assert
        verify(userRepository, times(1)).save(user);
        assert user.getPreferences().equals(preferences);
    }
}
