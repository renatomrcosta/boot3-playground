# Spring Boot 3 playground

Testing Spring Boot 3 / Spring 6 features


### Running the service

Either use your IDE, or type in:

```shell
$ make run
```

It will start the application on the default 8080 port

### How to reproduce the HttpExchange issue

In the [main application file](src/main/kotlin/com/example/boot3playground/Boot3PlaygroundApplication.kt), we defined two HttpExchange interfaces: one using reactor, one trying to use suspend functions. To call each, use:

This works:
```shell
curl http://localhost:8080/reactor/hello
```

This unfortunately fails:
```shell
curl http://localhost:8080/coroutines/hello
```