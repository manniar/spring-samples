package fi.ari.bootweb.allin.service;

import fi.ari.bootweb.allin.entity.Person;
import fi.ari.bootweb.allin.repository.PersonRepository;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonService {
	protected PersonRepository personRepository;
	protected Counter counter;

	public PersonService(PersonRepository personRepository, MeterRegistry meters) {
		System.out.println("-- PersonService.init : " + personRepository + ", " + meters);
		this.personRepository = personRepository;
		counter = Counter.builder("findall.count")
			.description("Count of findAll queries")
			.baseUnit("times")
			.register(meters);
	}

	public List<Person> findByFirstName(String firstName) {
		return personRepository.findByFirstName(firstName);
	}

	public <S extends Person> S save(S person) {
		return personRepository.save(person);
	}

	public Person getById(int id) throws EntityNotFoundException {
		return personRepository.findById(id).orElseThrow( () -> new EntityNotFoundException() );
	}

	public List<Person> findAll() {
		counter.increment();
		return personRepository.findAll();
	}

	public void deleteById(int id) {
		personRepository.deleteById(id);
	}

}
