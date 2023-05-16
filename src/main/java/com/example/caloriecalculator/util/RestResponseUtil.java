package com.example.caloriecalculator.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

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

    public static void sendHttpResponse(HttpServletResponse response, HttpResponse httpResponse) throws IOException{
        OutputStream outputStream = response.getOutputStream();
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(outputStream, httpResponse);
        outputStream.flush();
    }
}
