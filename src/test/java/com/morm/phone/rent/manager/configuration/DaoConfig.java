package com.morm.phone.rent.manager.configuration;

import java.util.Properties;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import net.ttddyy.dsproxy.listener.ChainListener;
import net.ttddyy.dsproxy.listener.DataSourceQueryCountListener;
import net.ttddyy.dsproxy.listener.logging.SLF4JQueryLoggingListener;
import net.ttddyy.dsproxy.support.ProxyDataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@TestPropertySource(locations = {"classpath:application.properties"}, properties = {
    "db.url=jdbc:hsqldb:mem:testdb;sql.syntax_mys=true"})
@EnableJpaRepositories("com.morm.phone.rent.manager.repository")
@PersistenceContext
@EnableTransactionManagement
public class DaoConfig {

  @Bean
  public DataSource dataSource() {
    ChainListener listener = new ChainListener();
    SLF4JQueryLoggingListener loggingListener = new SLF4JQueryLoggingListener();
    listener.addListener(loggingListener);
    listener.addListener(new DataSourceQueryCountListener());
    return ProxyDataSourceBuilder
        .create(new EmbeddedDatabaseBuilder()
            .generateUniqueName(true)
            .setType(EmbeddedDatabaseType.HSQL)
            .setScriptEncoding("UTF-8")
            .ignoreFailedDrops(true)
            .build())
        .name("DS-Proxy")
        .listener(listener)
        .build();
  }


  @Bean(name = "entityManagerFactory")
  public LocalSessionFactoryBean getSessionFactory() {
    LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
    sessionFactory.setPackagesToScan("com.morm.phone.rent.manager.domain");
    sessionFactory.setDataSource(dataSource());
    Properties properties = new Properties();
    properties.setProperty("hibernate.connection.driver_class", "org.hsqldb.jdbcDriver");
    properties.setProperty("hibernate.connection.url",
        "jdbc:hsqldb:mem:testdb;sql.syntax_mys=true");
    properties.setProperty("hibernate.connection.username", "sa");
    properties.setProperty("hibernate.connection.password", "");
    properties.setProperty("hibernate.dialect", "org.hibernate.dialect.HSQLDialect");
    properties.setProperty("hibernate.show_sql", "false");
    properties.setProperty("hibernate.format_sql", "true");
    properties.setProperty("hibernate.use_sql_comments", "true");
    properties.setProperty("hbm2ddl.auto", "validate");
    properties.setProperty("hibernate.hbm2ddl.auto", "create");
    properties.setProperty("hibernate.jdbc.batch_size", "50");
    properties.setProperty("hibernate.id.new_generator_mappings", "false");
    sessionFactory.setHibernateProperties(properties);

    return sessionFactory;
  }

  @Bean(name = "transactionManager")
  public PlatformTransactionManager transactionManager() {
    final HibernateTransactionManager transactionManager = new HibernateTransactionManager();
    transactionManager.setSessionFactory(getSessionFactory().getObject());
    return transactionManager;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

}
