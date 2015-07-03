package com.github.bmsantos.maven.cola;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import com.github.bmsantos.core.cola.story.annotations.Assigned;
import com.github.bmsantos.core.cola.story.annotations.Feature;
import com.github.bmsantos.core.cola.story.annotations.Given;
import com.github.bmsantos.core.cola.story.annotations.Then;
import com.github.bmsantos.core.cola.story.annotations.When;

public class AssignedColaTest extends BaseColaTest {

    @Feature
    private final String feature =
    "Feature: An example feature\n"
        + "Scenario: Should parse examples\n"
        + "Given there are 50 cucumbers\n"
        + "When I eat 20 cucumbers\n"
        + "Then I should have 30 cucumbers\n";

    private Integer start, end;

    @Given("there are <start> cucumbers")
    public void given(@Assigned("start") final String start) {
        this.start = Integer.valueOf(start);
    }

    @When("I eat <eaten> cucumbers")
    public void when(@Assigned("eaten") final Integer eaten) {
        end = start - eaten;
    }

    @Then("I should have <left> cucumbers")
    public void then(@Assigned("left") final Long left) {
        assertThat(end, equalTo(left.intValue()));
    }
}