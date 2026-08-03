package com.zs.assignment1112.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class HelloController {

    private static final Logger log =
            LogManager.getLogger(HelloController.class);

    @GetMapping("/hello")
    public ResponseEntity<String> hello() {

        log.info("Hello API called");

        return ResponseEntity.ok(
                "Application is running"
        );
    }
}

