package hello.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import hello.model.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.hal.Jackson2HalModule;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PersonController {

    private static Logger logger = LoggerFactory.getLogger(PersonController.class);

    public Person findPersonById(long id) {
        logger.info("Method [findPersonById] with parameter id [" + id + "] invoked.");

        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject("http://localhost:8070/people/"+id, Person.class);
    }

    public List<Person> listAllPeople() {
        logger.info("Method [listAllPeople] invoked.");

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.registerModule(new Jackson2HalModule());

        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
        messageConverter.setSupportedMediaTypes(MediaType.parseMediaTypes("application/hal+json"));
        messageConverter.setObjectMapper(mapper);

        ParameterizedTypeReference<PagedResources<Person>> resource = new ParameterizedTypeReference<PagedResources<Person>>() {};

        RestTemplate restTemplate = new RestTemplate(Collections.<HttpMessageConverter<?>> singletonList(messageConverter));

        ResponseEntity<PagedResources<Person>> responseEntity = restTemplate.exchange("http://localhost:8070/people",
                HttpMethod.GET, new HttpEntity<Void>(new HttpHeaders()), resource);

        PagedResources<Person> resources = responseEntity.getBody();
        return new ArrayList<>(resources.getContent());
    }
}
