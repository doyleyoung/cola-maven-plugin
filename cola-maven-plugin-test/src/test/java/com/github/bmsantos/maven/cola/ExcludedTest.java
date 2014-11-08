package com.github.bmsantos.maven.cola;

import org.junit.Test;

import com.github.bmsantos.maven.cola.annotations.Given;
import com.github.bmsantos.maven.cola.annotations.Then;
import com.github.bmsantos.maven.cola.annotations.When;

public class ExcludedTest {
    
    private final String stories =
            "Feature: Introduce addition\n" +
                    "Scenario: Should add two numbers\n" +
                    "Given A\n" +
                    "When added to B\n" +
                    "Then the result is A + B\n" +
                    "\n" +
                    "Scenario: Should add two numbers again\n" +
                    "Given C as A\n" +
                    "When added to B\n" +
                    "Then the result is A + B";
    
    @Given("A")
    public void given() {
        System.out.println("\tCalled: Given A");
    }
    
    @Given("C as A")
    public void givenCasA() {
        System.out.println("\tCalled: Given C as A");
    }
    
    @When("added to B")
    public void when() {
        System.out.println("\tCalled: When added to B");
    }
    
    @Then("the result is A + B")
    public void then() {
        System.out.println("\tCalled: Then the result is A + B");
    }
    
    @Test
    public void test() {
        System.out.println("This is a simple test!");
    }
    
}
