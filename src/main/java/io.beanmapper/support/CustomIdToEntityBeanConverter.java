package io.beanmapper.support;

import io.beanmapper.core.BeanFieldMatch;
import io.beanmapper.core.converter.BeanConverter;
import org.springframework.context.ApplicationContext;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.core.EntityInformation;
import org.springframework.data.repository.support.Repositories;

import java.io.Serializable;

public class CustomIdToEntityBeanConverter implements BeanConverter {

    private final Repositories repositories;

    public CustomIdToEntityBeanConverter(ApplicationContext applicationContext) {
        this.repositories = new Repositories(applicationContext);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public Object convert(Object source, Class<?> targetClass, BeanFieldMatch beanFieldMatch) {
        if (source == null) {
            return null;
        }

        CrudRepository repository = (CrudRepository) repositories.getRepositoryFor(targetClass);
        return repository.findOne((Serializable) source);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean match(Class<?> sourceClass, Class<?> targetClass) {
        boolean match = false;
        try {
            EntityInformation<Object, Serializable> information = repositories.getEntityInformationFor(targetClass);
            if (information != null) {
                match = sourceClass.equals(information.getIdType());
            }
        } catch (Exception e) {
            return match;
        }
        return match;
    }
}
