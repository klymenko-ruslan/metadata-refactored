package com.turbointernational.metadata;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * @author dmytro.trunykov@zorallabs.com
 */
@Configuration
//@formatter:off
@EnableJpaRepositories(
        basePackages = {"com.turbointernational.mas90.entity" },
        entityManagerFactoryRef = "mas90EntityManager", transactionManagerRef = "mas90TransactionManager")
//@formatter:on
public class Mas90DbConfig {

    @Bean
    @ConfigurationProperties(prefix = "entitymanager.mas90")
    public JpaConfigProps jpaConfigPropsMas90() {
        return new JpaConfigProps();
    }

    @Bean(name = "dataSourceMas90")
    @ConfigurationProperties(prefix = "datasource.mas90")
    public DataSource mas90DataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "transactionManagerMas90")
    public PlatformTransactionManager transactionManagerMas90() {
        return new DataSourceTransactionManager(mas90DataSource());
    }

    @Bean(name = "mas90EntityManager")
    public LocalContainerEntityManagerFactoryBean mas90EntityManagerFactory(EntityManagerFactoryBuilder builder) {
        DataSource dataSource = mas90DataSource();
        JpaConfigProps jpaConfigProps = jpaConfigPropsMas90();
        return builder.dataSource(dataSource).persistenceUnit("mas90").packages("com.turbointernational.mas90.entity")
                .properties(jpaConfigProps.toJpaPropersies()).build();
    }

}
