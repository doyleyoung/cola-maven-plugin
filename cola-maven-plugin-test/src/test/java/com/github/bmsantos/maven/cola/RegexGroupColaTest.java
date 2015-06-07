package com.github.bmsantos.maven.cola;

import static java.util.Objects.requireNonNull;
import static org.junit.Assert.assertTrue;

import com.github.bmsantos.core.cola.story.annotations.Given;
import com.github.bmsantos.core.cola.story.annotations.Group;
import com.github.bmsantos.core.cola.story.annotations.Then;
import com.github.bmsantos.core.cola.story.annotations.When;

public class RegexGroupColaTest extends BaseColaTest {

    private final String stories =
        "Feature: Introduce drinking\n"
            + "Scenario: Should get messy\n"
            + "Given 50 beers per 5 developers\n"
            + "When drunk in one go\n"
            + "Then the floor will get pretty messy";

    private Integer beers;
    private Integer developers;
    private boolean isMessy = false;

    @Given("(\\d+) beers per (\\d+) developers")
    public void given(@Group(1) final Integer beers, @Group(2) final Integer developers) {
        this.beers = requireNonNull(beers);
        this.developers = requireNonNull(developers);
    }

    @When("drunk in one go")
    public void when() {
        isMessy = beers / developers > 7;
    }

    @Then("the floor will get pretty messy")
    public void then() {
        assertTrue(isMessy);
    }
}
