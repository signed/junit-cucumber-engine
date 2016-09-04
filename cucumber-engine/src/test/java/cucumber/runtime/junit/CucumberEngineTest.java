package cucumber.runtime.junit;

import org.junit.After;
import org.junit.Test;

import static cucumber.runtime.junit.CucumberEngineFixture.whereExecutionFails;
import static cucumber.runtime.junit.CucumberEngineFixture.whereExecutionSucceeds;
import static cucumber.runtime.junit.CucumberFeatureMother.anyScenario;
import static cucumber.runtime.junit.ExecutionRecordMatcher.failed;
import static cucumber.runtime.junit.ExecutionRecordMatcher.skipped;
import static cucumber.runtime.junit.ExecutionRecordMatcher.successful;
import static org.hamcrest.MatcherAssert.assertThat;

public class CucumberEngineTest {

    private final CucumberEngineFixture fixture = new CucumberEngineFixture();

    @After
    public void allTestDescriptorsAreInAProperState() throws Exception {
        fixture.engineExecutionListener.ensureAllInProperEndState();
    }

    @Test
    public void reportSuccessfullyExecutedSteps() throws Exception {
        fixture.stepImplementationFor("it works", whereExecutionSucceeds());
        fixture.run(anyScenario().Then("it works"));

        assertThat(fixture.executionReportFor("it works"), successful());
    }

    @Test
    public void reportFailedExecutedSteps() throws Exception {
        fixture.stepImplementationFor("it works", whereExecutionFails());
        fixture.run(anyScenario().Then("it works"));

        assertThat(fixture.executionReportFor("it works"), failed());
    }

    @Test
    public void markedStepsAfterAFailingStepAsSkipped() throws Exception {
        fixture.stepImplementationFor("failing step", whereExecutionFails());
        fixture.stepImplementationFor("after failing step");

        fixture.run(anyScenario().Given("failing step").Then("after failing step"));

        assertThat(fixture.executionReportFor("after failing step"), skipped());
    }

}