package com.johnbryce.coupon_system_part_2;

import com.github.javafaker.Faker;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfiguration {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().
                //For deep mapping
                        setFieldMatchingEnabled(true)
                .setFieldAccessLevel
                        (org.modelmapper.config.
                                Configuration.AccessLevel.
                                PRIVATE);
        return modelMapper;
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
