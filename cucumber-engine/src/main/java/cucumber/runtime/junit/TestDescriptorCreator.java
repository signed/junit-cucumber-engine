package cucumber.runtime.junit;

import cucumber.runtime.Runtime;
import cucumber.runtime.RuntimeOptions;
import cucumber.runtime.model.CucumberExamples;
import cucumber.runtime.model.CucumberFeature;
import cucumber.runtime.model.CucumberScenario;
import cucumber.runtime.model.CucumberScenarioOutline;
import cucumber.runtime.model.CucumberTagStatement;
import gherkin.formatter.model.Step;
import org.junit.platform.engine.TestDescriptor;
import org.junit.platform.engine.UniqueId;

import java.util.ArrayList;
import java.util.List;

class TestDescriptorCreator {

    private final UniqueId engineId;
    private final RuntimeOptions runtimeOptions;
    private final Runtime runtime;

    TestDescriptorCreator(UniqueId uniqueId, RuntimeOptions runtimeOptions, Runtime runtime) {
        this.engineId = uniqueId;
        this.runtimeOptions = runtimeOptions;
        this.runtime = runtime;
    }

    CucumberEngineDescriptor createEngineDescriptorFor(List<CucumberFeature> cucumberFeatures) {
        CucumberEngineDescriptor cucumber = new CucumberEngineDescriptor(engineId, runtime, runtimeOptions);
        for (CucumberFeature cucumberFeature : cucumberFeatures) {
            cucumber.addChild(createFeatureDescriptorFor(cucumberFeature));
        }
        return cucumber;
    }

    private FeatureDescriptor createFeatureDescriptorFor(CucumberFeature cucumberFeature) {
        UniqueId featureFileId = engineId.append("feature", cucumberFeature.getGherkinFeature().getId());
        FeatureDescriptor result = new FeatureDescriptor(featureFileId, cucumberFeature);
        for (CucumberTagStatement cucumberTagStatement : cucumberFeature.getFeatureElements()) {
            result.addChild(createDescriptorFor(featureFileId, cucumberTagStatement));
        }

        return result;
    }

    private TestDescriptor createDescriptorFor(UniqueId parentId, CucumberTagStatement cucumberTagStatement) {
        if (cucumberTagStatement instanceof CucumberScenario) {
            return createScenarioDescriptorFor((CucumberScenario) cucumberTagStatement, parentId);
        }
        return createOutlineDescriptorFor((CucumberScenarioOutline) cucumberTagStatement, parentId);
    }

    private ScenarioDescriptor createScenarioDescriptorFor(CucumberScenario cucumberScenario, UniqueId parentId) {
        final UniqueId scenarioId = parentId.append("scenario", extractId(cucumberScenario));
        final ScenarioDescriptor descriptor = new ScenarioDescriptor(scenarioId, cucumberScenario.getVisualName(), cucumberScenario);

        List<Step> allSteps = new ArrayList<>();
        if (null != cucumberScenario.getCucumberBackground()) {
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
        }
        allSteps.addAll(cucumberScenario.getSteps());
        for (Step step : allSteps) {
            descriptor.addChild(new StepDescriptor(scenarioId.append("step", step.getName()), step.getName(), step));
        }
        return descriptor;
    }

    private OutlineDescriptor createOutlineDescriptorFor(CucumberScenarioOutline cucumberScenarioOutline, UniqueId parentId) {
        UniqueId scenarioOutlineId = parentId.append("scenario-outline", extractId(cucumberScenarioOutline));
        OutlineDescriptor descriptor = new OutlineDescriptor(scenarioOutlineId, cucumberScenarioOutline.getVisualName(), cucumberScenarioOutline);

        for (CucumberExamples cucumberExamples : cucumberScenarioOutline.getCucumberExamplesList()) {
            List<CucumberScenario> exampleScenarios = cucumberExamples.createExampleScenarios();
            for (CucumberScenario exampleScenario : exampleScenarios) {
                descriptor.addChild(createScenarioDescriptorFor(exampleScenario, scenarioOutlineId));
            }
        }
        return descriptor;
    }

    private String extractId(CucumberTagStatement cucumberScenario) {
        return cucumberScenario.getGherkinModel().getId();
    }
}
