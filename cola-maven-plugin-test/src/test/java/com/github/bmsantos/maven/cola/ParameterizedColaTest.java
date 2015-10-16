package com.github.bmsantos.maven.cola;

import com.github.bmsantos.core.cola.story.annotations.Feature;
import com.github.bmsantos.core.cola.story.annotations.Given;
import com.github.bmsantos.core.cola.story.annotations.Then;
import com.github.bmsantos.core.cola.story.annotations.When;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Collection;

import static java.util.Arrays.asList;
import static java.util.Objects.requireNonNull;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(Parameterized.class)
public class ParameterizedColaTest extends BaseColaTest {

    private final Integer inputNumber;
    private final Boolean expectedResult;
    private boolean actual;

    public ParameterizedColaTest(final Integer inputNumber, final Boolean expectedResult) {
        this.inputNumber = inputNumber;
        this.expectedResult = expectedResult;
    }

    @Parameterized.Parameters
    public static Collection<?> primeNumbers() {
        return asList(new Object[][]{
          {2, true},
          {6, false},
          {19, true},
          {22, false},
          {23, true}
        });
    }

    @Feature
    private final String feature =
      "@group1\n"
        + "Feature: Detect prime numbers\n"
        + "Scenario: Should detect prime numbers\n"
        + "Given a number\n"
        + "And a prime number guess\n"
        + "When validated for prime\n"
        + "Then guess should match validation result";

    @Given("a number")
    public void givenANumber() {
        requireNonNull(inputNumber);
    }

    @Given("a prime number guess")
    public void givenAGuess() {
        requireNonNull(expectedResult);
    }

    @When("validated for prime")
    public void whenValidated() {
        actual = validate(inputNumber);
    }

    @Then("guess should match validation result")
    public void verifyGuess() {
        assertThat(actual, is(expectedResult));
    }

    public Boolean validate(final Integer primeNumber) {
        for (int i = 2; i < primeNumber / 2; i++) {
            if (primeNumber % i == 0) {
                return false;
            }
        }
        return true;
    }
}
