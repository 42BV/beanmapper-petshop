package io.beanmapper.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import io.beanmapper.ApplicationConfig;
import io.beanmapper.BeanMapper;
import io.beanmapper.spring.web.MergedFormMethodArgumentResolver;
import io.beanmapper.spring.web.converter.StructuredJsonMessageConverter;
import io.beanmapper.support.JsonDateDeserializer;
import io.beanmapper.support.JsonDateSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.repository.support.DomainClassConverter;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@EnableWebMvc
@EnableSpringDataWebSupport
@ComponentScan(basePackageClasses = ApplicationConfig.class,
        includeFilters = @Filter({ ControllerAdvice.class, Controller.class, RestController.class }),
        excludeFilters = @Filter({ Configuration.class, Service.class, Repository.class }))
public class WebMvcConfig extends WebMvcConfigurerAdapter {

    @Autowired private FormattingConversionService mvcConversionService;
    @Autowired private BeanMapper beanMapper;
    @Autowired private ApplicationContext applicationContext;
    private MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter;

    public WebMvcConfig() {
        mappingJackson2HttpMessageConverter =  new MappingJackson2HttpMessageConverter();
        mappingJackson2HttpMessageConverter.setObjectMapper(objectMapper());
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(mappingJackson2HttpMessageConverter);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new MergedFormMethodArgumentResolver(
                Collections.singletonList(new StructuredJsonMessageConverter(mappingJackson2HttpMessageConverter)),
                beanMapper,
                applicationContext
        ));
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        SimpleModule module = new SimpleModule();
        module.addSerializer(LocalDate.class, new JsonDateSerializer());
        module.addDeserializer(LocalDate.class, new JsonDateDeserializer());
        mapper.registerModule(module);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper;
    }

    @Bean
    public DomainClassConverter<FormattingConversionService> domainClassConverter() {
        return new DomainClassConverter<>(mvcConversionService);
    }
}
