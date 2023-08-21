package com.sparta.petnexus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class PetNexusApplication {

    public static void main(String[] args) {
        SpringApplication.run(PetNexusApplication.class, args);
    }

}
