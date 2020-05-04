package com.mylibrary.rental.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * Configuration for {@link ObjectMapper}.
 * Since HATEOAS/HAL is enabled, it creates its own specific ObjectMapper which has to be adjusted in order to
 * set up rules for serialization/deserialization.
 * <p>
 *
 */
@Configuration
public class ObjectMapperCustomizer {

    private static final String SPRING_HATEOAS_OBJECT_MAPPER = "_halObjectMapper";

    private final ObjectMapper springHateoasObjectMapper;

    /**
     * Instantiates a new Object mapper customizer.
     *
     * @param springHateoasObjectMapper the spring hateoas object mapper
     */
    @Autowired
    public ObjectMapperCustomizer(@Qualifier(SPRING_HATEOAS_OBJECT_MAPPER) ObjectMapper springHateoasObjectMapper) {
        this.springHateoasObjectMapper = springHateoasObjectMapper;
    }

    @Bean(name = "objectMapper")
    @Primary
    ObjectMapper objectMapper() {
        this.springHateoasObjectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        this.springHateoasObjectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        this.springHateoasObjectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        this.springHateoasObjectMapper.configure(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS, false);
        this.springHateoasObjectMapper.configure(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS, false);
        //Switch to ISO-8601 compliant format
        this.springHateoasObjectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        this.springHateoasObjectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        this.springHateoasObjectMapper.registerModules(new JavaTimeModule());
        //the application should now always include unannotated fields for each view
        this.springHateoasObjectMapper.configure(MapperFeature.DEFAULT_VIEW_INCLUSION, true);
        return this.springHateoasObjectMapper;
    }

}


