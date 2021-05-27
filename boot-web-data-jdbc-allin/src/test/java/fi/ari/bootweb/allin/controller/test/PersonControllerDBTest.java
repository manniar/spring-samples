package fi.ari.bootweb.allin.controller.test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import fi.ari.bootweb.allin.config.JwtConfig;
import fi.ari.bootweb.allin.controller.PersonController;
import fi.ari.bootweb.allin.service.PersonService;
import fi.ari.bootweb.allin.test.TestBase;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.boot.test.autoconfigure.data.jdbc.AutoConfigureDataJdbc;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.util.NestedServletException;

import javax.sql.DataSource;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.util.Assert.isInstanceOf;

/** Test Controller, via Service and Repository to actual DB */
@ExtendWith(SpringExtension.class)

// Cherrypicked from @DataJdbcTest
@Transactional
@AutoConfigureCache
@AutoConfigureDataJdbc
// @AutoConfigureTestDatabase // Add this if you want autoconfigured test database. Does not work with @EnableGlobalMethodSecurity though.
@EnableJdbcRepositories(basePackages = "fi.ari.bootweb.allin.repository")

// Cherrypicked from @SpringJUnitWebConfig
@ContextConfiguration( classes = { PersonService.class, SimpleMeterRegistry.class })
@WebAppConfiguration // ("src/main/webapp")

@EnableWebMvc
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Import({ PersonController.class })
@EnableConfigurationProperties({ JwtConfig.class })
@TestPropertySource({"classpath:application-test.properties","classpath:application-test-db.properties"})
@Sql("/persons.sql")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PersonControllerDBTest extends TestBase {
	static final ObjectMapper mapper = new ObjectMapper();

	@Value("${allin.token.secret}")
	String secret;

	@Autowired
	ApplicationContext applicationContext;
	@Autowired
	JwtConfig jwtConfig;
	@Autowired
	MeterRegistry meterRegistry;
	@Autowired
	WebApplicationContext webAppContext;
	@Autowired
	DataSource dataSource;

	protected MockMvc mockMvc;

	@BeforeAll
	public void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();
	}

	@Test
	void autowiredShouldNotBeNull() throws SQLException {
		assertNotNull(applicationContext);
		assertNotNull(secret);
		assertNotNull(meterRegistry);
		assertNotNull(jwtConfig);
		assertNotNull(jwtConfig.secret);
		assertNotNull(dataSource);
		System.out.println("Connection URL : "+ dataSource.getConnection().getMetaData().getURL());
	}

	@Test
	@WithMockUser(username = "test", authorities = {"SCOPE_Person.Admin"})
	public void getAllWithAuthority() throws Exception {
		mockMvc.perform(get("/person/all"))
			.andExpect(status().isOk())
			.andDo( handler -> {
				String jsonStr = handler.getResponse().getContentAsString();
				JsonNode json = mapper.readTree(jsonStr);
				isInstanceOf( ArrayNode.class, json );
				assertEquals( 4, json.size() );
			});
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
