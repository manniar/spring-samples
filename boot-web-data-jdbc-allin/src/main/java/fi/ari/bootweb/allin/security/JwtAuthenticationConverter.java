package fi.ari.bootweb.allin.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.DefaultOAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import java.util.*;
import java.util.stream.Collectors;

import static fi.ari.bootweb.allin.util.JsonUtil.toPrettyJson;

/**
 * Jwt to Authentication converter featuring:
 *    <li>Convert scope/scp claims to authorizations with "SCOPE_" prefix and roles claim with "ROLE_" prefix.</li>
 *    <li>Takes Principal name from <code>"upn"</code>, <code>"name"</code> or <code>"sub"</code> claim,
 *       in this order until non-null value is found.</li>
 *    <li>Logs conversions on debug level</li>
 * </ul>
 */
public class JwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {
	private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationConverter.class);

	protected List<String> principalNameClaims = List.of("upn","name","sub");
	protected List<Converter<Jwt, Collection<GrantedAuthority>>> authoritiesConverters;

	public JwtAuthenticationConverter() {
		authoritiesConverters = List.of(
			new JwtGrantedAuthoritiesConverter(), // scope/scp claim to "SCOPE_" authorities
			createConverter("roles", "ROLE_") );
	}

	@Override
	public AbstractAuthenticationToken convert(final Jwt jwt) {
		log.debug("convert {}", toPrettyJson(jwt.getClaims()));
		OAuth2AccessToken accessToken = new OAuth2AccessToken(
			OAuth2AccessToken.TokenType.BEARER, jwt.getTokenValue(), jwt.getIssuedAt(), jwt.getExpiresAt() );

		Set<GrantedAuthority> authorities = authoritiesConverters.stream()
			.map( c -> c.convert(jwt) )
			.filter( Objects::nonNull )
			.flatMap( Collection::stream )
			.collect( Collectors.toSet() );

		Optional<Object> principalName = principalNameClaims.stream()
			.map( jwt::getClaim )
			.filter( Objects::nonNull )
			.findFirst();
		OAuth2AuthenticatedPrincipal principal = new DefaultOAuth2AuthenticatedPrincipal(
			principalName.orElse("token").toString(), jwt.getClaims(), authorities);

		log.debug("convert result: name {}, authorities {}", principal.getName(), authorities);
		return new BearerTokenAuthentication(principal, accessToken, authorities);
	}

	public static JwtGrantedAuthoritiesConverter createConverter(String authoritiesClaimName, String authorityPrefix) {
		JwtGrantedAuthoritiesConverter converter = new JwtGrantedAuthoritiesConverter();
		converter.setAuthoritiesClaimName(authoritiesClaimName);
		converter.setAuthorityPrefix(authorityPrefix);
		return converter;
	}
}
