package com.example;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.tracing.annotation.NewSpan;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.instrumentation.annotations.WithSpan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

@Controller
public class SimpleController {
  private final Logger log = LoggerFactory.getLogger(SimpleController.class);

  @Get
  @NewSpan("new-hello")
  //@WithSpan("with-hello")
  public String hello() {
    log.info("hello -> {}", Span.current().getSpanContext());
	return getMessage();
  }

  @Get
  @NewSpan("new-helloReactive")
  //@WithSpan("with-helloReactive")
  public Mono<String> helloReactive() {
    log.info("helloReactive -> {}", Span.current().getSpanContext());
	return Mono.fromCallable(this::getMessage)
		.doOnNext(x -> log.info("doOnNext -> {}", Span.current().getSpanContext()));
  }

  String getMessage() {
    log.info("getMessage -> {}", Span.current().getSpanContext());
	return "hello";
  }
}