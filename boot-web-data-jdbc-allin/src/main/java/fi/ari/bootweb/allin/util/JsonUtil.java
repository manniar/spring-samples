package fi.ari.bootweb.allin.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class JsonUtil {
	protected static final ObjectMapper PRETTY_MAPPER = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

	public static String prettyPrint(Object o) {
		try {
			return PRETTY_MAPPER.writeValueAsString(o);
		} catch (JsonProcessingException e) {
			return "{\"error\",\"" + e + "\"}";
		}
	}

	public static LazyJsonSerialized toPrettyJson(Object o) { return new LazyJsonSerialized(o); }

	static class LazyJsonSerialized {
		Object o;
		public LazyJsonSerialized(Object o) { this.o = o; }
		@Override
		public String toString() {
			return prettyPrint(o);
		}
	}
}
