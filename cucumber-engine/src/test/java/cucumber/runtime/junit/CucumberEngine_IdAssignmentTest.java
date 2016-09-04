package cucumber.runtime.junit;

import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.platform.engine.TestDescriptor;
import org.junit.platform.engine.UniqueId;

import static cucumber.runtime.junit.CucumberFeatureMother.anyFeatureFile;
import static cucumber.runtime.junit.CucumberFeatureMother.anyScenario;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class CucumberEngine_IdAssignmentTest implements CucumberEngineTestSugar {

    private final CucumberEngineFixture fixture = new CucumberEngineFixture();

    @After
    public void allTestDescriptorsAreInAProperState() throws Exception {
        fixture.engineExecutionListener.ensureAllInProperEndState();
    }

    @Test
    public void topLevelCucumberEngine() throws Exception {
        CucumberEngineDescriptor engineDescriptor = discoveredDescriptorsFor(anyScenario().When("does not matter"));

        assertThat(engineDescriptor.getUniqueId(), equalTo(UniqueId.forEngine("cucumber-jvm")));
    }

    @Test
    public void pathToFeatureFile() throws Exception {
        CucumberEngineDescriptor engineDescriptor = discoveredDescriptorsFor(anyScenario().path("/the/path").When("does not matter"));
        TestDescriptor path = engineDescriptor.getChildren().iterator().next();

        assertThat(path.getUniqueId(), equalTo(UniqueId.forEngine("cucumber-jvm").append("feature", "/the/path")));
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

    @Override
    public CucumberEngineFixture fixture() {
        return fixture;
    }
}