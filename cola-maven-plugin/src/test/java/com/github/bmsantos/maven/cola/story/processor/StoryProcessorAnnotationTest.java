package com.github.bmsantos.maven.cola.story.processor;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import com.github.bmsantos.maven.cola.story.annotations.Given;
import com.github.bmsantos.maven.cola.story.annotations.Then;
import com.github.bmsantos.maven.cola.story.annotations.When;

public class StoryProcessorAnnotationTest {

    private final String story = "Given a first method\n"
        + "And a second method\n"
        + "When the first method is called\n"
        + "And the second method is called\n"
        + "Then the first method will execute\n"
        + "But the second method will execute";

    private TestClass instance;

    @Before
    public void setUp() {
        instance  = new TestClass();
    }

    @Test
    public void shouldProcessGiven() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {

        // When
        StoryProcessor.process("Scenario: Should Process Story", story, instance);

        // Then
        assertThat(instance.wasGivenCalled, is(true));
    }

    @Test
    public void shouldProcessGivenAnd() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {

        // When
        StoryProcessor.process("Scenario: Should Process Story", story, instance);

        // Then
        assertThat(instance.wasGivenAndCalled, is(true));
    }

    @Test
    public void shouldProcessWhen() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {

        // When
        StoryProcessor.process("Scenario: Should Process Story", story, instance);

        // Then
        assertThat(instance.wasWhenCalled, is(true));
    }

    @Test
    public void shouldProcessWhenAnd() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {

        // When
        StoryProcessor.process("Scenario: Should Process Story", story, instance);

        // Then
        assertThat(instance.wasWhenAndCalled, is(true));
    }

    @Test
    public void shouldProcessThen() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {

        // When
        StoryProcessor.process("Scenario: Should Process Story", story, instance);

        // Then
        assertThat(instance.wasThenCalled, is(true));
    }

    @Test
    public void shouldProcessThenAnd() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {

        // When
        StoryProcessor.process("Scenario: Should Process Story", story, instance);

        // Then
        assertThat(instance.wasThenAndCalled, is(true));
    }

    @Test
    public void shouldProcessInCorrectOrder() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {

        // When
        StoryProcessor.process("Scenario: Should Process Story", story, instance);

        // Then
        assertThat(instance.executionOrder,
            Matchers.contains("givenFirst", "givenSecond", "whenFirst", "whenSecond", "thenFirst", "thenSecond"));
    }

    private class TestClass {

        public boolean wasGivenCalled = false;
        public boolean wasGivenAndCalled = false;
        public boolean wasWhenCalled = false;
        public boolean wasWhenAndCalled = false;
        public boolean wasThenCalled = false;
        public boolean wasThenAndCalled = false;
        public List<String> executionOrder = new ArrayList<>();

        @Given("a first method")
        public void givenFirst() {
            wasGivenCalled = true;
            executionOrder.add(Thread.currentThread().getStackTrace()[1].getMethodName());
        }

        @Given("a second method")
        public void givenSecond() {
            wasGivenAndCalled = true;
            executionOrder.add(Thread.currentThread().getStackTrace()[1].getMethodName());
        }

        @When("the first method is called")
        public void whenFirst() {
            wasWhenCalled = true;
            executionOrder.add(Thread.currentThread().getStackTrace()[1].getMethodName());
        }

        @When("the second method is called")
        public void whenSecond() {
            wasWhenAndCalled = true;
            executionOrder.add(Thread.currentThread().getStackTrace()[1].getMethodName());
        }

        @Then("the first method will execute")
        public void thenFirst() {
            wasThenCalled = true;
            executionOrder.add(Thread.currentThread().getStackTrace()[1].getMethodName());
        }

        @Then("the second method will execute")
        public void thenSecond() {
            wasThenAndCalled = true;
            executionOrder.add(Thread.currentThread().getStackTrace()[1].getMethodName());
        }
    }

}
