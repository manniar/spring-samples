package fi.ari.bootweb.allin.controller.test;

import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
// Cherrypicked from @WebMvcTest
@ImportAutoConfiguration
@AutoConfigureWebMvc
@AutoConfigureMockMvc
public @interface SetupWebMvcTest {
	/** Controller classes to configure */
	@AliasFor(annotation = ImportAutoConfiguration.class, attribute = "classes")
	Class<?>[] value() default {};
}
