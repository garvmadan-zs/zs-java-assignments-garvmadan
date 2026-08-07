package com.zs.assignment1112.exception;


import org.junit.jupiter.api.Assertions;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;


class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
    }

    @Test
    void shouldHandleResourceNotFoundException() {
        ResourceNotFoundException exception = new ResourceNotFoundException("Category not found");

        ResponseEntity<ErrorResponse> response = handler.handleResourceNotFound(exception);

        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        ErrorResponse body = response.getBody();
        Assertions.assertNotNull(body);
        Assertions.assertEquals(404, body.getStatus());
        Assertions.assertEquals("Category not found", body.getMessage());
        Assertions.assertNotNull(body.getTimestamp());
    }

    @Test
    void shouldHandleIllegalArgumentException() {
        IllegalArgumentException exception = new IllegalArgumentException("Invalid input");

        ResponseEntity<ErrorResponse> response = handler.handleIllegalArgumentException(exception);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ErrorResponse body = response.getBody();
        Assertions.assertNotNull(body);
        Assertions.assertEquals(400, body.getStatus());
        Assertions.assertEquals("Invalid input", body.getMessage());
        Assertions.assertNotNull(body.getTimestamp());
    }

    @Test
    void shouldHandleValidationException() {
        TestRequest request = new TestRequest();

        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(request, "request");

        bindingResult.addError(new FieldError("request", "name", "Name is required"));

        bindingResult.addError(new FieldError("request", "price", "Price must be positive"));

        MethodParameter methodParameter = new MethodParameter(TestController.class.getDeclaredMethods()[0], 0);

        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(methodParameter, bindingResult);

        ResponseEntity<Map<String, String>> response = handler.handleValidationException(exception);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        Map<String, String> body = response.getBody();

        Assertions.assertNotNull(body);
        Assertions.assertEquals(2, body.size());
        Assertions.assertEquals("Name is required", body.get("name"));
        Assertions.assertEquals("Price must be positive", body.get("price"));
    }

    @Test
    void shouldHandleUnexpectedException() {
        Exception exception = new Exception("Unexpected");

        ResponseEntity<ErrorResponse> response = handler.handleException(exception);

        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());

        ErrorResponse body = response.getBody();
        Assertions.assertNotNull(body);
        Assertions.assertEquals(500, body.getStatus());
        Assertions.assertEquals("An unexpected error occurred", body.getMessage());
        Assertions.assertNotNull(body.getTimestamp());
    }

    @Test
    void shouldHandleMethodArgumentTypeMismatch() {
        MethodArgumentTypeMismatchException exception = new MethodArgumentTypeMismatchException("abc", Long.class, "categoryId", null, new IllegalArgumentException());

        ResponseEntity<ErrorResponse> response = handler.handleMethodArgumentTypeMismatch(exception);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ErrorResponse body = response.getBody();

        Assertions.assertNotNull(body);
        Assertions.assertEquals(400, body.getStatus());
        Assertions.assertEquals("Invalid value 'abc' for parameter 'categoryId'. Expected type: Long.", body.getMessage());
        Assertions.assertNotNull(body.getTimestamp());
    }

    static class TestRequest {
        private String name;
        private Double price;
    }

    static class TestController {
        public void test(TestRequest request) {
        }
    }

    @Test
    void shouldHandleHttpMessageNotReadableException() {
        HttpMessageNotReadableException exception = new HttpMessageNotReadableException("Malformed JSON");

        ResponseEntity<ErrorResponse> response = handler.handleHttpMessageNotReadable(exception);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ErrorResponse body = response.getBody();
        Assertions.assertNotNull(body);
        Assertions.assertEquals(400, body.getStatus());
        Assertions.assertEquals("Malformed JSON request", body.getMessage());
        Assertions.assertNotNull(body.getTimestamp());
    }
}
