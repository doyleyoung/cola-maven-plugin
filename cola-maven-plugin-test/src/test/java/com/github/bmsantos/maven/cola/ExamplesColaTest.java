package com.github.bmsantos.maven.cola;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import com.github.bmsantos.core.cola.story.annotations.Feature;
import com.github.bmsantos.core.cola.story.annotations.Given;
import com.github.bmsantos.core.cola.story.annotations.Projection;
import com.github.bmsantos.core.cola.story.annotations.Then;
import com.github.bmsantos.core.cola.story.annotations.When;

public class ExamplesColaTest extends BaseColaTest {

    @Feature
    private final String feature =
    "Feature: An example feature\n"
        + "Scenario Outline: Should parse examples\n"
        + "Given there are <start> cucumbers\n"
        + "When I eat <eat> cucumbers\n"
        + "Then I should have <left> cucumbers\n"
        + "\n"
        + "Examples:\n"
        + " | start | eat | left |\n"
        + " | 12    | 5   | 7    |\n"
        + " | 20    | 5   | 15   |";

    private Integer start, end;

    @Given("there are <start> cucumbers")
    public void given(@Projection("start") final String start) {
        this.start = Integer.valueOf(start);
    }

    @When("I eat <eat> cucumbers")
    public void when(@Projection("eat") final Integer eat) {
        end = start - eat;
    }

    @Then("I should have <left> cucumbers")
    public void then(@Projection("left") final Long left) {
        assertThat(end, equalTo(left.intValue()));
    }
}
