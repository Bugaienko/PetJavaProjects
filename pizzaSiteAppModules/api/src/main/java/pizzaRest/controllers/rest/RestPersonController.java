package pizzaRest.controllers.rest;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import pizzaRest.dto.AuthenticationDTO;
import pizzaRest.dto.PersonDTO;

import pizzaRest.models.Person;
import pizzaRest.security.JwtUtil;
import pizzaRest.services.PersonService;
import pizzaRest.util.ErrorResponse;
import pizzaRest.util.NotFoundException;
import pizzaRest.util.PersonNotCreatedException;
import pizzaRest.util.PersonValidator;


import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Sergii Bugaienko
 */

@RestController
@RequestMapping("/api/users")
public class RestPersonController {


    private final PersonService personService;
    private final PersonValidator personValidator;
    private final ModelMapper modelMapper;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;


    @Autowired
    public RestPersonController(PersonService personService, PersonValidator personValidator, ModelMapper modelMapper, JwtUtil jwtUtil, AuthenticationManager authenticationManager) {
        this.personService = personService;
        this.personValidator = personValidator;


        this.modelMapper = modelMapper;
        this.jwtUtil = jwtUtil;

        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/registration")
    public Map<String, String> createPerson(@RequestBody @Valid PersonDTO personDTO, BindingResult bindingResult) {

        Person person = convertToPerson(personDTO);

        personValidator.validate(person, bindingResult);

        if (bindingResult.hasErrors()) {
            StringBuilder errorMsg = new StringBuilder();
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                errorMsg.append(error.getField()).append(" - ")
                        .append(error.getDefaultMessage())
                        .append(";");
            }
            throw new PersonNotCreatedException(errorMsg.toString());
        }

        personService.register(person);
        String token = jwtUtil.generateToken(person.getUsername());

        Map<String, String> map = new HashMap<>();
        map.put("jwt-token", token);
        return map;

    }

    @PostMapping("/login")
    public Map<String, String> performLogin(@RequestBody AuthenticationDTO authenticationDTO) {
        UsernamePasswordAuthenticationToken authInputToken = new UsernamePasswordAuthenticationToken(
                authenticationDTO.getUsername(), authenticationDTO.getPassword());

        try {
            authenticationManager.authenticate(authInputToken);
        } catch (BadCredentialsException e){
            Map<String, String> map = new HashMap<>();
            map.put("message", "Incorrect credentials");
            return map;
        }

        String token = jwtUtil.generateToken(authenticationDTO.getUsername());
        Map<String, String> resp = new HashMap<>();
        resp.put("jwt-token", token);
        return resp;

    }

    @GetMapping("/all")
    public List<PersonDTO> getPersons() {
        return personService.findAll().stream()
                .map(this::convertToDtoPerson).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public PersonDTO getPerson(@PathVariable("id") int personId) {
        return convertToDtoPerson(personService.findOne(personId));
    }

    @PostMapping("")
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid PersonDTO personDto, BindingResult bindingResult) {
        System.out.println("Pst start");
        if (bindingResult.hasErrors()) {
            //TODO
            StringBuilder errorMsg = new StringBuilder();
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                errorMsg.append(error.getField()).append(" - ")
                        .append(error.getDefaultMessage())
                        .append(";");
            }
            throw new PersonNotCreatedException(errorMsg.toString());
        }
        Person person = convertToPerson(personDto);
        personService.register(person);
        //отправляем ответ с пустым телом и статусом 200
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(NotFoundException e) {
        ErrorResponse response = new ErrorResponse(
                "Object with this id wasn't found", System.currentTimeMillis()
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleCreatedException(PersonNotCreatedException e) {
        ErrorResponse response = new ErrorResponse(
                e.getMessage(), System.currentTimeMillis()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    private Person convertToPerson(PersonDTO personDto) {
//        ModelMapper mapper = new ModelMapper(); // Create Bean in config

        return modelMapper.map(personDto, Person.class);

//        return new Person(personDto.getUsername(), personDto.getPassword(), personDto.getEmail(), personDto.getAvatar());
    }

    private PersonDTO convertToDtoPerson(Person person) {
        return modelMapper.map(person, PersonDTO.class);
    }


}
