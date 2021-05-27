package fi.ari.bootweb.allin.security;

import fi.ari.bootweb.allin.entity.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

import static org.springframework.util.CollectionUtils.isEmpty;

/** Methods for checking authorities of current user. */
@Component
public class AuthorityCheck {
	static final Logger log = LoggerFactory.getLogger(AuthorityCheck.class);

	protected AuthorityCheck() {}

	/** Tells if current user is authorized for given person. */
	@SuppressWarnings("unused") // Is used in @PreAuthorize annotations
	public boolean isAuthorized(Person person) {
		System.out.println("-- AuthorityCheck.isAuthorized: " + person);
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if ( authentication == null || ! authentication.isAuthenticated() || isEmpty(authentication.getAuthorities()) ) return false;
		List<String> expectedAuthorities = List.of("scope_person.admin", "role_person." + person.getLastName().toLowerCase());
		return authentication.getAuthorities().stream().anyMatch( auth -> expectedAuthorities.contains(auth.getAuthority().toLowerCase()) );
	}

}
