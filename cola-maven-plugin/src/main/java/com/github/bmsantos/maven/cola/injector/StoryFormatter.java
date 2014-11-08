package com.github.bmsantos.maven.cola.injector;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StoryFormatter implements Formatter {

    private Scenario currentScenario;
    private final Map<Scenario, List<Step>> scenarios = new HashMap<Scenario, List<Step>>();

    public static Map<Scenario, List<Step>> parse(final String feature) {
        final StoryFormatter formatter = new StoryFormatter();
        final Parser parser = new Parser(formatter, false);

        parser.parse(feature, "/Foo/Test/URI", 0);

        return formatter.getScenarios();
    }

    public Map<Scenario, List<Step>> getScenarios() {
        return scenarios;
    }

    public void syntaxError(final String state, final String event,
            final List<String> legalEvents, final String uri, final Integer line) {
        out(state, event, legalEvents, uri, line);
        err.println("Story syntax error in line " + line);
    }

    public void uri(final String uri) {
    }

    public void feature(final Feature feature) {
    }

    public void scenarioOutline(final ScenarioOutline scenarioOutline) {
    }

    public void examples(final Examples examples) {
    }

    public void startOfScenarioLifeCycle(final Scenario scenario) {
    }

    public void background(final Background background) {
    }

    public void scenario(final Scenario scenario) {
        scenarios.put(scenario, new ArrayList<Step>());
        currentScenario = scenario;
    }

    public void step(final Step step) {
        scenarios.get(currentScenario).add(step);
    }

    public void endOfScenarioLifeCycle(final Scenario scenario) {
    }

    public void done() {
    }

    public void close() {
    }

    public void eof() {
    }

    private void out(final Object... values) {
        final StringBuffer result = new StringBuffer();
        for (final Object value : values) {
            result.append(value + " - ");
        }
        System.out.println(result.toString());
    }
}
