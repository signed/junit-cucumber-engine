package cucumber.runtime.junit;

import org.junit.After;
import org.junit.Test;
import org.junit.platform.engine.TestDescriptor;

import static cucumber.runtime.junit.CucumberFeatureMother.anyFeatureFile;
import static cucumber.runtime.junit.UniqueIdMatcher.endsWith;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.platform.engine.UniqueId.forEngine;

public class CucumberEngine_IdAssignmentTest implements CucumberEngineTestSugar {

    private final CucumberEngineFixture fixture = new CucumberEngineFixture();
    private final CucumberFeatureBuilder feature = anyFeatureFile();

    @After
    public void allTestDescriptorsAreInAProperState() throws Exception {
        fixture.engineExecutionListener.ensureAllInProperEndState();
    }

    @Test
    public void topLevelCucumberEngine() throws Exception {
        assertThat(engineDescriptor().getUniqueId(), equalTo(forEngine("cucumber-jvm")));
        assertThat(engineDescriptor().getDisplayName(), equalTo("cucumber"));
    }

    @Test
    public void feature() throws Exception {
        feature.Feature("first line of feature description\nextended description");

        assertThat(featureDescriptor().getUniqueId(), endsWith("feature", "first-line-of-feature-description"));
        assertThat(featureDescriptor().getDisplayName(), equalTo("first line of feature description"));
    }

    @Test
    public void scenario() throws Exception {
        feature.Scenario("first line of the scenario\nextended description");

        assertThat(scenarioDescriptor().getUniqueId(), endsWith("scenario", "feature-name;first-line-of-the-scenario"));
        assertThat(scenarioDescriptor().getDisplayName(), equalTo("Scenario: first line of the scenario"));
    }

    @Test
    public void scenarioStep() throws Exception {
        feature.Scenario("first line of the scenario\nextended description").AStep("step text");

        assertThat(stepInScenarioDescriptor().getUniqueId(), endsWith("step", "step text"));
        assertThat(stepInScenarioDescriptor().getDisplayName(), equalTo("step text"));
    }

    @Test
    public void scenarioOutline() throws Exception {
        feature.ScenarioOutline("first line of the scenario outline\nextended description");

        assertThat(scenarioOutlineDescriptor().getUniqueId(), endsWith("scenario-outline", "feature-name;first-line-of-the-scenario-outline"));
        assertThat(scenarioOutlineDescriptor().getDisplayName(), equalTo("Scenario Outline: first line of the scenario outline"));
    }

    @Test
    public void scenarioInScenarioOutline() throws Exception {
        feature.ScenarioOutline("first-line")
                .AStep("<arg1> <arg2>")
                .Example("one", "two");

        assertThat(scenarioInScenarioOutlineDescriptor().getUniqueId(), endsWith("scenario", "feature-name;first-line;;2"));
        assertThat(scenarioInScenarioOutlineDescriptor().getDisplayName(), equalTo("| one | two |"));
    }

    @Test
    public void stepInScenarioOutline() throws Exception {
        feature.ScenarioOutline("first-line")
                .AStep("<arg1> <arg2>")
                .Example("one", "two");

        assertThat(stepInScenarioOutlineDescriptor().getUniqueId(), endsWith("step", "one two"));
        assertThat(stepInScenarioOutlineDescriptor().getDisplayName(), equalTo("one two"));
    }

    private TestDescriptor stepInScenarioDescriptor() {
        return scenarioDescriptor().getChildren().iterator().next();
    }

    private TestDescriptor stepInScenarioOutlineDescriptor() {
        return scenarioInScenarioOutlineDescriptor().getChildren().iterator().next();
    }

    private TestDescriptor scenarioInScenarioOutlineDescriptor() {
        return scenarioOutlineDescriptor().getChildren().iterator().next();
    }

    private TestDescriptor scenarioOutlineDescriptor() {
        return featureDescriptor().getChildren().iterator().next();
    }

    private TestDescriptor scenarioDescriptor() {
        return featureDescriptor().getChildren().iterator().next();
    }

    private TestDescriptor featureDescriptor() {
        return engineDescriptor().getChildren().iterator().next();
    }

    private CucumberEngineDescriptor engineDescriptor() {
        return discoveredDescriptorsFor(feature);
    }

    @Override
    public CucumberEngineFixture fixture() {
        return fixture;
    }
}