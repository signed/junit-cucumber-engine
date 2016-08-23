package cucumber.runtime.junit;

import cucumber.runtime.ClassFinder;
import cucumber.runtime.Runtime;
import cucumber.runtime.RuntimeOptions;
import cucumber.runtime.RuntimeOptionsFactory;
import cucumber.runtime.io.MultiLoader;
import cucumber.runtime.io.ResourceLoader;
import cucumber.runtime.io.ResourceLoaderClassFinder;
import cucumber.runtime.model.CucumberExamples;
import cucumber.runtime.model.CucumberFeature;
import cucumber.runtime.model.CucumberScenario;
import cucumber.runtime.model.CucumberScenarioOutline;
import cucumber.runtime.model.CucumberTagStatement;
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
import org.junit.platform.engine.EngineDiscoveryRequest;
import org.junit.platform.engine.ExecutionRequest;
import org.junit.platform.engine.TestDescriptor;
import org.junit.platform.engine.UniqueId;
import org.junit.platform.engine.discovery.ClassSelector;
import org.junit.platform.engine.support.descriptor.AbstractTestDescriptor;
import org.junit.platform.engine.support.descriptor.EngineDescriptor;
import org.junit.platform.engine.support.hierarchical.HierarchicalTestEngine;
import org.junit.platform.engine.support.hierarchical.Node;

import java.util.ArrayList;
import java.util.List;

public class CucumberEngine extends HierarchicalTestEngine<CucumberExecutionContext> {

    public static final String ENGINE_ID = "cucumber-jvm";

    @Override
    public String getId() {
        return ENGINE_ID;
    }

    @Override
    public TestDescriptor discover(EngineDiscoveryRequest discoveryRequest, UniqueId uniqueId) {

        ClassSelector selector = discoveryRequest.getSelectorsByType(ClassSelector.class).get(0);
        Class<?> clazz = selector.getJavaClass();

        RuntimeOptionsFactory runtimeOptionsFactory = new RuntimeOptionsFactory(clazz);
        RuntimeOptions runtimeOptions = runtimeOptionsFactory.create();
        runtimeOptions.getFeaturePaths().add("classpath:features");
        runtimeOptions.getGlue().add("glue");

        ClassLoader classLoader = clazz.getClassLoader();
        ResourceLoader resourceLoader = new MultiLoader(classLoader);
        final List<CucumberFeature> cucumberFeatures = runtimeOptions.cucumberFeatures(resourceLoader);

        ClassFinder classFinder = new ResourceLoaderClassFinder(resourceLoader, classLoader);
        Runtime runtime = new Runtime(resourceLoader, classFinder, classLoader, runtimeOptions);
        CucumberEngineDescriptor cucumber = new CucumberEngineDescriptor(uniqueId, "cucumber", runtime);

        for (CucumberFeature cucumberFeature : cucumberFeatures) {
            cucumber.addChild(createDescriptorFor(uniqueId, cucumberFeature));
        }

        return cucumber;
    }

    public static class CucumberEngineDescriptor extends EngineDescriptor implements Node<CucumberExecutionContext> {

        private final Runtime runtime;

        public CucumberEngineDescriptor(UniqueId uniqueId, String displayName, Runtime runtime) {
            super(uniqueId, displayName);
            this.runtime = runtime;
        }

        public Runtime runtime() {
            return runtime;
        }
    }

    private CucumberFeatureDescriptor createDescriptorFor(UniqueId parentId, CucumberFeature cucumberFeature) {
        UniqueId featureFileId = parentId.append("path", cucumberFeature.getPath());
        CucumberFeatureDescriptor result = new CucumberFeatureDescriptor(featureFileId, cucumberFeature);
        for (CucumberTagStatement cucumberTagStatement : cucumberFeature.getFeatureElements()) {
            result.addChild(createDescriptorFor(featureFileId, cucumberTagStatement));
        }

        return result;
    }

    private TestDescriptor createDescriptorFor(UniqueId parentId, CucumberTagStatement cucumberTagStatement) {
        if (cucumberTagStatement instanceof CucumberScenario) {
            return createDescriptorFor(parentId, (CucumberScenario) cucumberTagStatement);
        }
        return createDescriptorFor(parentId, (CucumberScenarioOutline) cucumberTagStatement);
    }

    private CucumberScenarioOutlineDescriptor createDescriptorFor(UniqueId parentId, CucumberScenarioOutline cucumberScenarioOutline) {
        UniqueId scenarioOutlineId = parentId.append("scenario-outline", extractId(cucumberScenarioOutline));
        CucumberScenarioOutlineDescriptor descriptor = new CucumberScenarioOutlineDescriptor(scenarioOutlineId, cucumberScenarioOutline.getVisualName(), cucumberScenarioOutline);

        for (CucumberExamples cucumberExamples : cucumberScenarioOutline.getCucumberExamplesList()) {
            List<CucumberScenario> exampleScenarios = cucumberExamples.createExampleScenarios();
            for (CucumberScenario exampleScenario : exampleScenarios) {
                descriptor.addChild(createDescriptorFor(scenarioOutlineId, exampleScenario));
            }
        }
        return descriptor;
    }

