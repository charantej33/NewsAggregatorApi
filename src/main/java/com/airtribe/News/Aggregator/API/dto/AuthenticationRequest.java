package com.airtribe.News.Aggregator.API.dto;

import lombok.*;
import org.hibernate.annotations.SecondaryRow;

@Getter
@Setter
@Data
public class AuthenticationRequest {
    private String username;
    private String password;

public AuthenticationRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
    public AuthenticationRequest() {
        // Default constructor
}
public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
