package com.example.caloriecalculator.controller;

import com.example.caloriecalculator.model.Food;
import com.example.caloriecalculator.model.assemblers.FoodModelAssembler;
import com.example.caloriecalculator.service.FoodService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.instancio.Instancio;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.NoSuchElementException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

    @Autowired
    private FoodModelAssembler assembler;


    @BeforeEach
    void setUp() {
        this.foodList = Instancio.ofList(Food.class).size(10).create();
        this.mapper = new ObjectMapper();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testFindAll() throws Exception {
        String baseURI = "http://localhost/foods";
        when(foodService.findAll()).thenReturn(foodList);
        MvcResult result = mockMvc.perform(get("/foods")).andExpect(status().isOk()).andReturn();
        assertThat(result.getResponse().getContentAsString(), containsString(baseURI));
        assertThat(result.getResponse().getContentAsString(), containsString(baseURI + "/" + foodList.get(0).getId()));
        verify(foodService, times(1)).findAll();
    }

    @Test
    void testFindById() throws Exception{
        String baseURI = "http://localhost/foods";
        when(foodService.findById(1)).thenReturn(foodList.get(0));
        MvcResult result = mockMvc.perform(get("/foods/{id}", 1)).andExpect(status().isOk()).andReturn();
        assertThat(result.getResponse().getContentAsString(), containsString(baseURI + "/" + foodList.get(0).getId()));
        verify(foodService, times(1)).findById(1);
    }

    @Test
    void testFindById_whenNotFound_thanBadRequest() throws Exception {
        when(foodService.findById(0)).thenThrow(NoSuchElementException.class);
        mockMvc.perform(get("/foods/{id}", 0)).andExpect(status().is4xxClientError());
        verify(foodService, times(0)).findById(1);
    }
}