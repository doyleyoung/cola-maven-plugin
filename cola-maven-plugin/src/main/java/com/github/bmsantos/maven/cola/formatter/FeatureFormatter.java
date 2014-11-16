package com.github.bmsantos.maven.cola.formatter;

import static java.lang.System.err;
import gherkin.formatter.Formatter;
import gherkin.formatter.model.Background;
import gherkin.formatter.model.Examples;
import gherkin.formatter.model.Feature;
import gherkin.formatter.model.Scenario;
import gherkin.formatter.model.ScenarioOutline;
import gherkin.formatter.model.Step;
import gherkin.parser.Parser;

import java.util.ArrayList;
import java.util.List;

import com.github.bmsantos.maven.cola.exceptions.InvalidFeature;
import com.github.bmsantos.maven.cola.exceptions.InvalidFeatureUri;

public class FeatureFormatter implements Formatter {

    private FeatureDetails currentFeature;
    private Scenario currentScenario;
    private Background currentBackground;

    public static FeatureDetails parse(final String feature, final String fromUri) {
        if (feature == null || feature.isEmpty()) {
            throw new InvalidFeature("Feature is null or empty.");
        }

        if (fromUri == null || fromUri.isEmpty()) {
            throw new InvalidFeatureUri("Feature URI is null or empty.");
        }

        final FeatureFormatter formatter = new FeatureFormatter();
        final Parser parser = new Parser(formatter, false);

        parser.parse(feature, fromUri, 0);

        return formatter.getFeature();
    }

    public FeatureDetails getFeature() {
        return currentFeature;
    }

    @Override
    public void syntaxError(final String state, final String event,
        final List<String> legalEvents, final String uri, final Integer line) {
        out(state, event, legalEvents, uri, line);
        err.println("Story syntax error in line " + line);
    }

    @Override
    public void uri(final String uri) {
        currentFeature = new FeatureDetails(uri);
    }

    @Override
    public void feature(final Feature feature) {
        currentFeature.setFeature(feature);
    }

    @Override
    public void scenarioOutline(final ScenarioOutline scenarioOutline) {
        // empty
    }

    @Override
    public void examples(final Examples examples) {
        // empty
    }

    @Override
    public void startOfScenarioLifeCycle(final Scenario scenario) {
        // empty
    }

    @Override
    public void background(final Background background) {
        currentFeature.setBackground(background);
        currentBackground = background;
        currentScenario = null;
    }

    @Override
    public void scenario(final Scenario scenario) {
        currentFeature.getScenarios().put(scenario, new ArrayList<Step>());
        currentScenario = scenario;
        currentBackground = null;
    }

    @Override
    public void step(final Step step) {
        if (currentScenario != null) {
            currentFeature.getScenarios().get(currentScenario).add(step);
        } else if (currentBackground != null) {
            currentFeature.getBackgroundSteps().add(step);
        }
    }

    @Override
    public void endOfScenarioLifeCycle(final Scenario scenario) {
        // empty
    }

    @Override
    public void done() {
        // empty
    }

    @Override
    public void close() {
        // empty
    }

    @Override
    public void eof() {
        // empty
    }

    private void out(final Object... values) {
        final StringBuffer result = new StringBuffer();
        for (final Object value : values) {
            result.append(value + " - ");
        }
        System.out.println(result.toString());
    }
}
