package com.github.bmsantos.maven.cola;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;

import com.github.bmsantos.core.cola.story.annotations.ColaInjected;
import com.github.bmsantos.core.cola.story.annotations.Given;
import com.github.bmsantos.core.cola.story.annotations.Then;
import com.github.bmsantos.core.cola.story.annotations.When;
import com.github.bmsantos.core.cola.story.processor.StoryProcessor;
import com.google.inject.Inject;
import org.junit.Before;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

@ColaInjected
public class ColaInjectedTest extends BaseColaTest {

    private final String stories =
        "Feature: Nine multiples rule\n"
            + "Scenario: Should add to nine\n"
            + "Given an injected number\n"
            + "When multiplied by nine\n"
            + "Then the addition of its digits will be nine";

    @Inject
    private Integer injectedNumber;

    @Inject
    @Named("injected_string")
    private String injectedString;

    private int result = 0;

    @Given("an injected number")
    public void givenAnInjectedNumber() {
        System.err.println("Injected number: " + injectedNumber);
        if (injectedNumber == null) {
            injectedNumber = new Integer(1);
        }
        System.err.println("Injected string: " + injectedString);
    }

    @When("multiplied by nine")
    public void whenA() {
        final String s = Integer.toString(injectedNumber * 9);
        for (int i = 0; i < s.length(); i++) {
            result += Integer.valueOf(String.valueOf(s.charAt(i)));
        }
    }

    @Then("the addition of its digits will be nine")
    public void thenNine() {
        assertThat(result, is(9));
    }
}
