package fi.ari.bootweb.allin.config;

import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "allin.token")
@ConditionalOnProperty( name = "allin.token.secret" )
@Setter @ToString
public class JwtConfig {
	public String issuer = "http://bootweb.ari.fi/";
	public String audience = "http://bootweb.ari.fi/";
	public String secret;
}
