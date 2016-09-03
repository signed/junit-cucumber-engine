package cucumber.runtime.junit;

import org.junit.After;
import org.junit.Test;

import static cucumber.runtime.junit.CucumberEngineFixture.stepExecutionFails;
import static cucumber.runtime.junit.CucumberEngineFixture.stepExecutionSucceeds;
import static cucumber.runtime.junit.CucumberFeatureMother.anyScenario;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.platform.engine.TestExecutionResult.Status.FAILED;
import static org.junit.platform.engine.TestExecutionResult.Status.SUCCESSFUL;

public class CucumberEngineTest {

    private final CucumberEngineFixture fixture = new CucumberEngineFixture();

    @After
    public void allTestDescriptorsAreInAProperState() throws Exception {
        fixture.engineExecutionListener.ensureAllInProperEndState();
    }

    @Test
    public void reportSuccessfullyExecutedSteps() throws Exception {
        fixture.addStepDefinitionFor("it works", stepExecutionSucceeds());
        fixture.run(anyScenario().Then("it works"));

        assertThat(fixture.executionReportFor("it works").testExecutionResult.getStatus(), equalTo(SUCCESSFUL));
    }

    @Test
    public void reportFailedExecutedSteps() throws Exception {
        fixture.addStepDefinitionFor("it works", stepExecutionFails());
        fixture.run(anyScenario().Then("it works"));

        assertThat(fixture.executionReportFor("it works").testExecutionResult.getStatus(), equalTo(FAILED));
    }


}