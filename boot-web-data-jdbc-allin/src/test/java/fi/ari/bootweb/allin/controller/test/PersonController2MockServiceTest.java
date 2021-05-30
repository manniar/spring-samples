package fi.ari.bootweb.allin.controller.test;

import fi.ari.bootweb.allin.controller.PersonController;
import fi.ari.bootweb.allin.entity.Person;
import fi.ari.bootweb.allin.security.AuthorityCheck;
import fi.ari.bootweb.allin.security.WebSecurityConfig;
import fi.ari.bootweb.allin.service.PersonService;
import fi.ari.bootweb.allin.test.MockTestConfig;
import fi.ari.bootweb.allin.test.TestBase;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/** Test Controller, via Service to Mock Repository */
/*
@SpringBootTest(classes = MockTestConfig.class)
@AutoConfigureMockMvc
@AutoConfigurationWithoutDB
@ActiveProfiles({ "test" })
/**/
@WebMvcTest(PersonController.class)
@ContextConfiguration( classes = { PersonController.class, AuthorityCheck.class, WebSecurityConfig.class })
@ActiveProfiles({ "test" })
@TestPropertySource( locations = "classpath:application-test.properties" )

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PersonController2MockServiceTest extends PersonControllerTestBase {
	public static final List<Person> list = List.of(
		new Person("John", "John"),
		new Person("Alex", "kolenchiski"),
		new Person("Steve", "Waugh")
	);

	@MockBean
	protected PersonService personService;

	@BeforeAll
	void initServiceMock() {
		when(personService.findAll()).thenReturn(list);
	}

	@Test
	void checkMockConfig() {
		assertNotNull(personService);
	}

}
