package com.example.appclientservice;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AppController {

  private final PersonService personService;

  @GetMapping("")
  public Publisher<?> getData() {
    Mono<Person> getPepe = personService.findById(1L).doOnSubscribe(s -> log.info("Trayendo a Pepito"));
    Mono<Person> getJuan = personService.findById(2L).doOnSubscribe(s -> log.info("Trayendo a Juan"));
    return Mono
      .zip(getPepe, getJuan, (pepe, juan) -> {
        log.info("zip pepe + juan");
        return List.of(pepe, juan);
      }) // 3 segundos
      .flatMapMany(Flux::fromIterable)
      // .flatMap(p -> simulateTime(p)); // 3 segundos * 2
      .flatMap(p -> simulateTime(p).subscribeOn(Schedulers.boundedElastic())); // 3 segundos
  }

  private Mono<Person> simulateTime(Person p) {
    return Mono.fromCallable(() -> {
      log.info("Ejecutando para: {}", p);
      Thread.sleep(3_000);
      return p;
    });
  }

}
