package com.github.bmsantos.maven.cola;

import java.util.HashMap;
import java.util.Map;

import com.github.bmsantos.core.cola.story.annotations.Assigned;
import com.github.bmsantos.core.cola.story.annotations.Given;
import com.github.bmsantos.core.cola.story.annotations.Then;
import com.github.bmsantos.core.cola.story.annotations.When;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class AliasTest extends BaseColaTest {

    private final String stories =
        "Feature: Introduce greeting\n"
            + "Scenario: Should say hello in English\n"
            + "Given a greeter\n"
            + "When the welcoming is announced\n"
            + "Then the greeting will be Hello!\n"
            + "\n"
            + "Scenario: Deveria de dizer ola em Portugues\n"
            + "Given um saudador\n"
            + "When anuncia as boas vindas\n"
            + "Then a saudacao sera Ola!";

    private Map<String,String> greetings = new HashMap<String,String>() {{
        put("greeter", "Hello!");
        put("saudador", "Ola!");
    }};

    private String greeter;
    private String greeting;

    @Given({"a <greeter>", "um <greeter>"})
    public void givenAGreeter(@Assigned("greeter") final String greeter) {
        this.greeter = greeter;
    }

    @When({"the welcoming is announced","anuncia as boas vindas"})
    public void whenAnnounced() {
        this.greeting = greetings.get(greeter);
    }

    @Then({"the greeting will be <greeting>", "a saudacao sera <greeting>"})
    public void thenVerifyGreeting(@Assigned("greeting") final String greeting) {
        assertThat(this.greeting, is(greeting));
    }
}