    private CucumberScenarioDescriptor createDescriptorFor(UniqueId parentId, CucumberScenario cucumberScenario) {
        final UniqueId scenarioId = parentId.append("scenario", extractId(cucumberScenario));
        final CucumberScenarioDescriptor descriptor = new CucumberScenarioDescriptor(scenarioId, cucumberScenario.getVisualName(), cucumberScenario);

        List<Step> allSteps = new ArrayList<Step>();
        for (Step backgroundStep : cucumberScenario.getCucumberBackground().getSteps()) {
            Step copy = new Step(
                    backgroundStep.getComments(),
                    backgroundStep.getKeyword(),
                    backgroundStep.getName(),
                    backgroundStep.getLine(),
                    backgroundStep.getRows(),
                    backgroundStep.getDocString()
            );
            allSteps.add(copy);
        }
        allSteps.addAll(cucumberScenario.getSteps());
        for (Step step : allSteps) {
            descriptor.addChild(new CucumberStepDescriptor(scenarioId.append("step", step.getName()), step.getName(), step));
        }
        return descriptor;
    }

    private String extractId(CucumberTagStatement cucumberScenario) {
        return cucumberScenario.getGherkinModel().getId();
    }

    @Override
    protected CucumberExecutionContext createExecutionContext(ExecutionRequest request) {
        CucumberEngineDescriptor rootTestDescriptor = (CucumberEngineDescriptor) request.getRootTestDescriptor();
        return new CucumberExecutionContext(rootTestDescriptor.runtime());
    }

    private static class CucumberStepDescriptor extends AbstractTestDescriptor implements Node<CucumberExecutionContext> {

        private final Step step;

        protected CucumberStepDescriptor(UniqueId uniqueId, String displayName, Step step) {
            super(uniqueId, displayName);
            this.step = step;
        }

        @Override
        public boolean isContainer() {
            return false;
        }

        @Override
        public boolean isTest() {
            return true;
        }
    }

    private static class CucumberScenarioOutlineDescriptor extends AbstractTestDescriptor implements Node<CucumberExecutionContext> {

        protected CucumberScenarioOutlineDescriptor(UniqueId uniqueId, String displayName, CucumberScenarioOutline cucumberScenarioOutline) {
            super(uniqueId, displayName);
        }

        @Override
        public boolean isContainer() {
            return true;
        }

        @Override
        public boolean isTest() {
            return false;
        }
    }

    private static class CucumberScenarioDescriptor extends AbstractTestDescriptor implements Node<CucumberExecutionContext> {

        private final CucumberScenario cucumberScenario;

        protected CucumberScenarioDescriptor(UniqueId uniqueId, String displayName, CucumberScenario cucumberScenario) {
            super(uniqueId, displayName);
            this.cucumberScenario = cucumberScenario;
        }

        @Override
        public CucumberExecutionContext execute(CucumberExecutionContext context) throws Exception {
            CucumberToJunitTranslator translator = new CucumberToJunitTranslator();
            this.cucumberScenario.run(translator, translator, context.runtime());
            return context;
        }

        @Override
        public boolean isContainer() {
            return true;
        }

        @Override
        public boolean isTest() {
            return false;
        }
    }

    private static class CucumberToJunitTranslator implements Reporter, Formatter {

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

        }

        @Override
        public void background(Background background) {

        }

        @Override
        public void scenario(Scenario scenario) {

        }

        @Override
        public void step(Step step) {

        }

        @Override
        public void endOfScenarioLifeCycle(Scenario scenario) {

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
        public void result(Result result) {

        }

        @Override
        public void after(Match match, Result result) {

        }

        @Override
        public void match(Match match) {

        }

        @Override
        public void embedding(String mimeType, byte[] data) {

        }

        @Override
        public void write(String text) {

        }
    }

    private static class CucumberFeatureDescriptor extends AbstractTestDescriptor implements Node<CucumberExecutionContext> {

        private final CucumberFeature cucumberFeature;

        public CucumberFeatureDescriptor(UniqueId featureFileId, CucumberFeature cucumberFeature) {
            super(featureFileId, cucumberFeature.getGherkinFeature().getName());
            this.cucumberFeature = cucumberFeature;
        }

        @Override
        public boolean isContainer() {
            return true;
        }

        @Override
        public boolean isTest() {
            return false;
        }

    }
}
