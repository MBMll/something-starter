package org.github.mbmll.example.springbatch.configuration;

import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;
import org.github.mbmll.example.springbatch.entity.targets.TargetEntityPackage;
import org.github.mbmll.example.springbatch.repository.targets.TargetRepositoryPackage;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableJpaRepositories(basePackageClasses = TargetRepositoryPackage.class,
    entityManagerFactoryRef = "targetEntityManagerFactory",
    transactionManagerRef = "targetTransactionManager")
public class TargetDataSourceConfiguration {

    @Bean("targetDataSourceProperties")
    @ConfigurationProperties("spring.datasource.target")
    public DataSourceProperties targetDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean("targetDataSource")
    public DataSource targetDataSource(
        @Qualifier("targetDataSourceProperties") DataSourceProperties targetDataSourceProperties) {
        return targetDataSourceProperties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
    }

    @Bean(name = "targetEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean targetEntityManagerFactory(
        EntityManagerFactoryBuilder builder, @Qualifier("targetDataSource") DataSource targetDataSource) {
        return builder
            .dataSource(targetDataSource)
            .packages(TargetEntityPackage.class)
            .build();
    }

    @Bean
    public PlatformTransactionManager targetTransactionManager(
        final @Qualifier("targetEntityManagerFactory") LocalContainerEntityManagerFactoryBean targetEntityManagerFactory) {
        return new JpaTransactionManager(targetEntityManagerFactory.getObject());
    }
}
