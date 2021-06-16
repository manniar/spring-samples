package fi.ari.bootweb.allin.repository;

import lombok.AllArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@ToString
public class Column {
	public final int ordinal;
	public final String name;
}
