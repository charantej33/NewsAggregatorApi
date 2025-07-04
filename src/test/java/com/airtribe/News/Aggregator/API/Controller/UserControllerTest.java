package com.airtribe.News.Aggregator.API.Controller;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.airtribe.News.Aggregator.API.controller.NewsAggregatorController;
import com.airtribe.News.Aggregator.API.dto.RegisterDto;
import com.airtribe.News.Aggregator.API.entity.User;
import com.airtribe.News.Aggregator.API.service.NewsService;
import com.fasterxml.jackson.databind.ObjectMapper;


@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
@Autowired
private MockMvc mockMvc;
@InjectMocks
private NewsAggregatorController newsAggregatorController;

@Mock
private NewsService newsService;

@Autowired
private ObjectMapper objectMapper;
@BeforeEach
    void setUp() {
        // mockMvc = MockMvcBuilders.standaloneSetup(newsAggregatorController).build();
     }


    @Test
    void registerUser_ShouldReturnOk_WhenUserIsRegistered() throws Exception {
        // Arrange
     RegisterDto requestDto = new RegisterDto();
        requestDto.setUsername("newUser");
        requestDto.setPassword("newPassword");
        requestDto.setPreferences(List.of("technology", "sports"));

        User userentity = new User();
       
        userentity.setUsername("newUser");
        userentity.setPassword("newPassword");  
        userentity.setPreferences(Set.of("technology", "sports"));
       
        //mock
        Mockito.doNothing().when(newsService).saveUser(Mockito.anyString(), Mockito.anyString());


        //act& assert
        mockMvc.perform(post("/api/register")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(content().string("User registered successfully"))
                 .andExpect(jsonPath("$.username").value("newUser"))
                .andExpect(jsonPath("$.preferences[0]").value("technology"))
                .andExpect(jsonPath("$.preferences[1]").value("sports"));
    }


}
