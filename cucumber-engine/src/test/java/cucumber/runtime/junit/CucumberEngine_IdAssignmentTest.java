package cucumber.runtime.junit;

import org.junit.After;
import org.junit.Test;
import org.junit.platform.engine.UniqueId;

import static cucumber.runtime.junit.CucumberFeatureMother.anyScenarioOutline;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class CucumberEngine_IdAssignmentTest implements CucumberEngineTestSugar {

    private final CucumberEngineFixture fixture = new CucumberEngineFixture();

    @After
    public void allTestDescriptorsAreInAProperState() throws Exception {
        fixture.engineExecutionListener.ensureAllInProperEndState();
    }

    @Test
    public void toplevelCucumberEngine() throws Exception {
        CucumberEngineDescriptor engineDescriptor = discoveredDescriptorsFor(anyScenarioOutline().When("blub"));

        assertThat(engineDescriptor.getUniqueId(), equalTo(UniqueId.forEngine("cucumber-jvm")));
    }

    @Override
    public CucumberEngineFixture fixture() {
        return fixture;
    }
}