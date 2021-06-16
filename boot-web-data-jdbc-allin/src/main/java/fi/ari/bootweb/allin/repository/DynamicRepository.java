package fi.ari.bootweb.allin.repository;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jdbc.core.JdbcAggregateOperations;
import org.springframework.data.jdbc.repository.support.SimpleJdbcRepository;
import org.springframework.data.mapping.PersistentEntity;
import org.springframework.data.util.Pair;
import org.springframework.data.util.Streamable;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.apache.commons.lang3.ArrayUtils.toArray;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

public class DynamicRepository {
	protected final JdbcTemplate jdbc;
	protected final DataSource ds;
	ColumnMapRowMapper mapper = new ColumnMapRowMapper();

	protected String table;
	protected List<Column> columns;
	protected Column[] pk;

	@Autowired
	public DynamicRepository(DataSource ds, JdbcTemplate jdbc) {
		this.ds = ds;
		this.jdbc = jdbc;
	}

	@Transactional
	public Map<String,Object> save(Map<String,Object> record) {

		return entityOperations.save(record);
	}

	@Transactional
	public Map<String,Object> insert(Map<String,Object> record) {

		return entityOperations.save(record);
	}

	@Transactional
	public Map<String,Object> update(Map<String,Object> record) {
		Pair<String,Object>[] updCols = columns.stream()
			.map( col -> Pair.of(col.name, record.get(col.name)) )
			.filter( pair -> pair.getSecond() != null )
			.toArray( size -> new Pair[size] );
		String updateSql = Arrays.stream(updCols).map( pair -> pair.getFirst() + " = ?" ).collect(joining(", "));
		List values = Arrays.stream(updCols).map( pair -> pair.getSecond() ).collect(toList());
		Arrays.stream(pk).forEach( col -> values.add( record.get( col.name ) ) );
		jdbc.update("UPDATE " + table + " SET " + updateSql + " WHERE " + pkWhere(), values.toArray());
		return findById(record.get("")).orElse(null);
	}

	@Transactional
	public Iterable<Map<String,Object>> saveAll(Iterable<Map<String,Object>> records) {
		return Streamable.of(records).stream() //
				.map(this::save) //
				.collect(Collectors.toList());
	}

	public Optional<Map<String,Object>> findById(Object ... id) {
		if ( id.length != pk.length ) throw new IllegalArgumentException("findById expected " + pk.length + " id columns but received " + id.length + " arguments");
		var list = jdbc.queryForList("SELECT * FROM " + table + " WHERE " + pkWhere(), id);
		return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
	}

	protected String pkWhere() {
		return IntStream.range(0, pk.length).mapToObj( x -> pk[x].name + " = ?" ).collect(Collectors.joining(" AND "));
	}

	public List<Map<String,Object>> findAll() {
		return jdbc.queryForList("SELECT * FROM " + table);
	}

	@Transactional
	public void deleteById(Object ... id) {
		if ( id.length != pk.length ) throw new IllegalArgumentException("findById expected " + pk.length + " id columns but received " + id.length + " arguments");
		jdbc.update("DELETE FROM " + table + " WHERE " + pkWhere(), id);
	}

	@Transactional
	public void delete(Map<String,Object> record) {
		Object[] id = IntStream.range(0, pk.length).mapToObj( x -> record.get(pk[x].name) ).toArray();
		jdbc.update("DELETE FROM " + table + " WHERE " + pkWhere(), id);
	}

	@Transactional
	@SuppressWarnings("unchecked")
	public void deleteAll(Iterable<Map<String,Object>> records) {
		records.forEach(it -> delete(it));
	}

	@Transactional
	public void deleteAll() {
		jdbc.update("DELETE FROM " + table);
	}

}
