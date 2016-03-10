package com.github.bmsantos.maven.cola;

import java.util.ArrayList;
import java.util.List;

import com.github.bmsantos.core.cola.story.annotations.Assigned;
import com.github.bmsantos.core.cola.story.annotations.Given;
import com.github.bmsantos.core.cola.story.annotations.PostSteps;
import com.github.bmsantos.core.cola.story.annotations.PreSteps;
import com.github.bmsantos.core.cola.story.annotations.Then;
import com.github.bmsantos.core.cola.story.annotations.When;

import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;

public class PrePostStepTest extends BaseColaTest {

    private final String stories =
      "Feature: Call pre and post steps\n"
        + "Scenario: Should call pre-steps\n"
        + "When step 3 is called\n"
        + "Then steps order will be 1, 2, 3\n"
        + "\n"
        + "Scenario: Should call post-steps\n"
        + "When step 0 is called\n"
        + "Then steps order will be 0, 1, 2, 3\n";

    private List<Integer> steps = new ArrayList<>();

    @Given("step <number> is called")
    public void stepCalled(@Assigned("number") final Integer number) {
        steps.add(number);
    }

    @PreSteps({ "Given step 1 is called", "Given step 2 is called"})
    @When("step 3 is called")
    public void stepThree() {
        steps.add(3);
    }

    @When("step 0 is called")
    @PostSteps( "When step 3 is called" )
    public void stepZero() {
        steps.add(0);
    }

    @Then("steps order will be 1, 2, 3")
    public void verifyPreSteps() {
        assertThat(steps, contains(1, 2, 3));
    }

    @Then("steps order will be 0, 1, 2, 3")
    public void verifyPostSteps() {
        assertThat(steps, contains(0, 1, 2, 3));
    }
}