package com.github.bmsantos.maven.cola.formatter;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import gherkin.formatter.model.Scenario;
import gherkin.formatter.model.Step;
import gherkin.lexer.LexingError;

import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.github.bmsantos.maven.cola.exceptions.InvalidFeature;
import com.github.bmsantos.maven.cola.exceptions.InvalidFeatureUri;

public class FeatureFormatterTest {

    private static final String FEATURE_NAME = "Introduce addition";
    private static final String SCENARIO_NAME = "Should add two numbers";

    private static final String PATH_TO_FEATURE = "/path/to/feature";

    private final String feature =
        "Feature: " + FEATURE_NAME +"\n"
            + "Scenario: " + SCENARIO_NAME + "\n"
            + "Given A\n"
            + "And B\n"
            + "When added together\n"
            + "Then the result will be addition of both numbers";

    @Test
    public void shoulParseUri() {
        // When
        final FeatureDetails featureDetails = FeatureFormatter.parse(feature, PATH_TO_FEATURE);

        // Then
        assertThat(featureDetails.getUri(), equalTo(PATH_TO_FEATURE));
    }

    @Test
    public void shoulParseFeature() {
        // When
        final FeatureDetails featureDetails = FeatureFormatter.parse(feature, PATH_TO_FEATURE);

        // Then
        assertThat(featureDetails.getFeature().getName(), equalTo(FEATURE_NAME));
    }

    @Test
    public void shoulParseScenario() {
        // When
        final FeatureDetails featureDetails = FeatureFormatter.parse(feature, PATH_TO_FEATURE);

        // Then
        assertThat(featureDetails.getScenarios().size(), equalTo(1));

        final Map<Scenario, List<Step>> scenarios = featureDetails.getScenarios();
        final Scenario scenario = scenarios.keySet().iterator().next();
        assertThat(scenario.getName(), equalTo(SCENARIO_NAME));
    }

    @Test
    public void shoulParseSteps() {
        // When
        final FeatureDetails featureDetails = FeatureFormatter.parse(feature, PATH_TO_FEATURE);

        // Then
        final Map<Scenario, List<Step>> scenarios = featureDetails.getScenarios();
        final Scenario scenario = scenarios.keySet().iterator().next();
        final List<Step> steps = scenarios.get(scenario);
        assertThat(steps.get(0).getKeyword(), equalTo("Given "));
        assertThat(steps.get(0).getName(), equalTo("A"));

        assertThat(steps.get(1).getKeyword(), equalTo("And "));
        assertThat(steps.get(1).getName(), equalTo("B"));

        assertThat(steps.get(2).getKeyword(), equalTo("When "));
        assertThat(steps.get(2).getName(), equalTo("added together"));

        assertThat(steps.get(3).getKeyword(), equalTo("Then "));
        assertThat(steps.get(3).getName(), equalTo("the result will be addition of both numbers"));
    }

    @Test(expected = InvalidFeature.class)
    public void shouldFailOnNullFeature() {
        // When
        FeatureFormatter.parse(null, PATH_TO_FEATURE);
    }

    @Test(expected = InvalidFeature.class)
    public void shouldFailOnEmptyFeature() {
        // When
        FeatureFormatter.parse("", PATH_TO_FEATURE);
    }

    @Test(expected = LexingError.class)
    public void shouldFailOnInvalidFeature() {
        // When
        FeatureFormatter.parse("this is not a bdd feature", PATH_TO_FEATURE);
    }

    @Test(expected = InvalidFeatureUri.class)
    public void shouldFailOnNullUri() {
        // When
        FeatureFormatter.parse(feature, null);
    }

    @Test(expected = InvalidFeatureUri.class)
    public void shouldFailOnEmptyUri() {
        // When
        FeatureFormatter.parse(feature, "");
    }
}
