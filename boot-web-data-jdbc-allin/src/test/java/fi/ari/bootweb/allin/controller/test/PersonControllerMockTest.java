package fi.ari.bootweb.allin.controller.test;

import fi.ari.bootweb.allin.config.JwtConfig;
import fi.ari.bootweb.allin.controller.PersonController;
import fi.ari.bootweb.allin.security.AuthorityCheck;
import fi.ari.bootweb.allin.service.PersonService;
import fi.ari.bootweb.allin.test.MockTestConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/** Test Controller, via Service to Mock Repository */
@SpringJUnitWebConfig({ MockTestConfig.class, AuthorityCheck.class, TestWebSecurityConfig.class })
@SetupWebMvcTest(PersonController.class)
@Import({ PersonController.class, PersonService.class })
@EnableConfigurationProperties({ JwtConfig.class })
@TestPropertySource(value = "classpath:application-test.properties")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PersonControllerMockTest extends PersonControllerTestBase {
	@Autowired
	protected MockTestConfig mockConfig;

	@Test
	void checkMockConfig() {
		assertNotNull(mockConfig);
	}

}
