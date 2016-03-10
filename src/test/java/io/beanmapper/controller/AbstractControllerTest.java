package io.beanmapper.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.beanmapper.ApplicationConfig;
import io.beanmapper.CustomMockMvcBeanMapper;
import io.beanmapper.config.WebMvcConfig;
import io.beanmapper.model.BaseModel;
import io.beanmapper.spring.web.mockmvc.MockMvcBeanMapper;
import org.springframework.data.repository.CrudRepository;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

public abstract class AbstractControllerTest {

    private WebMvcConfig config = new WebMvcConfig();
    protected MockMvc webClient;
    protected MockMvcBeanMapper mockMvcBeanMapper;
    protected ObjectMapper objectMapper;

    protected void initWebClient(Object controller) {
        this.mockMvcBeanMapper = new CustomMockMvcBeanMapper(
                new FormattingConversionService(),
                Collections.singletonList(config.getMappingJackson2HttpMessageConverter()),
                new ApplicationConfig().beanMapper()
        );

        this.webClient = MockMvcBuilders.standaloneSetup(controller)
                .setMessageConverters(config.getMappingJackson2HttpMessageConverter())
                .setCustomArgumentResolvers(mockMvcBeanMapper.createHandlerMethodArgumentResolvers())
                .setConversionService(mockMvcBeanMapper.getConversionService())
                .build();
        this.objectMapper = new ObjectMapper();
    }

    public void registerRepository(CrudRepository<? extends BaseModel, Long> repository, Class<?> entityClass) {
        mockMvcBeanMapper.registerRepository(repository, entityClass);
    }
}
