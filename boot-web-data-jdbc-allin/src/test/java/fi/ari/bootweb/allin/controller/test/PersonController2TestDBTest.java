package fi.ari.bootweb.allin.controller.test;

import fi.ari.bootweb.allin.config.JwtConfig;
import io.micrometer.core.instrument.MeterRegistry;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import javax.sql.DataSource;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.AUTO_CONFIGURED;

/** Test Controller, via Service and Repository to actual DB */
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase( replace = AUTO_CONFIGURED )
@ActiveProfiles({ "test" })
@Sql("/persons.sql")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PersonController2TestDBTest extends PersonControllerTestBase {
	@Autowired
	protected MeterRegistry meterRegistry;
	@Autowired
	protected JwtConfig jwtConfig;
	@Autowired
	DataSource dataSource;

	@Test
	public void checkConfig() {
		assertNotNull(meterRegistry);
		assertNotNull(jwtConfig);
		assertNotNull(jwtConfig.secret);
	}

	@Test
	void checkDataSource() throws SQLException {
		assertNotNull(dataSource);
		System.out.println("Connection URL : "+ dataSource.getConnection().getMetaData().getURL());
	}
}
