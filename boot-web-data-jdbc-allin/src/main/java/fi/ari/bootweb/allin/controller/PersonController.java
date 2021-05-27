package fi.ari.bootweb.allin.controller;

import fi.ari.bootweb.allin.entity.Person;
import fi.ari.bootweb.allin.service.EntityNotFoundException;
import fi.ari.bootweb.allin.service.PersonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path="/person", produces = APPLICATION_JSON_VALUE)
@ApiResponses(value = {
	@ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema())),
	@ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema())),
	@ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema()))
})
@SuppressWarnings("unused")
public class PersonController {

	@Autowired
	PersonService personService;

	@GetMapping(path = "/count")
	@Operation(summary = "Returns a count of persons", description = "${persons.info.get-count:}")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "List of persons") })
	public int count() {
		return personService.findAll().size();
	}

	/** Special case for authority check */
	@PostMapping(consumes = APPLICATION_JSON_VALUE)
	@Validated
	@Operation(summary = "Saves a person", description = "${persons.info.save:}")
	@ApiResponses(value = { @ApiResponse(responseCode = "201", description = "New person") })
	@PreAuthorize("@authorityCheck.isAuthorized(#person)")
	public Person save(@RequestBody Person person) {
		return personService.save(person);
	}

	@GetMapping(path = "/id/{id}")
	@Operation(summary = "Returns a person", description = "${persons.info.get-by-id:}")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "A person") })
	@PreAuthorize("hasAnyAuthority('SCOPE_Person.Admin','SCOPE_Person.User')")
	public Person getById(@PathVariable("id") int id) throws EntityNotFoundException {
		return personService.getById(id);
	}

	@GetMapping(path = "/all")
	@Operation(summary = "Returns all persons", description = "${persons.info.find-all:}")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "All persons") })
	@PreAuthorize("hasAnyAuthority('SCOPE_Person.Admin','SCOPE_Person.User')")
	public List<Person> findAll() {
		return personService.findAll();
	}

	@DeleteMapping(path = "/id/{id}")
	@Operation(summary = "Deletes a person", description = "${persons.info.delete-by-id:}")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "All persons") })
	@PreAuthorize("hasAuthority('SCOPE_Person.Admin')")
	public void deleteById(@PathVariable("id") int id) {
		personService.deleteById(id);
	}
}
