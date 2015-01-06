package com.github.bmsantos.maven.cola;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import com.github.bmsantos.core.cola.story.annotations.Features;
import com.github.bmsantos.core.cola.story.annotations.Given;
import com.github.bmsantos.core.cola.story.annotations.Then;
import com.github.bmsantos.core.cola.story.annotations.When;

@Features({ "introduce_addition", "introduce_addition2" })
public class ClassColaTest extends BaseColaTest {

    public List<String> executionOrder = new ArrayList<>();

    @Given("A")
    public void givenA() {
        assertThat(executionOrder.isEmpty(), is(true));

        executionOrder.add(Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Given("B")
    public void givenB() {
        assertThat(executionOrder.get(executionOrder.size() - 1), is("givenA"));

        executionOrder.add(Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Given("C")
    public void givenC() {
        assertThat(executionOrder.get(executionOrder.size() - 1), is("givenA"));

        executionOrder.add(Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @When("added together")
    public void whenA() {
        executionOrder.add(Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @When("subtracted by C")
    public void whenB() {
        assertThat(executionOrder.get(executionOrder.size() - 1), is("whenA"));

        executionOrder.add(Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Then("the result will be addition of both numbers")
    public void thenA() {
        assertThat(executionOrder.get(executionOrder.size() - 1), is("whenA"));

        executionOrder.add(Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Then("the result will be A")
    public void thenB() {
        assertThat(executionOrder.get(executionOrder.size() - 1), is("whenB"));

        executionOrder.add(Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Then("C will be C")
    public void thenC() {
        assertThat(executionOrder.get(executionOrder.size() - 1), is("thenB"));

        executionOrder.add(Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Then("A will be A")
    public void thenD() {
        assertThat(executionOrder.get(executionOrder.size() - 1), is("thenC"));

        executionOrder.add(Thread.currentThread().getStackTrace()[1].getMethodName());
    }
}
