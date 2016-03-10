package io.beanmapper.profiles;

import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.H2Dialect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;

import javax.sql.DataSource;

import static org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType.H2;

@Configuration
@Profile("test")
public class TestProfile {

    @Bean
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
                .setType(H2)
                .setName("dev")
                .build();
    }

    @Bean
    public Class<? extends Dialect> hibernateDialect() {
        return H2Dialect.class;
    }

    @Bean
    public String hibernate2Ddl() {
        return "create";
    }
}
