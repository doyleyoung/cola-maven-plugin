package com.github.bmsantos.maven.cola;

import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import com.github.bmsantos.core.cola.story.annotations.Feature;
import com.github.bmsantos.core.cola.story.annotations.Given;
import com.github.bmsantos.core.cola.story.annotations.Then;
import com.github.bmsantos.core.cola.story.annotations.When;

public class IgnoreColaTest extends BaseColaTest {

    private static final String ERROR = "Should never execute";

    @Feature
    private final String ignoreFeature =
    "@ignore\n"
        + "Feature: Ignore Feature\n"
        + "Scenario: Should ignore feature\n"
        + "Given A\n"
        + "When B\n"
        + "Then not executed";

    @Feature
    private final String ignoreScenarios =
    "Feature: Ignore annotated scenarios\n"
        + "@ignore\n"
        + "Scenario: Should ignore scenario\n"
        + "Given A\n"
        + "When B\n"
        + "Then not executed\n"
        + "\n"
        + "Scenario: Should execute scenario\n"
        + "Given C\n"
        + "When D\n"
        + "Then it will execute";

    private final List<String> executed = new ArrayList<>();

    @Given("A")
    public void givenA() {
        fail(ERROR);
    }

    @When("B")
    public void whenB() {
        fail(ERROR);
    }

    @Then("not executed")
    public void thenNotExecuted() {
        fail(ERROR);
    }

    @Given("C")
    public void givenC() {
        executed.add("givenC");
    }

    @When("D")
    public void whenD() {
        executed.add("whenD");
    }

    @Then("it will execute")
    public void thenExecute() {
        assertThat(executed, contains("givenC", "whenD"));
    }
}
