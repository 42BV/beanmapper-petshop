package io.beanmapper.model;

import io.beanmapper.ApplicationConfig;
import io.beanmapper.BeanMapper;
import io.beanmapper.config.BeanMapperBuilder;

public class ModelTest {

    protected BeanMapper beanMapper;

    public ModelTest() {
        beanMapper = new BeanMapperBuilder()
                .addPackagePrefix(ApplicationConfig.class)
                .addProxySkipClass(Enum.class)
                .build();
    }
}
