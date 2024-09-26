package com.simplesoft.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariDataSource;



@Configuration 
@MapperScan(value="com.simplesoft", annotationClass=MysqlConnMapper.class, sqlSessionFactoryRef="mysqlSqlSessionFactory")
@EnableTransactionManagement
public class MysqlDatabaseConfig {

	@Value("${spring.datasource.driver-class-name}")
    private String driverClassName;
	
	@Value("${spring.datasource.url}")
    private String url;
	
	@Value("${spring.datasource.username}")
    private String username;
    
    @Value("${spring.datasource.password}")
    private String password;

//    private String aesKey = "bsdhsdudghksehddhksxhdgkqjeonsan";

    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource")
    DataSourceProperties dataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @ConfigurationProperties("spring.datasource.hikari")
    HikariDataSource dataSource(DataSourceProperties properties) {
        return properties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
    }
    
    @Bean(name = "mysqlSqlSessionFactory")
    @Primary
    SqlSessionFactory mysqlSqlSessionFactory(DataSource mysqlDataSource, ApplicationContext applicationContext) throws Exception { 
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean(); 
		sqlSessionFactoryBean.setDataSource(mysqlDataSource);
		sqlSessionFactoryBean.setConfigLocation(applicationContext.getResource("classpath:mybatis-config.xml"));
		sqlSessionFactoryBean.setMapperLocations(applicationContext.getResources("classpath*:simple/mapper/*.xml")); 
		
		return sqlSessionFactoryBean.getObject(); 
	}

    @Bean(name = "mysqlSqlSessionTemplate")
    @Primary
    SqlSessionTemplate mysqlSqlSessionTemplate(SqlSessionFactory mysqlSqlSessionFactory) throws Exception { 
		return new SqlSessionTemplate(mysqlSqlSessionFactory); 
	}

}
