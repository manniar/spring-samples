package fi.ari.bootweb.allin.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
@ConditionalOnBean(DataSource.class)
@Slf4j
public class SchemaRepository {
//  private final JdbcTemplate jdbc;
    private final DataSource ds;

    @Autowired
    public SchemaRepository(DataSource ds) {
        this.ds = ds;
    }

    public List<Column> getTable(String table) throws SQLException {
        DatabaseMetaData dbmd = ds.getConnection().getMetaData();
        ResultSet rs = dbmd.getColumns(null, null, table, null);
        List<Column> result = new ArrayList<>();
        int pos = 0;
        while ( rs.next() ) {
            result.add( new Column(pos++, rs.getString("COLUMN_NAME")) );
        }
        return result;
    }

}
