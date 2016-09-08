package cucumber.runtime.junit;

import cucumber.api.StepDefinitionReporter;
import cucumber.runtime.StepDefinition;
import cucumber.runtime.model.CucumberFeature;
import gherkin.formatter.Argument;
import gherkin.formatter.model.Step;
import org.junit.platform.engine.TestSource;
import org.junit.platform.engine.support.descriptor.JavaMethodSource;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

class CucumberInsight implements StepDefinitionReporter {
    private final MethodResolver methodResolver = new MethodResolver();
    private final List<StepDefinition> stepDefinitions = new ArrayList<>();
    private final CucumberFeature cucumberFeature;

    public CucumberInsight(CucumberFeature cucumberFeature) {
        this.cucumberFeature = cucumberFeature;
    }

    @Override
    public void stepDefinition(StepDefinition stepDefinition) {
        stepDefinitions.add(stepDefinition);
    }

    Optional<TestSource> sourcesFor(Step step) {
        return stepDefinitionFor(step).map(this::resolveTestSource);
    }

    private Optional<StepDefinition> stepDefinitionFor(Step step) {
        for (StepDefinition stepDefinition : stepDefinitions) {
            List<Argument> arguments = stepDefinition.matchedArguments(step);
            if (arguments != null) {
                return Optional.of(stepDefinition);
            }
        }
        return Optional.empty();
    }

    private JavaMethodSource resolveTestSource(StepDefinition stepDefinition) {
        String details = stepDefinition.getLocation(true);
        Method method = methodResolver.resolve(details);
        return new JavaMethodSource(method);
    }
}
