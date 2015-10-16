package com.github.bmsantos.maven.cola;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.net.URISyntaxException;

import org.cortx.maven.client.CortxFactory;
import org.cortx.maven.client.dsl.Cortx;
import org.junit.After;
import org.junit.Before;

import com.github.bmsantos.core.cola.story.annotations.Feature;
import com.github.bmsantos.core.cola.story.annotations.Given;
import com.github.bmsantos.core.cola.story.annotations.Then;
import com.github.bmsantos.core.cola.story.annotations.When;

public class ReportColaTest extends BaseColaTest {

    private static final String CONFIG_PATH_QUERY = "/rest/to/${type}";
    private static final String APPLICATION_JSON = "application/json";
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String CONFIG_BODY = "{ \"type\":\"${type}\", \"state\":\"${state}\", \"error\":\"${error}\" }";

    @Feature
    private final String ignoreFeature =
      "#report> type:feature\n"
      + "@group1\n"
      + "Feature: Ignore Feature\n"
        + "\n"
        + "#report> type:scenario\n"
        + "Scenario: Should ignore feature\n"
        + "Given A\n"
        + "When B\n"
        + "Then not executed";

    private Cortx cortx;

    @Before
    public void setUp() throws URISyntaxException {
        cortx = CortxFactory.getCortx("localhost");
        cortx.reset();
    }

    @After
    public void checkResults() {
        // Given
        final String featurePathAndQuery = buildUri(CONFIG_PATH_QUERY, "feature");
        final String featureBody = buildBody(CONFIG_BODY, "feature");

        final String scenarioPathAndQuery = buildUri(CONFIG_PATH_QUERY, "scenario");
        final String scenarioBody = buildBody(CONFIG_BODY, "scenario");

        // Then
        assertThat(cortx.verify().post(featurePathAndQuery).withHeader(CONTENT_TYPE, APPLICATION_JSON).withBody(featureBody)
            .wasCalled(), is(true));

        assertThat(cortx.verify().post(scenarioPathAndQuery).withHeader(CONTENT_TYPE, APPLICATION_JSON).withBody(scenarioBody)
            .wasCalled(), is(true));
    }

    @Given("A")
    public void givenA() {
    }

    @When("B")
    public void whenB() {
    }

    @Then("not executed")
    public void thenNotExecuted() {
    }

    private String buildUri(final String template, final String type) {
        return template.replace("${type}", type);
    }

    private String buildBody(final String template, final String type) {
        return template.replace("${type}", type).replace("${state}", "Pass").replace("${error}", "none");
    }
}
