package com.github.bmsantos.maven.cola;

import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import com.github.bmsantos.core.cola.story.annotations.Given;
import com.github.bmsantos.core.cola.story.annotations.Then;
import com.github.bmsantos.core.cola.story.annotations.When;

public class RegexColaTest extends BaseColaTest {

    private final String stories =
        "Feature: Introduce drinking\n"
            + "Scenario: Should get happy\n"
            + "Given a beer to enjoy\n"
            + "When mixed with 13 other alcoholic drinks\n"
            + "Then one will be drunk!";

    public List<String> executionOrder = new ArrayList<>();

    @Given("a (juice|beer) to enjoy")
    public void given() {
        executionOrder.add(Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @When("mixed with \\d+ other (redbull|alcoholic drinks)")
    public void when() {
        executionOrder.add(Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Then("one will be (drunk|really energetic)!")
    public void then() {
        executionOrder.add(Thread.currentThread().getStackTrace()[1].getMethodName());

        assertThat(executionOrder, contains("given", "when", "then"));
    }
}
