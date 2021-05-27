package fi.ari.bootweb.allin.test;

import fi.ari.bootweb.allin.config.JwtConfig;
import fi.ari.bootweb.allin.entity.Person;
import fi.ari.bootweb.allin.repository.PersonRepository;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

import javax.annotation.PostConstruct;
import java.util.List;

import static org.mockito.Mockito.when;

//@EnableWebSecurity
@EnableConfigurationProperties({ JwtConfig.class })
@Import({ SimpleMeterRegistry.class })
@EnableGlobalMethodSecurity(prePostEnabled = true)
@TestConfiguration // or @Configuration, seems no difference
public class MockTestConfig {
	public static final List<Person> list = List.of(
		new Person("John", "John"),
		new Person("Alex", "kolenchiski"),
		new Person("Steve", "Waugh")
	);

	public MockTestConfig() {
		System.out.println("-- MockTestConfig.init");
	}

	@Mock
	PersonRepository repo;

	@PostConstruct
	public void postConstruct() {
		MockitoAnnotations.openMocks(this);
		when(repo.findAll()).thenReturn(list);
		System.out.println("-- MockTestConfig.postConstruct : " + repo);
	}

	@Bean
	public PersonRepository getPersonRepository() {
		System.out.println("-- MockTestConfig.PersonRepository : " + repo);
		return repo;
	}
}
