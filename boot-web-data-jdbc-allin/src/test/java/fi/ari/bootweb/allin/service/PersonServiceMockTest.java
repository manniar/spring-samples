package fi.ari.bootweb.allin.service;

import fi.ari.bootweb.allin.config.JwtConfig;
import fi.ari.bootweb.allin.entity.Person;
import fi.ari.bootweb.allin.repository.PersonRepository;
import fi.ari.bootweb.allin.test.MockTestConfig;
import fi.ari.bootweb.allin.test.TestBase;
import io.micrometer.core.instrument.MeterRegistry;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import static fi.ari.bootweb.allin.test.MockTestConfig.list;


/** Test Service via Repository to mock DB */
@SpringJUnitConfig({ MockTestConfig.class, PersonService.class })
@EnableConfigurationProperties({ JwtConfig.class }) // Without this, JwtConfig will be empty
@TestPropertySource(value = "classpath:application-test.properties")
public class PersonServiceMockTest extends TestBase {
	@Autowired PersonService service;
	@Autowired PersonRepository repo;

	@Value("${allin.token.secret}")
	String secret;

	@Autowired
	protected ApplicationContext applicationContext;
	@Autowired
	protected MockTestConfig mockConfig;
	@Autowired
	protected JwtConfig jwtConfig;
	@Autowired
	protected MeterRegistry meterRegistry;

	@Test
	void autowiredShouldNotBeNull() {
		assertNotNull(applicationContext);
		assertNotNull(secret);
		assertNotNull(mockConfig);
		assertNotNull(meterRegistry);
		assertNotNull(jwtConfig);
		assertNotNull(jwtConfig.secret);
	}

	@Test
	public void testFindAllPersons() {
		List<Person> result = service.findAll();
		assertEquals(3, result.size());
		verify(repo, times(1)).findAll();
	}

	@Test
	public void testFindPersonById() throws EntityNotFoundException {
		when(repo.findById(1)).thenReturn(Optional.ofNullable(list.get(0)));
		Person person = service.getById(1);
		assertNotNull(person);
		assertEquals(list.get(0).getFirstName(), person.getFirstName());
	}

}
