package fi.ari.bootweb.allin.controller.test;

import fi.ari.bootweb.allin.test.AutoConfigurationWithoutDB;
import fi.ari.bootweb.allin.test.MockTestConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/** Test Controller, via Service to Mock Repository */
@SpringBootTest(classes = MockTestConfig.class)
@AutoConfigureMockMvc
@AutoConfigurationWithoutDB
@ActiveProfiles({ "test" })
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PersonControllerMockTest extends PersonControllerTestBase {
	@Autowired
	protected MockTestConfig mockConfig;

	@Test
	void checkMockConfig() {
		assertNotNull(mockConfig);
	}

}
