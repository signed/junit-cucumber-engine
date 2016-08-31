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

class CucumberToJunitTranslator implements Reporter, Formatter {

    private final CucumberExecutionContext context;
    private final Queue<StepDescriptor> stepDescriptors;
    private final Queue<Step> steps = new LinkedList<>();
    private boolean inScenarioLifeCycle = false;
    private StepDescriptor currentStepDescriptor;

    public CucumberToJunitTranslator(CucumberExecutionContext context, Queue<StepDescriptor> stepDescriptors) {
        this.context = context;
        this.stepDescriptors = stepDescriptors;
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
        if(inScenarioLifeCycle){
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

    }

    @Override
    public void match(Match match) {
        currentStepDescriptor = fetchAndCheckRunnerStep();
        executionListener().executionStarted(currentStepDescriptor);
    }

    @Override
    public void result(Result result) {
        executionListener().executionFinished(currentStepDescriptor, TestExecutionResult.successful());
    }

    @Override
    public void after(Match match, Result result) {

    }

    @Override
    public void embedding(String mimeType, byte[] data) {

    }

    @Override
    public void write(String text) {

    }

    private StepDescriptor fetchAndCheckRunnerStep() {
        Step scenarioStep = steps.poll();
        StepDescriptor stepDescriptor = this.stepDescriptors.poll();
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
