package com.example.caloriecalculator.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

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

    public static ProblemDetail createProblemDetail(int statusCode, String message) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(statusCode), message);
        problemDetail.setProperty("timestamp", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        return problemDetail;
    }

    public static void sendHttpResponse(HttpServletResponse response, Object httpResponse) throws IOException{
        OutputStream outputStream = response.getOutputStream();
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(outputStream, httpResponse);
        outputStream.flush();
    }
}
