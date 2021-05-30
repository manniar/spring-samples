package fi.ari.spring.groovyproperties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.time.Instant;
import java.util.function.Function;
import java.util.function.Supplier;

@Configuration
public class GroovyConfigurationDynamic {

	@GroovyProperty
	@Value("${script.hello}")
	Function<String,String> hello;

	@Value("${script.json}") @GroovyProperty
	Supplier<String> jsonSource;

	@GroovyProperty @Value("${script.convert}")
	Function<Object,String> convert;

	Instant when;

	@Value("${script.when}") @GroovyProperty
	public void setWhen(Instant when) {
		this.when = when;
	}

}
