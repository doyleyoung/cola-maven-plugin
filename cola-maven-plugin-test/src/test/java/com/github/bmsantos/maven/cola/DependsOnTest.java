package com.github.bmsantos.maven.cola;

import com.github.bmsantos.core.cola.story.annotations.DependsOn;
import com.github.bmsantos.core.cola.story.annotations.Then;
import com.github.bmsantos.core.cola.story.annotations.When;

@DependsOn(ColaTest.class)
public class DependsOnTest extends BaseColaTest {

    private final String stories =
        "Feature: Depend on other junit\n"
            + "Scenario: Should add two numbers\n"
            + "When state is checked\n"
            + "Then the result will verify";

    @When("state is checked")
    @DependsOn({ExamplesColaTest.class, RegexColaTest.class})
    public void when() {
    }

    @Then("the result will verify")
    public void then() {
    }
}
