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
    }

    @Test
    public void pathToFeatureFile() throws Exception {
        feature.Feature("first line of feature description\nextended description");

        assertThat(featureDescriptor().getUniqueId(), endsWith("feature", "first-line-of-feature-description"));
    }

    @Test
    public void scenarioId() throws Exception {
        feature.Scenario("first line of the scenario\nextended description");

        assertThat(scenarioDescriptor().getUniqueId(), endsWith("scenario", "feature-name;first-line-of-the-scenario"));
    }

    @Test
    public void scenarioStepId() throws Exception {
        feature.Scenario("first line of the scenario\nextended description").AStep("step text");

        assertThat(scenarioDescriptor().getChildren().iterator().next().getUniqueId(), endsWith("step", "step text"));
    }

    @Test
    public void scenarioOutlineId() throws Exception {
        feature.ScenarioOutline("first line of the scenario outline\nextended description");

        assertThat(scenarioOutlineDescriptor().getUniqueId(), endsWith("scenario-outline", "feature-name;first-line-of-the-scenario-outline"));
    }

    @Test
    public void scenarioInScenarioOutlineId() throws Exception {
        feature.ScenarioOutline("first-line")
                .AStep("<arg1> <arg2>")
                .Example("one", "two");

        assertThat(scenarioInScenarioOutline().getUniqueId(), endsWith("scenario", "feature-name;first-line;;2"));
    }

    @Test
    public void stepInScenarioOutlineId() throws Exception {
        feature.ScenarioOutline("first-line")
                .AStep("<arg1> <arg2>")
                .Example("one", "two");

        assertThat(stepInScenarioOutline().getUniqueId(), endsWith("step", "one two"));
    }

    private TestDescriptor stepInScenarioOutline() {
        return scenarioInScenarioOutline().getChildren().iterator().next();
    }

    private TestDescriptor scenarioInScenarioOutline() {
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