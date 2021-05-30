package fi.ari.spring.groovyproperties;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = { SpringBootTestApplication.class, GroovyConfigurationDynamic.class })
@TestPropertySource( locations = "/application-test.properties" )
public class SpringBootDynamicTest {

	static JsonParser jsonParser = JsonParserFactory.getJsonParser();

	@Autowired
	GroovyConfigurationDynamic config;

	@Test
	void contextLoads() {}

	@Test
	void configLoads() {
		assertNotNull(config);
		assertNotNull(config.when);
		System.out.println("When : " + config.when);
	}

	@Test
	public void testHello() {
		String greeting = config.hello.apply("Ari");
		System.out.println("Hello : " + greeting);
		assertNotNull( greeting );
		assertEquals( "Hello Ari", greeting );
	}

	@Test
	public void testWhen() {
		System.out.println("When : " + config.when);
		assertTrue( config.when.isAfter( Instant.now() ) );
	}

	@Test
	public void testJson() {
		List<String> list = jsonParser.parseList( config.jsonSource.get() ).stream()
			.map( user -> config.convert.apply(user) )
			.collect(Collectors.toList());
		list.forEach( System.out::println );
		assertTrue( list.size() >= 10 );
	}

}
