package com.opoc.india.vaccine.notifier.infrastructure.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.hateoas.server.mvc.TypeConstrainedMappingJackson2HttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@org.springframework.context.annotation.Configuration
public class NotifierConfiguration {

    @Autowired
    RestTemplateBuilder restTemplateBuilder;


    @Autowired
    @Qualifier("halJacksonHttpMessageConverter")
    private TypeConstrainedMappingJackson2HttpMessageConverter halConverter;


    @Bean
    public RestTemplateCustomizer getRestTemplateCustomizer() {
        return (RestTemplate restTemplate) -> {
            restTemplate.setMessageConverters(getHalConverter(restTemplate));
        };
    }

   /* @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public RestTemplate getRestTemplate() {
        RestTemplate restTemplate = restTemplateBuilder.build();
        return restTemplate;
    }*/

    private List<HttpMessageConverter<?>> getHalConverter(final RestTemplate restTemplate) {
        List<HttpMessageConverter<?>> converters = restTemplate.getMessageConverters();
        converters.add(0, halConverter);
        return converters;
    }

    @Bean
    @Primary
    public TaskExecutor taskExecutor() {
        return new SimpleAsyncTaskExecutor();
    }
}
