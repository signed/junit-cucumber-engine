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
import gherkin.formatter.model.Step;
import org.junit.platform.engine.EngineDiscoveryRequest;
import org.junit.platform.engine.ExecutionRequest;
import org.junit.platform.engine.TestDescriptor;
import org.junit.platform.engine.UniqueId;
import org.junit.platform.engine.discovery.ClassSelector;
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

    private FeatureDescriptor createDescriptorFor(UniqueId parentId, CucumberFeature cucumberFeature) {
        UniqueId featureFileId = parentId.append("path", cucumberFeature.getPath());
        FeatureDescriptor result = new FeatureDescriptor(featureFileId, cucumberFeature);
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

    private OutlineDescriptor createDescriptorFor(UniqueId parentId, CucumberScenarioOutline cucumberScenarioOutline) {
        UniqueId scenarioOutlineId = parentId.append("scenario-outline", extractId(cucumberScenarioOutline));
        OutlineDescriptor descriptor = new OutlineDescriptor(scenarioOutlineId, cucumberScenarioOutline.getVisualName(), cucumberScenarioOutline);

        for (CucumberExamples cucumberExamples : cucumberScenarioOutline.getCucumberExamplesList()) {
            List<CucumberScenario> exampleScenarios = cucumberExamples.createExampleScenarios();
            for (CucumberScenario exampleScenario : exampleScenarios) {
                descriptor.addChild(createDescriptorFor(scenarioOutlineId, exampleScenario));
            }
        }
        return descriptor;
    }

    private ScenarioDescriptor createDescriptorFor(UniqueId parentId, CucumberScenario cucumberScenario) {
        final UniqueId scenarioId = parentId.append("scenario", extractId(cucumberScenario));
        final ScenarioDescriptor descriptor = new ScenarioDescriptor(scenarioId, cucumberScenario.getVisualName(), cucumberScenario);

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
            descriptor.addChild(new StepDescriptor(scenarioId.append("step", step.getName()), step.getName(), step));
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

}
