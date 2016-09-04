package cucumber.runtime.junit;

import org.junit.After;
import org.junit.Test;

import static cucumber.runtime.junit.CucumberEngineFixture.whereExecutionSucceeds;
import static cucumber.runtime.junit.CucumberFeatureMother.anyScenarioOutline;
import static cucumber.runtime.junit.ExecutionRecordMatcher.successful;
import static org.hamcrest.MatcherAssert.assertThat;

public class CucumberEngine_ScenarioOutlineTest implements CucumberEngineTestSugar {

    private final CucumberEngineFixture fixture = new CucumberEngineFixture();

    @After
    public void allTestDescriptorsAreInAProperState() throws Exception {
        fixture.engineExecutionListener.ensureAllInProperEndState();
    }

    @Test
    public void successfullyExecutedSteps() throws Exception {
        stepImplementationFor("argument", whereExecutionSucceeds());
        run(anyScenarioOutline()
                .Given("<parameter>")
                .Example("argument"));

        assertThat(executionRecordFor("argument"), successful());
    }


    @Override
    public CucumberEngineFixture fixture() {
        return fixture;
    }
}