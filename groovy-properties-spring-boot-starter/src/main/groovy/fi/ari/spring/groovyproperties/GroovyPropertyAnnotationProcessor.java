package fi.ari.spring.groovyproperties;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.HashSet;

@Component
public class GroovyPropertyAnnotationProcessor implements InstantiationAwareBeanPostProcessor {
    protected static final boolean PARSE_METHOD_PARAMS = false;

    @Autowired
    ConfigurableListableBeanFactory beanFactory;

    @Override
    public Object postProcessBeforeInstantiation(final Class<?> type, final String name) throws BeansException {
        final HashSet<Class> types = new HashSet<Class>();

        GroovyProperties gpAnn = AnnotationUtils.findAnnotation(type, GroovyProperties.class);
        if ( gpAnn != null ) {
            System.out.println("\n\n- - - " + name + " : " + Arrays.asList(gpAnn.value()) + "\n\n" );
            types.addAll( Arrays.asList(gpAnn.value()) );
        }

        ReflectionUtils.doWithFields(type, field -> {
            if ( field.isAnnotationPresent(GroovyProperty.class)) {
                types.add(field.getType());
            }
        });

        ReflectionUtils.doWithMethods(type, m -> {
            boolean isGroovyProp = m.isAnnotationPresent(GroovyProperty.class);
            Parameter[] params = m.getParameters();
            if ( isGroovyProp || PARSE_METHOD_PARAMS ) {
                for (Parameter param : params) {
                    if ( isGroovyProp || param.isAnnotationPresent(GroovyProperty.class) ) {
                        types.add(param.getType());
                    }
                }
            }
        });

        GroovyPropertyEditorRegistrar registrar = GroovyPropertyEditorRegistrar.of(types);
        if ( registrar != null ) {
            System.out.println("-- For bean: " + name + " (" + type.getSimpleName() + ") register types " + registrar.getTypeNames() );
            beanFactory.addPropertyEditorRegistrar(registrar);
        }
        return null;
    }

}
