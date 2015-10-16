package com.github.bmsantos.maven.cola;

import static org.junit.Assert.fail;

import com.github.bmsantos.core.cola.story.annotations.Feature;
import com.github.bmsantos.core.cola.story.annotations.Given;
import com.github.bmsantos.core.cola.story.annotations.Then;
import com.github.bmsantos.core.cola.story.annotations.When;

public class SkipColaTest extends BaseColaTest {

    private static final String ERROR = "Should never execute";

    @Feature
    private final String skipFeature =
    "@skip\n"
        + "Feature: Skip Feature\n"
        + "Scenario: Should skip feature\n"
        + "Given A\n"
        + "When B\n"
        + "Then not executed";

    @Feature
    private final String skipFeatureWithoutScenarios =
    "Feature: Skip Feature without scenarios\n"
        + "@skip\n"
        + "Scenario: Should be skipped\n"
        + "Given A\n"
        + "When B\n"
        + "Then not executed";

    @Given("A")
    public void givenA() {
        fail(ERROR);
    }

    @When("B")
    public void whenA() {
        fail(ERROR);
    }

    @Then("not executed")
    public void thenA() {
        fail(ERROR);
    }
}
