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

public class CucumberEngine_ReportStateOfTest {

    private final CucumberEngineFixture fixture = new CucumberEngineFixture();

    @After
    public void allTestDescriptorsAreInAProperState() throws Exception {
        fixture.engineExecutionListener.ensureAllInProperEndState();
    }

    @Test
    public void successfullyExecutedSteps() throws Exception {
        fixture.stepImplementationFor("it works", whereExecutionSucceeds());
        fixture.run(anyScenario().Then("it works"));

        assertThat(fixture.executionReportFor("it works"), successful());
    }

    @Test
    public void failedSteps() throws Exception {
        fixture.stepImplementationFor("it works", whereExecutionFails());
        fixture.run(anyScenario().Then("it works"));

        assertThat(fixture.executionReportFor("it works"), failed());
    }

    @Test
    public void stepsAfterAFailingStepAsSkipped() throws Exception {
        fixture.stepImplementationFor("failing step", whereExecutionFails());
        fixture.stepImplementationFor("after failing step");

        fixture.run(anyScenario().Given("failing step").Then("after failing step"));

        assertThat(fixture.executionReportFor("after failing step"), skipped());
    }

    @Test
    public void stepsAfterAFailingBeforeHookAsSkipped() throws Exception {
        fixture.beforeHookImplementation(whereExecutionFails());
        fixture.stepImplementationFor("after failing before hook");

        fixture.run(anyScenario().AStep("after failing before hook"));
        assertThat(fixture.executionReportFor("after failing before hook"), skipped());
    }
}