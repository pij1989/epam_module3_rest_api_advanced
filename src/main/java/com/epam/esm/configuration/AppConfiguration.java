package com.epam.esm.configuration;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Properties;

@Configuration
@ComponentScan("com.epam.esm")
@PropertySources({@PropertySource("${path.configure:classpath:database.properties}"),
        @PropertySource("classpath:hibernate.properties")})
@EnableTransactionManagement
public class AppConfiguration {
    private static final String DB_URL = "db.url";
    private static final String DB_USERNAME = "db.username";
    private static final String DB_PASSWORD = "db.password";
    private static final String DB_DRIVER = "db.driver";
    private static final String POOL_INITIAL_SIZE = "pool.initialSize";
    private static final String POOL_MAX_TOTAL = "pool.maxTotal";
    private static final String HIBERNATE_DIALECT = "hibernate.dialect";
    private static final String HIBERNATE_HBM2DDL = "hibernate.hbm2ddl.auto";
    private static final String HIBERNATE_SHOW_SQL = "hibernate.show_sql";

    private final Environment env;

    @Autowired
    public AppConfiguration(Environment env) {
        this.env = env;
    }

    @Bean
    public BasicDataSource dataSource() {
        BasicDataSource basicDataSource = new BasicDataSource();
        basicDataSource.setUrl(env.getProperty(DB_URL));
        basicDataSource.setUsername(env.getProperty(DB_USERNAME));
        basicDataSource.setPassword(env.getProperty(DB_PASSWORD));
        basicDataSource.setDriverClassName(env.getProperty(DB_DRIVER));
        basicDataSource.setInitialSize(Integer.parseInt(Objects.requireNonNull(env.getProperty(POOL_INITIAL_SIZE))));
        basicDataSource.setMaxTotal(Integer.parseInt(Objects.requireNonNull(env.getProperty(POOL_MAX_TOTAL))));
        return basicDataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource());
        em.setPackagesToScan("com.epam.esm.model.entity");
        JpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(jpaVendorAdapter);
        Properties properties = new Properties();
        properties.setProperty(HIBERNATE_DIALECT, env.getProperty(HIBERNATE_DIALECT));
        properties.setProperty(HIBERNATE_HBM2DDL, env.getProperty(HIBERNATE_HBM2DDL));
        properties.setProperty(HIBERNATE_SHOW_SQL, env.getProperty(HIBERNATE_SHOW_SQL));
        em.setJpaProperties(properties);
        return em;
    }

    @Bean
    public PlatformTransactionManager txManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
        return transactionManager;
    }

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
        return builder -> builder.serializationInclusion(JsonInclude.Include.NON_NULL)
                .serializationInclusion(JsonInclude.Include.NON_EMPTY)
                .serializers(new LocalDateTimeSerializer(DateTimeFormatter.ISO_DATE_TIME));
    }
}
