package com.example.appclientservice;

import static org.springframework.http.HttpMethod.GET;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class PersonService {

  private final WebClient personWebClient;

  public Mono<Person> findById(Long id) {
    return personWebClient.method(GET)
      .uri("person/{id}", id)
      .retrieve()
      .bodyToMono(Person.class);
  }

  public Flux<Person> findAll() {
    return personWebClient.method(GET)
      .uri("person")
      .retrieve()
      .bodyToFlux(Person.class);
  }

}
