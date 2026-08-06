package com.zs.assignment1112.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.assertj.core.api.Assertions;


class HelloControllerTest {


    private final HelloController helloController = new HelloController();


    @Test
    void shouldReturnApplicationRunningMessage() {


        ResponseEntity<String> response = helloController.hello();


        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);


        Assertions.assertThat(response.getBody()).isEqualTo("Application is running");
    }
}
