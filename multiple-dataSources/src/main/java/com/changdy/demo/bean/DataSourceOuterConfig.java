package com.changdy.demo.bean;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = "com.changdy.demo.mapper.outer", sqlSessionTemplateRef = "outerSqlSessionTemplate")
public class DataSourceOuterConfig {

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.outer")
    public DataSource outerDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    public SqlSessionFactory outerSqlSessionFactory(@Qualifier("outerDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapper/outer/*.xml"));
        return bean.getObject();
    }

    @Bean
    public DataSourceTransactionManager outerTransactionManager(@Qualifier("outerDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    public SqlSessionTemplate outerSqlSessionTemplate(@Qualifier("outerSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}