package fi.ari.bootweb.allin.security;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.nimbusds.jwt.proc.DefaultJWTClaimsVerifier;
import com.nimbusds.jwt.proc.JWTClaimsSetVerifier;
import fi.ari.bootweb.allin.config.JwtConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.oauth2.jose.jws.JwsAlgorithms;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static org.springframework.util.CollectionUtils.isEmpty;

/**
 * Security component to create strong HMAC signed (access) tokens for authorization.
 * Provides token creation and verification.
 */
@Component
@Primary
@ConditionalOnBean( JwtConfig.class )
public class HMACJws implements JwtDecoder {
    protected JWSSigner signer;
    protected JwtDecoder decoder;
    protected JwtConfig config;

    public HMACJws(JwtConfig config) throws KeyLengthException {
        this.config = config;
        signer = new MACSigner(config.secret);
        if (!signer.supportedJWSAlgorithms().contains(JWSAlgorithm.HS512)) {
            throw new IllegalArgumentException("Secret is too small for signing algorithm (512 bits required)");
        }
        decoder = createDecoder(config.audience, config.secret);
    }

    public String create(String subject, List<String> scopes, List<String> roles, Date expire) throws JOSEException {
        JWTClaimsSet.Builder claims = new JWTClaimsSet.Builder()
            .issuer(config.issuer)
            .issueTime(new Date())
            .audience(config.audience)
            .subject(subject);
        if ( expire != null ) claims.expirationTime(expire);
        if ( ! isEmpty(scopes) ) claims.claim("scp", String.join(" ", scopes) );
        if ( ! isEmpty(roles) ) claims.claim("roles", roles);
        return signAndSerialize(claims.build());
    }

    public String signAndSerialize(JWTClaimsSet jwtClaimsSet) throws JOSEException {
        SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS512), jwtClaimsSet);
        signedJWT.sign(signer);

        return signedJWT.serialize();
    }

    @Override
    public Jwt decode(String token) throws JwtException {
        return decoder.decode(token);
    }

    public static JwtDecoder createDecoder(String audience, String secret) {
        JWTClaimsSetVerifier<SecurityContext> verifier = new DefaultJWTClaimsVerifier<>(audience, null, Set.of("sub"));
        SecretKey secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), JwsAlgorithms.HS512);
        return NimbusJwtDecoder
            .withSecretKey(secretKey)
            .jwtProcessorCustomizer( proc -> proc.setJWTClaimsSetVerifier(verifier) )
            .macAlgorithm(MacAlgorithm.HS512)
            .build();
    }
}
