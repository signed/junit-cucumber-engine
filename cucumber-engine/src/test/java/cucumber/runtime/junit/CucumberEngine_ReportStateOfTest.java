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

public class CucumberEngine_ReportStateOfTest implements CucumberEngineTestSugar{

    private final CucumberEngineFixture fixture = new CucumberEngineFixture();

    @After
    public void allTestDescriptorsAreInAProperState() throws Exception {
        fixture.engineExecutionListener.ensureAllInProperEndState();
    }

    @Test
    public void successfullyExecutedSteps() throws Exception {
        stepImplementationFor("a step", whereExecutionSucceeds());
        run(anyScenario().AStep("a step"));

        assertThat(executionRecordFor("a step"), successful());
    }

    @Test
    public void failedSteps() throws Exception {
        stepImplementationFor("a step", whereExecutionFails());
        run(anyScenario().AStep("a step"));

        assertThat(executionRecordFor("a step"), failed());
    }

    @Test
    public void stepsAfterAFailingStepAsSkipped() throws Exception {
        stepImplementationFor("failing step", whereExecutionFails());
        stepImplementationFor("after failing step");

        run(anyScenario().Given("failing step").Then("after failing step"));

        assertThat(executionRecordFor("after failing step"), skipped());
    }

    @Test
    public void stepsAfterAFailingBeforeHookAsSkipped() throws Exception {
        beforeHookImplementation(whereExecutionFails());
        stepImplementationFor("after failing before hook");

        run(anyScenario().AStep("after failing before hook"));
        assertThat(executionRecordFor("after failing before hook"), skipped());
    }

    @Override
    public CucumberEngineFixture fixture() {
        return fixture;
    }
}