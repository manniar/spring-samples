package fi.ari.springgroovyproperty;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;

import java.time.Instant;
import java.util.function.Function;
import java.util.function.Supplier;


@SpringBootApplication
public class SpringGroovyPropertyApplication implements CommandLineRunner {
	static JsonParser jsonParser = JsonParserFactory.getJsonParser();

	@Value("${script.when}")
	Instant when;
	@Value("${script.hello}")
	Function<String,String> hello;
	@Value("${script.json}")
	Supplier<String> jsonSource;
	@Value("${script.convert}")
	Function convert;

	public static void main(String[] args) {
		SpringApplication.run(SpringGroovyPropertyApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("Hello : " + hello.apply("Ari"));
		System.out.println("When : " + when);
		jsonParser.parseList( jsonSource.get() )
			.forEach( user -> System.out.println("Item : " + convert.apply(user)) );
	}
}
