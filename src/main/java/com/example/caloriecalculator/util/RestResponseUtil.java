package com.example.caloriecalculator.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RestResponseUtil {


    public static String createJsonStringResponse(HttpResponse httpResponse) {
        ObjectMapper objectMapper = new ObjectMapper();
        String response;
        try {
            response = objectMapper.writeValueAsString(httpResponse);
        } catch (JsonProcessingException ex) {
            response = "\"message\": \"Sorry! An unexpected error occurred during your request!Please try again!\"";
        }
        return response;
    }
}
