package fi.ari.bootweb.allin.controller.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import fi.ari.bootweb.allin.config.JwtConfig;
import fi.ari.bootweb.allin.controller.SimpleController;
import fi.ari.bootweb.allin.entity.Person;
import fi.ari.bootweb.allin.security.AuthorityCheck;
import fi.ari.bootweb.allin.test.MockTestConfig;
import io.micrometer.core.instrument.MeterRegistry;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.endpoint.EndpointAutoConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.jdbc.JdbcRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.autoconfigure.groovy.template.GroovyTemplateAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.*;

/** Test Controller, via Service to Mock Repository */
@SpringBootTest(classes = MockTestConfig.class)
@AutoConfigureMockMvc
@ActiveProfiles({ "test" })
@EnableAutoConfiguration(exclude = {
	GroovyTemplateAutoConfiguration.class,
	DataSourceAutoConfiguration.class,
	FlywayAutoConfiguration.class,
	JdbcRepositoriesAutoConfiguration.class,
	DataSourceTransactionManagerAutoConfiguration.class
})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SimpleControllerMock2Test {
	protected static final ObjectMapper mapper = new ObjectMapper();

	@Value("${allin.token.secret}")
	protected String secret;

	@Autowired
	protected MockTestConfig mockConfig;

	@Test
	void checkMockConfig() {
		assertNotNull(mockConfig);
	}

	@Autowired
	protected JwtConfig jwtConfig;
	@Autowired
	protected MeterRegistry meterRegistry;
	@Autowired
	protected WebApplicationContext webAppContext;

	@Test
	public void autowiredShouldNotBeNull() {
		assertNotNull(secret);
		assertNotNull(meterRegistry);
		assertNotNull(jwtConfig);
		assertNotNull(jwtConfig.secret);
	}

	@Autowired
	protected MockMvc mockMvc;

	/*	@BeforeAll
	public void setupMockMvc() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();
	} /**/


	@Test
	@WithMockUser(username = "test", authorities = {"SCOPE_Person.Admin"})
	public void getAllWithAuthority() throws Exception {
		mockMvc.perform(get("/simple/all"))
				.andExpect(status().isOk());
	}

	@Test
	@WithMockUser(username = "test", authorities = {"SCOPE_Person.Dull"})
	public void getAllWithoutAuthority() throws Exception {
		MvcResult result = mockMvc.perform(get("/simple/all"))
				.andExpect(status().isForbidden())
				.andReturn();
		System.out.println("-- MvcResult : " + result);
		/*
		assertThrows(
			NestedServletException.class,
			() -> mockMvc.perform(get("/simple/all"))
				.andExpect(status().isOk()) );
		/**/
	}
	/*
		@Test
		public void getAllWithoutAuthentication() {
			assertThrows(
					NestedServletException.class,
					() -> mockMvc.perform(get("/simple/all")) );
		}
	/**/
	@Test
	@WithMockUser(username = "test", authorities = {"ROLE_Person.waltari"})
	public void savePersonWithAuthority() throws Exception {
		Person person = new Person("Mika", "Waltari");
		mockMvc.perform(post("/simple")
				.contentType(APPLICATION_JSON)
				.content(mapper.writeValueAsString(person)))
				.andExpect(status().isOk());
	}

	@Test
	@WithMockUser(username = "test", authorities = {"ROLE_Person.Tolstoi"})
	public void savePersonWithoutAuthority() throws Exception {
		Person person = new Person("Mika", "Waltari");
		mockMvc.perform(post("/simple")
				.contentType(APPLICATION_JSON)
				.content(mapper.writeValueAsString(person)))
				.andExpect(status().isForbidden());
	}

	/*
	@Test
	public void savePersonWithoutAuthentication() {
		Person person = new Person("Mika", "Waltari");
		assertThrows(
				ServletException.class,
				() -> mockMvc.perform(post("/simple")
						.contentType(APPLICATION_JSON)
						.content(mapper.writeValueAsString(person))) );
		}
	/**/
	@Test
	public void getCount() throws Exception {
		mockMvc.perform(get("/simple/count"))
				.andExpect(status().isOk());
	}

}
