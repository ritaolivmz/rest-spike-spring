package hello;

import hello.controller.PersonController;
import hello.model.Person;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        PersonController personController = new PersonController();

        Person person = personController.findPersonById(1);
        System.out.println(person.toString());

        List<Person> people = personController.listAllPeople();
        System.out.println("There are " + people.size() + " people subscribed.");
    }
}