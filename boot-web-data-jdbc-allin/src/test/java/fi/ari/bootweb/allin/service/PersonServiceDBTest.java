package fi.ari.bootweb.allin.service;

import fi.ari.bootweb.allin.config.JwtConfig;
import fi.ari.bootweb.allin.entity.Person;
import fi.ari.bootweb.allin.repository.PersonRepository;
import fi.ari.bootweb.allin.test.TestBase;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE )
@AutoConfigurationPackage(basePackages = "fi.ari.bootweb.allin") // Either here or in class loaded in @ContextConfiguration
@ContextConfiguration(classes = { PersonService.class, SimpleMeterRegistry.class })
@EnableConfigurationProperties({ JwtConfig.class }) // Without this, JwtConfig will be empty
@TestPropertySource({"classpath:application-test.properties","classpath:application-test-db.properties"})
@Sql("/persons.sql")
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
