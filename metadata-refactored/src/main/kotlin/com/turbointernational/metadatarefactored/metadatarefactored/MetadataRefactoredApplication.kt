package com.turbointernational.metadatarefactored.metadatarefactored

import com.arangodb.springframework.annotation.EnableArangoRepositories
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Primary
import com.arangodb.ArangoDB
import com.arangodb.springframework.config.AbstractArangoConfiguration


@SpringBootApplication
@EnableArangoRepositories(basePackages = arrayOf( "com.turbointernational"))
class MetadataRefactoredApplication(): AbstractArangoConfiguration() {

    @Bean(name = arrayOf("accessDataSource"))
    @ConfigurationProperties(prefix = "access.datasource")
    fun accessDatasource() = DataSourceBuilder.create().build()

    @Bean(name = arrayOf("mysqlDataSource"))
    @ConfigurationProperties(prefix = "mysql.datasource")
    fun mysqlDatasource() = DataSourceBuilder.create().build()

    @Bean(name = arrayOf("postgresDataSource"))
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource")
    fun postgresDatasource() = DataSourceBuilder.create().build()

    @Bean
    fun objectMapper(): ObjectMapper {
        val objectMapper = ObjectMapper()
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
        return objectMapper
    }

    override fun arango(): ArangoDB.Builder {
        return ArangoDB.Builder().host("localhost", 8529).user("root").password("root")
    }

    override fun database() = "=metadata-refactored"

}

fun main(args: Array<String>) {
    runApplication<MetadataRefactoredApplication>(*args)
}
