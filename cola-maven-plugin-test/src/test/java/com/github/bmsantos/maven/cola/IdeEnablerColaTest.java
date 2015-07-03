package com.github.bmsantos.maven.cola;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.github.bmsantos.core.cola.story.annotations.Feature;
import com.github.bmsantos.core.cola.story.annotations.IdeEnabler;
import com.github.bmsantos.core.cola.story.annotations.Then;
import com.github.bmsantos.core.cola.story.annotations.When;

public class IdeEnablerColaTest {

    @Feature
    private final String feature = 
    "Feature: @IdeEnabler test methods will be removed\n"
        + "Scenario: Should remove annotated test\n"
        + "When the JUnit test is executed\n"
        + "Then the POJO will not include the annotated test method\n";

    private boolean exists = true;

    @IdeEnabler
    @Test
    public void willNotExecute() {
        fail("Should not have executed...");
    }

    @When("the JUnit test is executed")
    public void when() throws Exception {
        try {
            this.getClass().getMethod("willNotExecute", (Class<?>) null);
        } catch (final NoSuchMethodException e) {
            exists = false;
        }
    }

    @Then("the POJO will not include the annotated test method")
    public void then() {
        assertThat(exists, is(false));
    }
}