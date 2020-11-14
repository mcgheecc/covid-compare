package com.cmg.covidcompare.webscraper.config;

import com.cmg.covidcompare.webscraper.worldometer.WorldometerMapper;
import com.gargoylesoftware.htmlunit.WebClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WorldometerConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**");
    }

    @Bean
    public WebClient webClient() {
        return new WebClient();
    }

    @Bean
    public WorldometerMapper worldometerMapper() {
        return new WorldometerMapper();
    }
}
