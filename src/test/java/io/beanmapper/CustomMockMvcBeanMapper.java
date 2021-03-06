package io.beanmapper;

import io.beanmapper.spring.web.MergedFormMethodArgumentResolver;
import io.beanmapper.spring.web.mockmvc.MockEntityConverter;
import io.beanmapper.spring.web.mockmvc.MockEntityFinder;
import io.beanmapper.spring.web.mockmvc.MockIdToEntityBeanConverter;
import io.beanmapper.spring.web.mockmvc.MockMvcBeanMapper;
import org.springframework.data.domain.Persistable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;

import java.util.List;

public class CustomMockMvcBeanMapper extends MockMvcBeanMapper {

    private final FormattingConversionService conversionService;
    private final List<HttpMessageConverter<?>> messageConverters;
    private BeanMapper beanMapper;
    private MockEntityFinder mockEntityFinder = new MockEntityFinder();

    public CustomMockMvcBeanMapper(FormattingConversionService conversionService,
                             List<HttpMessageConverter<?>> messageConverters,
                             BeanMapper beanMapper) {
        super(conversionService, messageConverters, beanMapper);
        this.conversionService = conversionService;
        this.messageConverters = messageConverters;
        this.beanMapper = beanMapper;
    }

    public void registerRepository(CrudRepository<? extends Persistable, Long> repository, Class<?> entityClass) {

        // Add a converter for the target class to the generic conversion service
        conversionService.addConverter(String.class, entityClass, new MockEntityConverter<>(repository));

        // Add a BeanConverter for the target class to the BeanMapper
        beanMapper = beanMapper.config().addConverter(new MockIdToEntityBeanConverter(repository, entityClass)).build();

        // Add the repository to the MockEntityFinder
        mockEntityFinder.addRepository(repository, entityClass);

    }

    public HandlerMethodArgumentResolver[] createHandlerMethodArgumentResolvers() {
        return new HandlerMethodArgumentResolver[] {
                new MergedFormMethodArgumentResolver(messageConverters, beanMapper, mockEntityFinder)
        };
    }

    public FormattingConversionService getConversionService() {
        return this.conversionService;
    }

    public BeanMapper getBeanMapper() {
        return beanMapper;
    }
}
