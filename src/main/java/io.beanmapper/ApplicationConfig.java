package io.beanmapper;

import io.beanmapper.config.BeanMapperBuilder;
import io.beanmapper.config.WebMvcConfig;
import io.beanmapper.spring.unproxy.HibernateAwareBeanUnproxy;
import io.beanmapper.support.CustomIdToEntityBeanConverter;
import org.hibernate.cfg.ImprovedNamingStrategy;
import org.hibernate.dialect.Dialect;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@ComponentScan(basePackageClasses = ApplicationConfig.class,
        excludeFilters = {
                @Filter({ ControllerAdvice.class, Controller.class, RestController.class }),
                @Filter(value = WebMvcConfig.class, type = FilterType.ASSIGNABLE_TYPE)
        })
@EnableTransactionManagement
@EnableJpaRepositories(basePackageClasses = ApplicationConfig.class)
public class ApplicationConfig {

    @Autowired private DataSource dataSource;
    @Autowired private Class<? extends Dialect> hibernateDialect;
    @Autowired @Qualifier("hibernate2Ddl") private String hibernate2Ddl;
    @Autowired private ApplicationContext applicationContext;

    @Bean
    public BeanMapper beanMapper() {
        return new BeanMapperBuilder()
                .addPackagePrefix(ApplicationConfig.class)
                .setBeanUnproxy(new HibernateAwareBeanUnproxy())
                .addProxySkipClass(Enum.class)
                .addConverter(new CustomIdToEntityBeanConverter(applicationContext))
                .build();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setDataSource(dataSource);
        entityManagerFactoryBean.setPersistenceProviderClass(HibernatePersistenceProvider.class);
        entityManagerFactoryBean.setPackagesToScan(ApplicationConfig.class.getPackage().getName());
        HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
        entityManagerFactoryBean.setJpaVendorAdapter(jpaVendorAdapter);

        Map<String, Object> jpaProperties = new HashMap<>();
        jpaProperties.put("hibernate.show_sql", Boolean.TRUE);
        jpaProperties.put("hibernate.ejb.naming_strategy", ImprovedNamingStrategy.class.getName());
        jpaProperties.put("hibernate.dialect", hibernateDialect.getName());
        jpaProperties.put("hibernate.hbm2ddl.auto", hibernate2Ddl);
        jpaProperties.put("hibernate.jdbc.use_get_generated_keys", true);
        jpaProperties.put("hibernate.generate_statistics", false);
        entityManagerFactoryBean.setJpaPropertyMap(jpaProperties);

        return entityManagerFactoryBean;
    }

    @Bean
    public JpaTransactionManager transactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
        return transactionManager;
    }
}
