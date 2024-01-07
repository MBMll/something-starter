
package org.github.mbmll.example.springbatch.configuration;

import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;
import org.github.mbmll.example.springbatch.entity.source.SourceEntityPackage;
import org.github.mbmll.example.springbatch.repository.source.SourceRepositoryPackage;
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

/**
 *
 */
@Configuration
@EnableJpaRepositories(basePackageClasses = SourceRepositoryPackage.class,
    entityManagerFactoryRef = "sourceEntityManagerFactory",
    transactionManagerRef = "sourceTransactionManager")
public class SourceDataSourceConfiguration {

    @Bean("sourceDataSourceProperties")
    @ConfigurationProperties("spring.datasource.source")
    public DataSourceProperties sourceDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean("sourceDataSource")
    public DataSource sourceDataSource(
        @Qualifier("sourceDataSourceProperties") DataSourceProperties sourceDataSourceProperties) {
        return sourceDataSourceProperties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
    }

    @Bean(name = "sourceEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean sourceEntityManagerFactory(
        EntityManagerFactoryBuilder builder, @Qualifier("sourceDataSource") DataSource sourceDataSource) {
        return builder
            .dataSource(sourceDataSource)
            .packages(SourceEntityPackage.class)
            .build();
    }

    @Bean
    public PlatformTransactionManager sourceTransactionManager(
        final @Qualifier("sourceEntityManagerFactory") LocalContainerEntityManagerFactoryBean sourceEntityManagerFactory) {
        return new JpaTransactionManager(sourceEntityManagerFactory.getObject());
    }
}

