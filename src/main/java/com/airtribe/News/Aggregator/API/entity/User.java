package com.airtribe.News.Aggregator.API.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User implements UserDetails  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @ElementCollection
    private Set<String> preferences;

    @ElementCollection
    private Set<String> readArticles;

    @ElementCollection
    private Set<String> favoriteArticles;

   

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
     public User(){
        
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;  // Account never expires
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;  // Account never locked
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;  // Credentials never expire
    }

    @Override
    public boolean isEnabled() {
        return true;  // Account is always enabled
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setPreferences(Set<String> preferences) {
        this.preferences = preferences;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }
    public Set<String> getPreferences() {
        return preferences;
    }

    public Set<String> getReadArticles() {
        return readArticles;
    }

    public Set<String> getFavoriteArticles() {
        return favoriteArticles;
    }

}
