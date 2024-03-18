package com.example.letsplay;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class LetsplayApplication {

    public static void main(String[] args) {
        SpringApplication.run(LetsplayApplication.class, args);
    }

    @Bean // This annotation marks the method as a bean provider.
    public ModelMapper getMapper() {
        // Creates and configures an instance of ModelMapper, a utility for object mapping.
        ModelMapper returnValue = new ModelMapper();

        // Sets the configuration to ignore ambiguities and allows private field access.
        returnValue.getConfiguration()
                .setAmbiguityIgnored(true)
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE);

        // Configures the matching strategy to be strict.
        returnValue.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return returnValue; // Returns the configured ModelMapper instance.
    }
}
