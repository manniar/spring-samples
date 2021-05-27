package fi.ari.bootweb.allin.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import static org.springframework.util.CollectionUtils.isEmpty;

/** Methods for checking authorities of current user. */
@Component
public class AuthorityCheck {
	static final Logger log = LoggerFactory.getLogger(AuthorityCheck.class);

	protected AuthorityCheck() {}

	/** Tells if current user is authorized for given person. */
	@SuppressWarnings("unused") // Is used in @PreAuthorize annotations
	public boolean isAuthorized(int personId) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if ( authentication == null || ! authentication.isAuthenticated() || isEmpty(authentication.getAuthorities()) ) return false;
		String expectedAuthority = "ROLE_Person." + personId;
		return authentication.getAuthorities().stream().anyMatch( auth -> expectedAuthority.equalsIgnoreCase(auth.getAuthority()) );
	}

}
