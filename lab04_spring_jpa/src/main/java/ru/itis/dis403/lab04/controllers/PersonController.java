package ru.itis.dis403.lab04.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.itis.dis403.lab04.model.Admin;
import ru.itis.dis403.lab04.model.Client;
import ru.itis.dis403.lab04.model.Person;
import ru.itis.dis403.lab04.model.Phone;
import ru.itis.dis403.lab04.service.PersonService;
import ru.itis.dis403.lab04.service.PhoneService;

import java.util.List;

@Controller
public class PersonController {

    private PersonService personService;
    private PhoneService phoneService;

    public PersonController(PersonService personService,  PhoneService phoneService) {
        this.personService = personService;
        this.phoneService = phoneService;
    }

    @GetMapping("/persons/add")
    public String person() {
        return "personForm";
    }

    @PostMapping("/persons/add")
    public String savePerson(
            @RequestParam String name,
            @RequestParam String phone,
            @RequestParam String role,
            @RequestParam String extra
    ) {
        Phone phoneEntity = new Phone();
        phoneEntity.setNumber(phone);
        phoneService.save(phoneEntity);

        Person person;

        if ("ADMIN".equals(role)) {
            Admin admin = new Admin();
            admin.setEmail(extra);
            admin.setRole(role);
            person = admin;
        } else {
            Client client = new Client();
            client.setAddress(extra);
            client.setRole("USER");
            person = client;
        }

        person.setName(name);
        person.setPhone(phoneEntity);

        personService.save(person);

        return "redirect:/persons";
    }

    @GetMapping("/persons")
    public String persons(Model model) {
        List<Person> persons = personService.findAll();
        model.addAttribute("persons", persons);
        return "persons";
    }
}
