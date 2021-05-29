package fi.ari.spring.groovyproperties;

import groovy.lang.GroovyShell;

import java.beans.PropertyEditorSupport;

public class GroovyTypeEditor<T> extends PropertyEditorSupport {
    Class<T> clazz;
    static final GroovyShell shell = new GroovyShell();


    public GroovyTypeEditor(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public String getAsText() {
        T value = (T) getValue();
        return value == null ? "" : value.toString();
    }
    
    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        T value = GroovySamUtil.objectFromScript(text, clazz );
        setValue(value);
    }

}
