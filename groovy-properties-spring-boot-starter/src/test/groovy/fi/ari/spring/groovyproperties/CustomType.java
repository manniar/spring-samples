package fi.ari.spring.groovyproperties;

@GroovyProperty
public class CustomType {
	protected final String name;
	protected final int value;

	public CustomType(String name, int value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public int getValue() {
		return value;
	}
}
