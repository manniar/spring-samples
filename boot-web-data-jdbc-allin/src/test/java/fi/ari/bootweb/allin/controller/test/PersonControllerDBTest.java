package fi.ari.bootweb.allin.controller.test;

import fi.ari.bootweb.allin.config.JwtConfig;
import fi.ari.bootweb.allin.controller.PersonController;
import fi.ari.bootweb.allin.security.AuthorityCheck;
import fi.ari.bootweb.allin.service.PersonService;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.data.jdbc.AutoConfigureDataJdbc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/** Test Controller, via Service and Repository to actual DB */
@ExtendWith(SpringExtension.class)

// Cherrypicked from @DataJdbcTest
@Transactional
@AutoConfigureDataJdbc
// @AutoConfigureTestDatabase // Add this if you want autoconfigured test database. Does not work with @EnableGlobalMethodSecurity though.
@EnableJdbcRepositories(basePackages = "fi.ari.bootweb.allin.repository")

// Cherrypicked from @SpringJUnitWebConfig
@ContextConfiguration( classes = { PersonService.class, SimpleMeterRegistry.class, PersonControllerDBTest.Config.class, AuthorityCheck.class })
@WebMvcTest(PersonController.class)

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

	@TestConfiguration
	@EnableGlobalMethodSecurity(prePostEnabled = true)
	public static class Config {}
}
