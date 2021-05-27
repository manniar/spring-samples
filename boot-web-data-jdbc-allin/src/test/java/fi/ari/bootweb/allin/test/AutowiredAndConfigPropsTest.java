package fi.ari.bootweb.allin.test;

import fi.ari.bootweb.allin.config.JwtConfig;
import io.micrometer.core.instrument.MeterRegistry;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/** Test @ConfigurationProperties construction and injection,
 * importing beans from @TestConfiguration and injections via @Value and @Autowired. */
@SpringJUnitConfig({ MockTestConfig.class })
@EnableConfigurationProperties({ JwtConfig.class }) // Without this, JwtConfig will be empty
@TestPropertySource(value = "classpath:application-test.properties")
public class AutowiredAndConfigPropsTest extends TestBase {
	@Value("${allin.token.secret}")
	String secret;

	@Autowired
	ApplicationContext applicationContext;
	@Autowired
	MockTestConfig mockConfig;
	@Autowired
	JwtConfig jwtConfig;
	@Autowired
	MeterRegistry meterRegistry;

	@Test
	void autowiredShouldNotBeNull() {
		assertNotNull(applicationContext);
		assertNotNull(secret);
		assertNotNull(mockConfig);
		assertNotNull(meterRegistry);
		assertNotNull(jwtConfig);
		assertNotNull(jwtConfig.secret);
	}
}
