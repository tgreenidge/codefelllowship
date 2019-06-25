package com.tgreenidge.codefellowship.codefellowship;



import org.junit.Test;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.*;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;



@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CodefellowshipApplicationTests {
    @Autowired
    CodefellowshipController codefellowshipController;

    @Autowired
    MockMvc mockMvc;


    @Test
    public void contextLoads() {
    }

    @Test
    public void testControllerIsAutowired() {
        assertNotNull(codefellowshipController);
    }

    @Test
    public void testRequestToRootGivesWelcome() throws Exception {
        mockMvc.perform(get("/")).andExpect(content().string(containsString("Welcome to Codefellowship!")));
    }

    @Test
    public void testRequestToSignUpGivesSignUp() throws Exception {
        mockMvc.perform(get("/signup")).andExpect(content().string(containsString("Sign up")));
    }

    @Test
    public void testRequestToLoginGivesUsername() throws Exception {
        mockMvc.perform(get("/login")).andExpect(content().string(containsString("Username:")));
    }

}

