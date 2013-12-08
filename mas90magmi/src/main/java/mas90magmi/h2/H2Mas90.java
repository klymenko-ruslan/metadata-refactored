package mas90magmi.h2;

import com.healthmarketscience.jackcess.Database;
import com.healthmarketscience.jackcess.DatabaseBuilder;
import java.io.File;
import org.h2.jdbcx.JdbcDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 *
 * @author jrodriguez
 */
public class H2Mas90 {

    /**
     * In-memory H2 database.
     */
    private final JdbcTemplate h2db = new JdbcTemplate(new JdbcDataSource() {
        {
            setURL("jdbc:h2:mem");
        }
    }, false);
    
    private final Database mas90Db;

    public H2Mas90(File mas90DbFile) throws Exception {

        // Open the mas90 database
        mas90Db = DatabaseBuilder.open(mas90DbFile);
        
        h2db.execute("CREATE TABLE customer (id VARCHAR(20), email VARCHAR(255))");
        h2db.execute("ALTER TABLE customer ADD PRIMARY KEY id");
        
        h2db.execute("CREATE TABLE product (id VARCHAR(20), price DECIMAL)");
        h2db.execute("ALTER TABLE customer ADD PRIMARY KEY id");
        
        h2db.execute(
                  "CREATE TABLE prices (\n"
                + "  id VARCHAR(20),\n"
                + "  price DECIMAL,\n"
                + "  price INTEGER,\n"
                + ")");
        h2db.execute("ALTER TABLE customer ADD PRIMARY KEY id");
        
      
    }
}
