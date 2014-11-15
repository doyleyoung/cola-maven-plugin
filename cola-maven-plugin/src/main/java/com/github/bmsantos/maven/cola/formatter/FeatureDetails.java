package com.github.bmsantos.maven.cola.formatter;

import gherkin.formatter.model.Feature;
import gherkin.formatter.model.Scenario;
import gherkin.formatter.model.Step;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FeatureDetails {

    private final String uri;
    private Feature feature;
    private Map<Scenario, List<Step>> scenarios = new HashMap<>();

    public FeatureDetails(final String uri) {
        this.uri = uri;
    }

    public String getUri() {
        return uri;
    }

    public Feature getFeature() {
        return feature;
    }

    public void setFeature(final Feature feature) {
        this.feature = feature;
    }

    public Map<Scenario, List<Step>> getScenarios() {
        return scenarios;
    }

    public void setScenarios(final Map<Scenario, List<Step>> scenarios) {
        this.scenarios = scenarios;
    }
}
