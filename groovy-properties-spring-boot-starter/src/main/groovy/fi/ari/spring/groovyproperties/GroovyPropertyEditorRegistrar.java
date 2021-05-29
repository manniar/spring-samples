package fi.ari.spring.groovyproperties;

import org.springframework.beans.PropertyEditorRegistrar;
import org.springframework.beans.PropertyEditorRegistry;
import org.springframework.beans.factory.config.CustomEditorConfigurer;

import java.util.List;

public class GroovyPropertyEditorRegistrar implements PropertyEditorRegistrar {
	final List<Class> classes;

	public static CustomEditorConfigurer createCustomEditorConfigurer(List<Class> classes) {
		PropertyEditorRegistrar[] pers = { new GroovyPropertyEditorRegistrar(classes) };
		CustomEditorConfigurer configurer = new CustomEditorConfigurer();
		configurer.setPropertyEditorRegistrars(pers);
		return configurer;
	}

	public GroovyPropertyEditorRegistrar(List<Class> classes) {
		this.classes = classes;
	}

	@Override
	public void registerCustomEditors(PropertyEditorRegistry registry) {
		classes.forEach(clazz -> {
			GroovyTypeEditor editor = new GroovyTypeEditor(clazz);
			registry.registerCustomEditor(clazz, editor);
		});
	}
}
