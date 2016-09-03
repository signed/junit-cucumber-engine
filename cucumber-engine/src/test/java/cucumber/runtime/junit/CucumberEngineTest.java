package cucumber.runtime.junit;

import cucumber.runtime.Backend;
import cucumber.runtime.Runtime;
import cucumber.runtime.RuntimeGlue;
import cucumber.runtime.RuntimeOptions;
import cucumber.runtime.StopWatch;
import cucumber.runtime.UndefinedStepsTracker;
import cucumber.runtime.model.CucumberFeature;
import cucumber.runtime.xstream.LocalizedXStreams;
import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Test;
import org.junit.platform.engine.ExecutionRequest;
import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.engine.UniqueId;

import java.util.Collection;
import java.util.Collections;

import static org.hamcrest.MatcherAssert.assertThat;

public class CucumberEngineTest {

    private final CucumberEngine engine = new CucumberEngine();
    private final CapturingEngineExecutionListener engineExecutionListener = new CapturingEngineExecutionListener();
    private final ClassLoader classLoader = this.getClass().getClassLoader();
    private final RuntimeGlue glue = new RuntimeGlue(new UndefinedStepsTracker(), new LocalizedXStreams(classLoader));


    @After
    public void allTestDescriptorsAreInAProperState() throws Exception {
        engineExecutionListener.ensureAllInProperEndState();
    }

    @Test
    public void reportSuccessfullyExecutedSteps() throws Exception {
        CucumberFeature cucumberFeature = CucumberFeatureMother.feature("feature/Path", "" +
                "Feature: feature name\n" +
                "  Scenario: scenario name\n" +
                "    Then it works\n");

        String stepText = "it works";

//        addStepDefinitionFor(stepText, stepExecutionFails());
        addStepDefinitionFor(stepText, stepExecutionSucceeds());

        UniqueId engineId = UniqueId.forEngine(CucumberEngine.ENGINE_ID);
        RuntimeOptions runtimeOptions = new RuntimeOptions("");
        Collection<? extends Backend> backends = Collections.singleton(new BackendStub());
        Runtime runtime = new Runtime(null, classLoader, backends, runtimeOptions, StopWatch.SYSTEM, glue);
        CucumberEngineDescriptor cucumberEngineDescriptor = new CucumberEngineDescriptor(engineId, runtime, runtimeOptions);
        cucumberEngineDescriptor.addChild(engine.createDescriptorFor(engineId, cucumberFeature));
        engine.execute(new ExecutionRequest(cucumberEngineDescriptor, engineExecutionListener, new NoConfigurationParameters()));
        ExecutionRecord record = engineExecutionListener.executionRecordFor(stepText);
        TestExecutionResult result = record.testExecutionResult;
        result.getThrowable().ifPresent(Throwable::printStackTrace);

        assertThat(result.getStatus(), CoreMatchers.equalTo(TestExecutionResult.Status.SUCCESSFUL));
    }

    private void addStepDefinitionFor(String stepText, Runnable stepExecution) {
        glue.addStepDefinition(new ConfigurableStepDefinition(stepText, stepExecution));
    }

    private Runnable stepExecutionSucceeds(){
        return () -> {
            //no exception marks success
        };
    }

    private Runnable stepExecutionFails() {
        return () -> {
            throw new RuntimeException();
        };
    }

}