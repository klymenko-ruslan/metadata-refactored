package com.turbointernational.metadata;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * @author dmytro.trunykov@zorallabs.com
 */
@Configuration
//@formatter:off
@EnableJpaRepositories(
        basePackages = {"com.turbointernational.metadata.entity" },
        entityManagerFactoryRef = "metadataEntityManager",
        transactionManagerRef = "transactionManager")
//@formatter:on
public class MetadataDbConfig {

    @Bean
    @ConfigurationProperties(prefix = "entitymanager.metadata")
    public JpaConfigProps jpaConfigPropsMetadata() {
        return new JpaConfigProps();
    }

    @Primary
    @Bean
    @ConfigurationProperties(prefix = "datasource.metadata")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Primary
    @Bean(name = "transactionManager")
    public PlatformTransactionManager transactionManagerJPA(EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }

    // TODO: it looks that this transaction manager is useless and can be replaced by 'transactionManager'
    @Bean(name = "transactionManagerMetadata")
    public PlatformTransactionManager transactionManagerMetadata() {
        return new DataSourceTransactionManager(dataSource());
    }

    @Primary
    @Bean(name = "metadataEntityManager")
    public LocalContainerEntityManagerFactoryBean metadataEntityManagerFactory(EntityManagerFactoryBuilder builder) {
        DataSource dataSource = dataSource();
        JpaConfigProps jpaConfigProps = jpaConfigPropsMetadata();
        return builder.dataSource(dataSource).persistenceUnit("metadata")
                .packages("com.turbointernational.metadata.entity").properties(jpaConfigProps.toJpaPropersies())
                .build();
    }

}
