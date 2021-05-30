package fi.ari.spring.groovyproperties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.time.Instant;
import java.util.function.Function;
import java.util.function.Supplier;

@Configuration
public class GroovyConfiguration {
	@Value("${script.when}")
	Instant when;
	@Value("${script.hello}")
	Function<String,String> hello;
	@Value("${script.json}")
	Supplier<String> jsonSource;
	@Value("${script.convert}")
	Function<Object,String> convert;
}
