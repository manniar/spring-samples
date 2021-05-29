package fi.ari.spring.groovyproperties

import java.util.function.Function

class GroovySamUtil {
	protected static final GroovyShell shell = new GroovyShell();
	Function<String,String> hello = hello -> "Hello $hello"
	static <T> T objectFromScript(String script, Class<T> clz) {
		Object x = shell.evaluate(script);
		return Eval.x(x, '(' + clz.getName() + ') x');
	}
}
