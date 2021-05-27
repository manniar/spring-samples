package fi.ari.bootweb.allin.test;

import org.springframework.boot.test.autoconfigure.data.jdbc.AutoConfigureDataJdbc;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
// Cherrypicked from @DataJdbcTest
@Transactional
//@AutoConfigureCache
@AutoConfigureDataJdbc
@EnableJdbcRepositories(basePackages = "fi.ari.bootweb.allin.repository")
public @interface SetupDataJdbcTest {}
