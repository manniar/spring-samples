package fi.ari.bootweb.allin.service;

import fi.ari.bootweb.allin.config.JwtConfig;
import fi.ari.bootweb.allin.entity.Person;
import fi.ari.bootweb.allin.repository.PersonRepository;
import fi.ari.bootweb.allin.test.SetupDataJdbcTest;
import fi.ari.bootweb.allin.test.TestBase;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/** Test Service via Repository to actual DB */
@ExtendWith(SpringExtension.class)
@SetupDataJdbcTest
@AutoConfigureTestDatabase // Add this if you want autoconfigured test database. Does not work with @EnableGlobalMethodSecurity though.
@ContextConfiguration(classes = { PersonService.class, SimpleMeterRegistry.class })
@EnableConfigurationProperties({ JwtConfig.class }) // Without this, JwtConfig will be empty
@TestPropertySource({"classpath:application-test.properties","classpath:application-test-db.properties"})
@Sql("/persons.sql")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PersonServiceDBTest extends TestBase {

	@Value("${allin.token.secret}")
	String secret;

	@Autowired
	ApplicationContext applicationContext;
	@Autowired
	JwtConfig jwtConfig;
	@Autowired
	MeterRegistry meterRegistry;
	@Autowired
	PersonService service;
	@Autowired
	PersonRepository repo;
	@Autowired
	DataSource dataSource;

	@Test
	void autowiredShouldNotBeNull() throws SQLException {
		assertNotNull(applicationContext);
		assertNotNull(secret);
		assertNotNull(meterRegistry);
		assertNotNull(jwtConfig);
		assertNotNull(jwtConfig.secret);
		assertNotNull(service);
		assertNotNull(repo);
		System.out.println("Connection URL : "+ dataSource.getConnection().getMetaData().getURL());
	}

	@Test
	public void testFindAllPersons() {
		List<Person> result = service.findAll();
		assertNotNull( result );
		assertEquals( 4, result.size() );
	}

	@Test
	public void testFindPersonByName() throws EntityNotFoundException {
		List<Person> persons = service.findByFirstName("Dante");
		assertEquals( 1, persons.size() );
		Person person = persons.get(0);
		assertEquals( "Alighieri", person.getLastName() );
	}
}
