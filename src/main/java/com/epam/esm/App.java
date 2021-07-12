package com.epam.esm;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;

import java.time.format.DateTimeFormatter;

@SpringBootApplication
public class App {
    private static final String PROFILE_ACTIVE = "profile.active";
    private static final String DEFAULT_PROFILE = "prod";

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
        return builder -> builder.serializationInclusion(JsonInclude.Include.NON_NULL)
                .serializationInclusion(JsonInclude.Include.NON_EMPTY)
                .serializers(new LocalDateTimeSerializer(DateTimeFormatter.ISO_DATE_TIME));
    }

    public static void main(String[] args) {
        String activeProfile = System.getProperty(PROFILE_ACTIVE) != null ? System.getProperty(PROFILE_ACTIVE) : DEFAULT_PROFILE;
        new SpringApplicationBuilder(App.class)
                .profiles(activeProfile)
                .run(args);
    }
}
