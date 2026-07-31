package com.zs.assignment1112;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class Main {
    private static final Logger logger =
            LogManager.getLogger(Main.class);

    public static void main(String[] args){
        logger.info("Application Started");
      SpringApplication.run(Main.class,args);
    }
}
