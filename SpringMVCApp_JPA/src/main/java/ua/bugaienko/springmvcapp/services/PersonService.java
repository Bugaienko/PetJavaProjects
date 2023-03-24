package ua.bugaienko.springmvcapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.bugaienko.springmvcapp.models.Person;
import ua.bugaienko.springmvcapp.repositories.PersonRepository;

import java.util.List;
import java.util.Optional;

/**
 * @author Sergii Bugaienko
 */

@Service
@Transactional(readOnly = true)
public class PersonService {

    private final PersonRepository personRepository;

    @Autowired
    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public List<Person> findAll() {
        return personRepository.findAll();
    }

    public Person findOne(int id) {
        Optional<Person> foundPerson = personRepository.findById(id);
        return foundPerson.orElse(null);
    }

    @Transactional
    public void save(Person person) {
            personRepository.save(person);
    }

    @Transactional
    public void update(int id, Person updatedData) {
        Person person = findOne(id);
        person.setFullName(updatedData.getFullName());
        person.setYearBirthday(updatedData.getYearBirthday());
        personRepository.save(person);
    }

    @Transactional
    public  void delete(int id){
        personRepository.deleteById(id);
    }
}
