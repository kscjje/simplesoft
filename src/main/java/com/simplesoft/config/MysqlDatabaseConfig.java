package com.simplesoft.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.simplesoft.util.EncryptUtils;
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
    
    private String aesKey = "bsdhsdudghksehddhksxhdgkqjeonsan";

	
	@Bean(name = "mysqlDataSource", destroyMethod = "close")
	@Primary
	//@ConfigurationProperties(prefix = "spring.mysql.datasource")
	public DataSource mysqlDataSource() throws Exception { 
		return DataSourceBuilder.create()
				.url(url)
				.driverClassName(driverClassName)
//				.username(EncryptUtils.AES256_Decrypt(username, aesKey))
//				.password(EncryptUtils.AES256_Decrypt(password, aesKey))
				.username(username)
				.password(password)
				.type(HikariDataSource.class).build(); 
	}
	
	@Bean(name = "mysqlSqlSessionFactory")
	@Primary
	public SqlSessionFactory mysqlSqlSessionFactory(@Qualifier("mysqlDataSource") DataSource mysqlDataSource, ApplicationContext applicationContext) throws Exception { 
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean(); 
		sqlSessionFactoryBean.setDataSource(mysqlDataSource);		
		sqlSessionFactoryBean.setConfigLocation(applicationContext.getResource("classpath:mybatis-config.xml"));
		sqlSessionFactoryBean.setMapperLocations(applicationContext.getResources("classpath*:simple/mapper/**/*.xml")); 
		
		
		
		return sqlSessionFactoryBean.getObject(); 
	}

	@Bean(name = "mysqlSqlSessionTemplate") 
	@Primary
	public SqlSessionTemplate mysqlSqlSessionTemplate(SqlSessionFactory mysqlSqlSessionFactory) throws Exception { 
		return new SqlSessionTemplate(mysqlSqlSessionFactory); 
	}

}
