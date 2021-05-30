package fi.ari.spring.groovyproperties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.time.Instant;
import java.util.function.Function;
import java.util.function.Supplier;

@Configuration
@GroovyProperties(Supplier.class)
public class GroovyConfigurationPreDefined {

	@Value("${script.hello}")
	Function<String,String> hello;

	@Value("${script.json}")
	Supplier<String> jsonSource;

	@Value("${script.convert}")
	Function<Object,String> convert;

	Instant when;

	@Value("${script.when}")
	public void setWhen(Instant when) {
		this.when = when;
	}

}
