package fi.ari.bootweb.allin.controller.test;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import javax.sql.DataSource;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/** Test Controller, via Service and Repository to actual DB */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles({ "test","test-db" })
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
