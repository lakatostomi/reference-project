package com.example.caloriecalculator.controller;

import com.example.caloriecalculator.dto.FoodDTO;
import com.example.caloriecalculator.model.Food;
import com.example.caloriecalculator.service.FoodService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.instancio.Instancio;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.NoSuchElementException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class FoodControllerUnitTest {

    @MockBean
    private FoodService foodService;

    @Autowired
    private MockMvc mockMvc;

    private List<Food> foodList;

    private ObjectMapper mapper;

    private String baseURI = "http://localhost/api/rest/foods";


    @BeforeEach
    void setUp() {
        this.foodList = Instancio.ofList(Food.class).size(10).create();
        this.mapper = new ObjectMapper();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @WithMockUser(username = "test@test.com", password = "test")
    void testFindAll() throws Exception {
        when(foodService.findAll()).thenReturn(foodList);
        MvcResult result = mockMvc.perform(get("/api/rest/foods")).andExpect(status().isOk()).andReturn();
        assertThat(result.getResponse().getContentAsString(), containsString(baseURI));
        assertThat(result.getResponse().getContentAsString(), containsString(baseURI + "/" + foodList.get(0).getId()));
        verify(foodService, times(1)).findAll();
    }

    @Test
    @WithMockUser(username = "test@test.com", password = "test")
    void testFindById() throws Exception {
        when(foodService.findFoodById(1)).thenReturn(foodList.get(0));
        MvcResult result = mockMvc.perform(get("/api/rest/foods/{id}", 1)).andExpect(status().isOk()).andReturn();
        assertThat(result.getResponse().getContentAsString(), containsString(baseURI + "/" + foodList.get(0).getId()));
        verify(foodService, times(1)).findFoodById(1);
    }

    @Test
    @WithMockUser(username = "test@test.com", password = "test")
    void testFindById_whenNotFound_thanBadRequest() throws Exception {
        when(foodService.findFoodById(0)).thenThrow(NoSuchElementException.class);
        mockMvc.perform(get("/api/rest/foods/{id}", 0)).andExpect(status().is4xxClientError());
        verify(foodService, times(0)).findFoodById(1);
    }

    @Test
    @WithMockUser(username = "test@test.com", authorities = {"ROLE_ADMIN", "WRITE_PRIVILEGE"})
    @WithUserDetails(value = "admin")
    void testSaveNewFood() throws Exception {
        FoodDTO foodDTO = Instancio.create(FoodDTO.class);
        Food food = Instancio.create(Food.class);
        when(foodService.saveFood(foodDTO)).thenReturn(food);
        MvcResult result = mockMvc.perform(post("/api/rest/foods").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(foodDTO)))
                .andExpect(status().isCreated()).andReturn();
        String createdURI = result.getResponse().getHeader("Location");
        assertThat(createdURI, is(equalTo(baseURI + "/" + food.getId())));
        verify(foodService, times(1)).saveFood(foodDTO);
    }

    @Test
    @WithMockUser(username = "test@test.com", authorities = {"ROLE_ADMIN", "WRITE_PRIVILEGE"})
    void testUpdateFood() throws Exception {
        FoodDTO foodDTO = Instancio.create(FoodDTO.class);
        Food food = Instancio.create(Food.class);
        when(foodService.updateFood(foodDTO, 1)).thenReturn(food);
        MvcResult result = mockMvc.perform(put("/api/rest/foods/{id}", 1).contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(foodDTO)))
                .andExpect(status().isCreated()).andReturn();
        String createdURI = result.getResponse().getHeader("Location");
        assertThat(createdURI, is(equalTo(baseURI + "/" + food.getId())));
        verify(foodService, times(1)).updateFood(foodDTO, 1);
    }

    @Test
    @WithMockUser(username = "test@test.com", authorities = {"ROLE_ADMIN", "WRITE_PRIVILEGE"})
    void testDeleteFood() throws Exception {
        mockMvc.perform(delete("/api/rest/foods/{id}", 1)).andExpect(status().isNoContent());
        verify(foodService, times(1)).deleteFood(1);
    }

}