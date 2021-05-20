package com.hitqz.scds.config;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.ClassicConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

@Configuration
public class FlywayConfig {

    @Autowired
    private DataSource dataSource;

    @PostConstruct
    public void migrate() {
        ClassicConfiguration classicConfiguration = new ClassicConfiguration();
        classicConfiguration.setDataSource(dataSource);
        classicConfiguration.setBaselineOnMigrate(true);
        Flyway flyway = new Flyway(classicConfiguration);
        flyway.migrate();
    }
}
