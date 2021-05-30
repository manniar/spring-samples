package fi.ari.spring.groovyproperties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.CustomEditorConfigurer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Configuration
@ConditionalOnProperty(name = "groovy.property.classes")
public class GroovyPropertyEditorConfiguration {
	private static final Logger log = LoggerFactory.getLogger(GroovyPropertyEditorConfiguration.class);

	@Bean
	public CustomEditorConfigurer customEditorConfigurer(@Value("${groovy.property.classes}") List<String> classNames) {
		log.info("Configure for classes: {}", String.join(",", classNames));
		Set<Class> classes = classNames.stream().map(className -> {
				try {
					return Class.forName(className);
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
					return null;
				}
			})
			.filter( Objects::nonNull )
			.collect(Collectors.toCollection( HashSet::new ));
		return GroovyPropertyEditorRegistrar.createCustomEditorConfigurer(classes);
	}

}
