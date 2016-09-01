package cucumber.runtime.junit;

import cucumber.runtime.CucumberException;
import gherkin.formatter.Formatter;
import gherkin.formatter.Reporter;
import gherkin.formatter.model.Background;
import gherkin.formatter.model.Examples;
import gherkin.formatter.model.Feature;
import gherkin.formatter.model.Match;
import gherkin.formatter.model.Result;
import gherkin.formatter.model.Scenario;
import gherkin.formatter.model.ScenarioOutline;
import gherkin.formatter.model.Step;
import org.junit.platform.engine.EngineExecutionListener;
import org.junit.platform.engine.TestExecutionResult;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import static cucumber.runtime.Runtime.isPending;

class CucumberToJunitTranslator implements Reporter, Formatter {

    private final CucumberExecutionContext context;
    private final Queue<StepDescriptor> stepDescriptors;
    private final ScenarioDescriptor scenarioDescriptor;
    private final Queue<Step> steps = new LinkedList<>();
    private boolean inScenarioLifeCycle = false;
    private StepDescriptor currentStepDescriptor;
    private boolean ignoredStep = false;
    private Exception toReThrow;
    private boolean scenarioAborted;

    public CucumberToJunitTranslator(CucumberExecutionContext context, ScenarioDescriptor scenarioDescriptor) {
        this.context = context;
        this.scenarioDescriptor = scenarioDescriptor;
        this.stepDescriptors = scenarioDescriptor.childrenAsQeueu();
    }

    public Exception somethingToRethrow() {
        return toReThrow;
    }

    @Override
    public void syntaxError(String state, String event, List<String> legalEvents, String uri, Integer line) {

    }

    @Override
    public void uri(String uri) {

    }

    @Override
    public void feature(Feature feature) {

    }

    @Override
    public void scenarioOutline(ScenarioOutline scenarioOutline) {

    }

    @Override
    public void examples(Examples examples) {

    }

    @Override
    public void startOfScenarioLifeCycle(Scenario scenario) {
        this.inScenarioLifeCycle = true;
    }

    @Override
    public void background(Background background) {

    }

    @Override
    public void scenario(Scenario scenario) {

    }

    @Override
    public void step(Step step) {
        if (inScenarioLifeCycle) {
            steps.add(step);
        }
    }

    @Override
    public void endOfScenarioLifeCycle(Scenario scenario) {
        this.inScenarioLifeCycle = false;
    }

    @Override
    public void done() {

    }

    @Override
    public void close() {

    }

    @Override
    public void eof() {

    }

    @Override
    public void before(Match match, Result result) {
        handleHook(result);
    }

    @Override
    public void match(Match match) {
        if (scenarioAborted) {
            return;
        }
        currentStepDescriptor = fetchAndCheckRunnerStep();
        executionListener().executionStarted(currentStepDescriptor);
    }

    @Override
    public void result(Result result) {
        executionListener().executionFinished(currentStepDescriptor, TestExecutionResult.successful());
    }

    @Override
    public void after(Match match, Result result) {
        handleHook(result);
    }

    @Override
    public void embedding(String mimeType, byte[] data) {

    }

    @Override
    public void write(String text) {

    }

    private void handleHook(Result result) {
        if (Result.FAILED.equals(result.getStatus()) || (context.isStrict() && isPending(result.getError()))) {
            scenarioAborted = true;
            markRemainingStepsAsSkipped();
            this.toReThrow = (Exception) result.getError();
        } else {
            if (isPending(result.getError())) {
                ignoredStep = true;
            }
        }
    }

    private void markRemainingStepsAsSkipped() {
        for (StepDescriptor stepDescriptor : stepDescriptors) {
            executionListener().executionSkipped(stepDescriptor, "unknown");
        }
    }

    private StepDescriptor fetchAndCheckRunnerStep() {
        Step scenarioStep = steps.poll();
        StepDescriptor stepDescriptor = stepDescriptors.poll();
        Step runnerStep = stepDescriptor.step();
        if (!scenarioStep.getName().equals(runnerStep.getName())) {
            throw new CucumberException("Expected step: \"" + scenarioStep.getName() + "\" got step: \"" + runnerStep.getName() + "\"");
        }
        return stepDescriptor;
    }

    private EngineExecutionListener executionListener() {
        return context.executionRequest().getEngineExecutionListener();
    }
}
