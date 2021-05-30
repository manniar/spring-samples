package fi.ari.spring.groovyproperties;

import org.springframework.beans.PropertyEditorRegistrar;
import org.springframework.beans.PropertyEditorRegistry;
import org.springframework.beans.factory.config.CustomEditorConfigurer;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class GroovyPropertyEditorRegistrar implements PropertyEditorRegistrar {
	protected static Set<Class> allTypes = new HashSet<>();
	protected Set<Class> types;

	public static CustomEditorConfigurer createCustomEditorConfigurer(Set<Class> classes) {
		System.out.println("-- GroovyPropertyEditorRegistrar.createCustomEditorConfigurer");
		PropertyEditorRegistrar[] pers = { new GroovyPropertyEditorRegistrar(classes) };
		CustomEditorConfigurer configurer = new CustomEditorConfigurer();
		configurer.setPropertyEditorRegistrars(pers);
		return configurer;
	}

	public Set<String> getTypeNames() {
		return types.stream().map( Class::getName).collect(Collectors.toSet());
	}

	public static GroovyPropertyEditorRegistrar of(Set<Class> types) {
		Set<Class> newTypes = new HashSet<>();
		types.forEach( type -> {
			if ( ! allTypes.contains(type) ) {
				newTypes.add(type);
			}
		});
		return newTypes.isEmpty() ? null : new GroovyPropertyEditorRegistrar(newTypes);
	}

	public GroovyPropertyEditorRegistrar(Set<Class> types) {
		this.types = types;
		allTypes.addAll(types);
	}

	@Override
	public void registerCustomEditors(PropertyEditorRegistry registry) {
		types.forEach(clazz -> {
			GroovyTypeEditor editor = new GroovyTypeEditor(clazz);
			registry.registerCustomEditor(clazz, editor);
		});
	}
}
