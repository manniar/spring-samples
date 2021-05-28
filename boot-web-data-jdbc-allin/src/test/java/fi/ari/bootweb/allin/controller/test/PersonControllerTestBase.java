package fi.ari.bootweb.allin.controller.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import fi.ari.bootweb.allin.config.JwtConfig;
import fi.ari.bootweb.allin.entity.Person;
import fi.ari.bootweb.allin.test.TestBase;
import io.micrometer.core.instrument.MeterRegistry;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public abstract class PersonControllerTestBase extends TestBase {
	protected static final ObjectMapper mapper = new ObjectMapper();

	@Value("${allin.token.secret}")
	protected String secret;

	@Autowired
	protected WebApplicationContext webAppContext;
	@Autowired
	protected MeterRegistry meterRegistry;
	@Autowired
	protected JwtConfig jwtConfig;

	@Autowired
	protected MockMvc mockMvc;

	/*	@BeforeAll
	public void setupMockMvc() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();
	} /**/

	@Test
	public void autowiredShouldNotBeNull() {
		assertNotNull(secret);
		assertNotNull(webAppContext);
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
	public void getAllWithoutAuthority() throws Exception {
		mockMvc.perform(get("/person/all"))
				.andExpect(status().isForbidden());
	}
	@Test
	public void getAllWithoutAuthentication() throws Exception {
		mockMvc.perform(get("/person/all"))
				.andExpect(status().isUnauthorized());
	}

	@Test
	@WithMockUser(username = "test", authorities = {"ROLE_Person.waltari"})
	public void savePersonWithAuthority() throws Exception {
		Person person = new Person("Mika", "Waltari");
		mockMvc.perform(post("/person")
				.contentType(APPLICATION_JSON)
				.content(mapper.writeValueAsString(person)))
				.andExpect(status().isOk());
	}

	@Test
	@WithMockUser(username = "test", authorities = {"ROLE_Person.Tolstoi"})
	public void savePersonWithoutAuthority() throws Exception {
		Person person = new Person("Mika", "Waltari");
		mockMvc.perform(post("/person")
				.contentType(APPLICATION_JSON)
				.content(mapper.writeValueAsString(person)))
				.andExpect(status().isForbidden());
	}

	@Test
	public void savePersonWithoutAuthentication() throws Exception {
		Person person = new Person("Mika", "Waltari");
		mockMvc.perform(post("/person")
				.contentType(APPLICATION_JSON)
				.content(mapper.writeValueAsString(person)))
				.andExpect(status().isUnauthorized());
	}

	@Test
	public void getCount() throws Exception {
		mockMvc.perform(get("/person/count"))
				.andExpect(status().isOk());
	}

}
