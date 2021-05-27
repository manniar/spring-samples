package fi.ari.bootweb.allin.controller.test;

import fi.ari.bootweb.allin.config.JwtConfig;
import fi.ari.bootweb.allin.controller.PersonController;
import fi.ari.bootweb.allin.service.PersonService;
import fi.ari.bootweb.allin.test.MockTestConfig;
import fi.ari.bootweb.allin.test.TestBase;
import io.micrometer.core.instrument.MeterRegistry;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.util.NestedServletException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/** Test Controller, via Service to Mock Repository */
@SpringJUnitWebConfig({ MockTestConfig.class, PersonService.class })
@EnableWebMvc
@Import({ PersonController.class })
@TestPropertySource(value = "classpath:application-test.properties")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PersonControllerMockTest extends TestBase {
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
	@Autowired
	protected WebApplicationContext webAppContext;

	protected MockMvc mockMvc;

	@BeforeAll
	public void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();
	}

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
	@WithMockUser(username = "test", authorities = {"SCOPE_Person.Admin"})
	public void getAllWithAuthority() throws Exception {
		mockMvc.perform(get("/person/all"))
			.andExpect(status().isOk());
	}

	@Test
	@WithMockUser(username = "test", authorities = {"SCOPE_Person.Dull"})
	public void getAllWithoutAuthority() {
		assertThrows(
			NestedServletException.class,
			() -> mockMvc.perform(get("/person/all"))
				.andExpect(status().isOk()) );
	}

	@Test
	public void getAllWithoutAuthentication() {
		assertThrows(
			NestedServletException.class,
			() -> mockMvc.perform(get("/person/all"))
				.andExpect(status().isOk()) );
	}

	@Test
	public void getCount() throws Exception {
		mockMvc.perform(get("/person/count"))
				.andExpect(status().isOk());
	}

}
