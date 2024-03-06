# micronaut-tracing

This project is being used as a demo of the micronaut tracing capabilities, in particular trying to identify why reactive methods annotated with NewSpan are not having their contexts propagated.

It is important to note, that this is massively cut down implementation of a GCP deployed application that we're attempting to use the NewSpan (or WithSpan) annotations to add additional levels to some traces, but also to instrument pubsub listeners that are using the reactive model.

It is also important to note that when using the following, the overall trace is correctly propagated (but any intermediate NewSpan's are not) - this demo project only exposes micronaut-tracing-opentelemetry to replicate the issue.
```
    <dependency>
      <groupId>io.micronaut.tracing</groupId>
      <artifactId>micronaut-tracing-opentelemetry-http</artifactId>
   </dependency>
```


## Local results
<http://localhost:8080/hello> - to hit the non-reactive endpoint, the current span logged is valid
```
    ... hello -> ImmutableSpanContext{traceId=5444699c6791c96606dd3526a08408f, spanId=2457aa8ded06d6b, tracingFlags=01, traceState=ArrayBasedTraceState{entries=[]}, remote=false, valid=true}
    ... getMessage -> ImmutableSpanContext{traceId=5444699c6791c96606dd3526a08408f, spanId=2457aa8ded06d6b, tracingFlags=01, traceState=ArrayBasedTraceState{entries=[]}, remote=false, valid=true}
```

<http://localhost:8080/helloReactive> - to hit the reactive endpoint, the current span logged is invalid
```
    ... helloReactive -> ImmutableSpanContext{traceId=278bb519b2b0e4e04a05b074739a6327, spanId=ee6bfdfda3ecccae, tracingFlags=01, traceState=ArrayBasedTraceState{entries=[]}, remote=false, valid=true}
    ... getMessage -> ImmutableSpanContext{traceId=0000000000000000000000000000000, spanId=000000000000000, tracingFlags=00, traceState=ArrayBasedTraceState{entries=[]}, remote=false, valid=false}
    ... doOnNext -> ImmutableSpanContext{traceId=0000000000000000000000000000000, spanId=000000000000000, tracingFlags=00, traceState=ArrayBasedTraceState{entries=[]}, remote=false, valid=false}

```
