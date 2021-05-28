package fi.ari.bootweb.allin.test;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.jdbc.JdbcRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@EnableAutoConfiguration(exclude = {
	DataSourceAutoConfiguration.class,
	FlywayAutoConfiguration.class,
	JdbcRepositoriesAutoConfiguration.class,
	DataSourceTransactionManagerAutoConfiguration.class
})
public @interface AutoConfigurationWithoutDB {}
