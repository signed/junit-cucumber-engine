package cucumber.runtime.junit;

import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.platform.engine.TestDescriptor;

import static cucumber.runtime.junit.CucumberFeatureMother.anyFeatureFile;
import static cucumber.runtime.junit.UniqueIdMatcher.endsWith;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.platform.engine.UniqueId.forEngine;

public class CucumberEngine_IdAssignmentTest implements CucumberEngineTestSugar {

    private final CucumberEngineFixture fixture = new CucumberEngineFixture();
    private final CucumberFeatureBuilder cucumberFeatureBuilder = anyFeatureFile();

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
        cucumberFeatureBuilder.path("/the/path");

        assertThat(featureDescriptor().getUniqueId(), endsWith("feature", "/the/path"));
    }

    @Test
    public void scenarioId() throws Exception {
        cucumberFeatureBuilder.Scenario("first line of the scenario\nextended description");

        assertThat(scenarioDescriptor().getUniqueId(), endsWith("scenario", "feature-name;first-line-of-the-scenario"));
    }

    @Test
    public void scenarioOutlineId() throws Exception {
        cucumberFeatureBuilder.ScenarioOutline("first line of the scenario outline\nextended description");

        assertThat(scenarioOutlineDescriptor().getUniqueId(), endsWith("scenario-outline", "feature-name;first-line-of-the-scenario-outline"));
    }

    @Test
    @Ignore
    public void name() throws Exception {
        CucumberEngineDescriptor engineDescriptor = discoveredDescriptorsFor(
                anyFeatureFile().ScenarioOutline("text take\nExtended discription")
                        .Given("<arg1> <arg2>")
                        .Example("one", "two")
                        .Example("A", "B"));

        System.out.println(engineDescriptor);
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
        return discoveredDescriptorsFor(cucumberFeatureBuilder);
    }

    @Override
    public CucumberEngineFixture fixture() {
        return fixture;
    }
}