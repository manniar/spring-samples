package fi.ari.spring.groovyproperties;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target( ElementType.TYPE )
@Documented
public @interface GroovyProperties {
	Class[] value() default {};
}
