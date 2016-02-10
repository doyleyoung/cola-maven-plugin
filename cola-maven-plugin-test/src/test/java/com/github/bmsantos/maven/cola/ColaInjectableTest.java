package com.github.bmsantos.maven.cola;

import com.github.bmsantos.core.cola.story.annotations.ColaInjectable;
import com.github.bmsantos.core.cola.story.annotations.DependsOn;
import com.github.bmsantos.core.cola.story.annotations.Then;
import com.github.bmsantos.core.cola.story.annotations.When;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@DependsOn(ColaInjectedTest.class)
public class ColaInjectableTest extends BaseColaTest {

    private final String stories =
        "Feature: Inject state in depending test\n"
            + "Scenario: Should inject state\n"
            + "When state is increased\n"
            + "Then the result will verify";

    @ColaInjectable
    private Integer injectableNumber;

    @ColaInjectable("injected_string")
    private String injectableString = "Hello COLA Test Dependency Injection!";

    @Before
    public void setUp() {
        injectableNumber = new Integer(101);
    }

    @When("state is increased")
    public void when() {
        injectableNumber++;
    }

    @Then("the result will verify")
    public void then() {
        assertThat(injectableNumber, is(102));
    }
}
