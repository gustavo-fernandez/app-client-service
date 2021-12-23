package com.example.appclientservice;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import java.util.concurrent.TimeUnit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

@SpringBootApplication
public class AppClientServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AppClientServiceApplication.class, args);
	}

	@Bean
	WebClient personWebClient() {
		HttpClient httpClient = HttpClient.create()
			.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 1_000)
			.doOnConnected(connection -> connection
				.addHandlerLast(new WriteTimeoutHandler(5, TimeUnit.SECONDS))
				.addHandlerLast(new ReadTimeoutHandler(10, TimeUnit.SECONDS)));

		return WebClient.builder()
			.clientConnector(new ReactorClientHttpConnector(httpClient))
			.baseUrl("http://localhost:8080/api/")
			.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
			.build();
	}

}
