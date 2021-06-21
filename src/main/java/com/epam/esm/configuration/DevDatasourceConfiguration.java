package com.epam.esm.configuration;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Objects;
import java.util.Properties;

@Configuration
@Profile("dev")
@ComponentScan("com.epam.esm")
@PropertySources({@PropertySource("classpath:database_dev.properties"),
        @PropertySource("classpath:hibernate_dev.properties")})
public class DevDatasourceConfiguration {
    private static final String DB_URL = "db.url";
    private static final String DB_USERNAME = "db.username";
    private static final String DB_PASSWORD = "db.password";
    private static final String DB_DRIVER = "db.driver";
    private static final String POOL_INITIAL_SIZE = "pool.initialSize";
    private static final String POOL_MAX_TOTAL = "pool.maxTotal";
    private static final String HIBERNATE_DIALECT = "hibernate.dialect";
    private static final String HIBERNATE_HBM2DDL = "hibernate.hbm2ddl.auto";
    private static final String HIBERNATE_SHOW_SQL = "hibernate.show_sql";
    private static final String PACKAGE_TO_SCAN = "com.epam.esm.model.entity";
    private static final Logger logger = LoggerFactory.getLogger(DevDatasourceConfiguration.class);

    private final Environment env;

    @Autowired
    public DevDatasourceConfiguration(Environment env) {
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
        logger.debug("BasicDataSource is created...");
        return basicDataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource());
        em.setPackagesToScan(PACKAGE_TO_SCAN);
        JpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(jpaVendorAdapter);
        Properties properties = new Properties();
        properties.setProperty(HIBERNATE_DIALECT, env.getProperty(HIBERNATE_DIALECT));
        properties.setProperty(HIBERNATE_HBM2DDL, env.getProperty(HIBERNATE_HBM2DDL));
        properties.setProperty(HIBERNATE_SHOW_SQL, env.getProperty(HIBERNATE_SHOW_SQL));
        em.setJpaProperties(properties);
        logger.debug("LocalContainerEntityManagerFactoryBean is created...");
        return em;
    }

    @Bean
    public PlatformTransactionManager txManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
        logger.debug("PlatformTransactionManager is created...");
        return transactionManager;
    }
}
