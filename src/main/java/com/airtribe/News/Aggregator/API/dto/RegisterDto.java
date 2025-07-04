package com.airtribe.News.Aggregator.API.dto;

import java.util.List;

public class RegisterDto {
    private String username;
    private String password;
    private List<String> preferences;

  

    public RegisterDto(String username, String password, List<String> preferences) {
        this.username = username;
        this.password = password;
        this.preferences = preferences;
    }
      public RegisterDto() {
        // Default constructor
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public List<String> getPreferences() {
        return preferences;
    }
    public void setPreferences(List<String> preferences) {
        this.preferences = preferences;
    }

}
