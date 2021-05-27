package fi.ari.bootweb.allin.controller.test;

import fi.ari.bootweb.allin.config.JwtConfig;
import fi.ari.bootweb.allin.controller.PersonController;
import fi.ari.bootweb.allin.security.AuthorityCheck;
import fi.ari.bootweb.allin.service.PersonService;
import fi.ari.bootweb.allin.test.SetupDataJdbcTest;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;

import javax.sql.DataSource;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/** Test Controller, via Service and Repository to actual DB */
@SpringJUnitWebConfig({ PersonService.class, SimpleMeterRegistry.class, AuthorityCheck.class, TestWebSecurityConfig.class })
@SetupDataJdbcTest
@SetupWebMvcTest(PersonController.class)
@Import({ PersonController.class })
@EnableConfigurationProperties({ JwtConfig.class })
@TestPropertySource({"classpath:application-test.properties","classpath:application-test-db.properties"})
@Sql("/persons.sql")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PersonControllerDBTest extends PersonControllerTestBase {
	@Autowired
	DataSource dataSource;

	@Test
	void checkDataSource() throws SQLException {
		assertNotNull(dataSource);
		System.out.println("Connection URL : "+ dataSource.getConnection().getMetaData().getURL());
	}
}
