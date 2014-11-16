package com.github.bmsantos.maven.cola;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import com.github.bmsantos.maven.cola.story.annotations.Given;
import com.github.bmsantos.maven.cola.story.annotations.Then;
import com.github.bmsantos.maven.cola.story.annotations.When;

public class BackgroundColaTest extends BaseColaTest {

    private final String stories =
        "Feature: Introduce addition\n"
            + "Background: should clean boards\n"
            + "Given a clean blackboard\n"
            + "And a clean whiteboard\n"
            + "\n"
            + "Scenario: Should add two numbers again\n"
            + "Given A\n"
            + "And B\n"
            + "When added together\n"
            + "Then the result will be A + B";

    public List<String> executionOrder = new ArrayList<>();

    @Given("a clean blackboard")
    public void givenACleanBlackboard() {
        assertThat(executionOrder.isEmpty(), is(true));
        
        executionOrder.add(Thread.currentThread().getStackTrace()[1].getMethodName());
    }
    
    @Given("a clean whiteboard")
    public void givenACleanWhiteboard() {
        assertThat(executionOrder.get(executionOrder.size() - 1), is("givenACleanBlackboard"));
        
        executionOrder.add(Thread.currentThread().getStackTrace()[1].getMethodName());
    }
    
    @Given("A")
    public void givenA() {
        assertThat(executionOrder.get(executionOrder.size() - 1), is("givenACleanWhiteboard"));

        executionOrder.add(Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Given("B")
    public void givenB() {
        assertThat(executionOrder.get(executionOrder.size() - 1), is("givenA"));

        executionOrder.add(Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @When("added together")
    public void whenA() {
        assertThat(executionOrder.get(executionOrder.size() - 1), is("givenB"));
        
        executionOrder.add(Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Then("the result will be A + B")
    public void thenAB() {
        assertThat(executionOrder.get(executionOrder.size() - 1), is("whenA"));
    }
}
