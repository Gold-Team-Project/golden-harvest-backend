package com.teamgold.goldenharvest.common.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = {
        "com.teamgold.goldenharvest.domain.notification.query.mapper",
        "com.teamgold.goldenharvest.domain.master.query.mapper",
        "com.teamgold.goldenharvest.domain.inventory.query.mapper",
        "com.teamgold.goldenharvest.domain.customersupport.query.mapper",
        "com.teamgold.goldenharvest.domain.sales.query.application.mapper"
})
public class MyBatisConfig {

    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource, ApplicationContext applicationContext) throws Exception {
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        // application.yaml에 설정된 mapper-locations 값을 참조합니다.
        sessionFactory.setMapperLocations(applicationContext.getResources("classpath:mappers/**/*.xml"));
        // application.yaml에 설정된 type-aliases-package 값을 참조합니다.
        sessionFactory.setTypeAliasesPackage("com.teamgold.goldenharvest.domain");
        // application.yaml에 설정된 map-underscore-to-camel-case 값을 참조합니다.
        org.apache.ibatis.session.Configuration mybatisConfig = new org.apache.ibatis.session.Configuration();
        mybatisConfig.setMapUnderscoreToCamelCase(true);
        sessionFactory.setConfiguration(mybatisConfig);
        return sessionFactory.getObject();
    }

    @Bean
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}