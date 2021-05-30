
package fi.ari.bootweb.allin.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import java.util.Date;
import java.util.List;

import static java.util.Collections.EMPTY_LIST;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@SuppressWarnings("unused")
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private static final Logger log = LoggerFactory.getLogger(WebSecurityConfig.class);

    @Autowired(required = false)
    HMACJws hmacJws;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        if ( hmacJws != null ) {
            log.info("Configuring JWT");
            http.oauth2ResourceServer().jwt()
                .decoder(hmacJws)
                .jwtAuthenticationConverter(new JwtAuthenticationConverter());

            //// For demonstration purposes
            System.out.println("\nUSER TOKEN : " +
                    hmacJws.create("test", List.of("Person.User"), List.of("Person.1","Person.2","Person.3"), new Date(System.currentTimeMillis() + 3_600_000)) );
            System.out.println("\nADMIN TOKEN : " +
                    hmacJws.create("test", List.of("Person.Admin"), EMPTY_LIST, new Date(System.currentTimeMillis() + 3_600_000)) );
        }

        http.authorizeRequests( req -> req.anyRequest().permitAll() );
        http.csrf().disable();

        http.sessionManagement().sessionCreationPolicy(STATELESS);
    }
}
