package org.example.service;

import org.example.domain.Person;
import org.example.exception.NotFoundException;
import org.example.repository.PersonRepository;
import org.example.repository.UserRepository;
import org.example.validation.ValidationStrategy;

import java.util.List;

public class PersonService {

    private final PersonRepository repo;
    private final UserRepository userRepo;
    private final ValidationStrategy<Person> validator;

    public PersonService(PersonRepository repo, UserRepository userRepo, ValidationStrategy<Person> validator) {
        this.repo = repo;
        this.userRepo = userRepo;
        this.validator = validator;
    }

    /**
     * Inserts a new Person into the database after validation.
     */
    public Person addPerson(Person p) {
        validator.validate(p);
        return repo.save(p);
    }

    /**
     * Returns a Person by ID or throws if missing.
     */
    public Person getPerson(int id) {
        return repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Person", id));
    }

    /**
     * Deletes a Person by ID.
     */
    public void deletePerson(int id) {
        repo.delete(id);
        userRepo.delete(id);
    }

    /**
     * Returns all Persons.
     */
    public List<Person> getAllPersons() {
        return repo.findAll();
    }

    /**
     * Returns a page of persons.
     */
    public List<Person> getPersonPage(int page, int size) {
        int offset = (page - 1) * size;
        return repo.findPage(size, offset);
    }

    /**
     * Returns total number of persons.
     */
    public long getPersonCount() {
        return repo.count();
    }
}
