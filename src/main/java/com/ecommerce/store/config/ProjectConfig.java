package com.ecommerce.store.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProjectConfig {
    // Automatic convert dto to entity or viceversa library use
    @Bean
    public ModelMapper mapper(){
        return new ModelMapper();
    }
}
