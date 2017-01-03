package cucumber.runtime.junit;

import cucumber.api.StepDefinitionReporter;
import cucumber.runtime.StepDefinition;
import cucumber.runtime.model.CucumberFeature;
import gherkin.formatter.Argument;
import gherkin.formatter.model.Step;
import org.junit.platform.engine.TestSource;
import org.junit.platform.engine.support.descriptor.ClasspathResourceSource;
import org.junit.platform.engine.support.descriptor.CompositeTestSource;
import org.junit.platform.engine.support.descriptor.FilePosition;
import org.junit.platform.engine.support.descriptor.MethodSource;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

class CucumberInsight implements StepDefinitionReporter {
    private final List<StepDefinition> stepDefinitions = new ArrayList<>();
    private final MethodResolver methodResolver;
    private final CucumberFeature cucumberFeature;

    CucumberInsight(CucumberFeature cucumberFeature, MethodResolver methodResolver) {
        this.methodResolver = methodResolver;
        this.cucumberFeature = cucumberFeature;
    }

    @Override
    public void stepDefinition(StepDefinition stepDefinition) {
        stepDefinitions.add(stepDefinition);
    }

    Optional<TestSource> sourcesFor(Step step) {
        List<TestSource> sources = new ArrayList<>();
        stepDefinitionFor(step).map(this::resolveTestSource).ifPresent(sources::add);
        sources.add(new ClasspathResourceSource(cucumberFeature.getPath(), new FilePosition(step.getLine(), 1)));
        return Optional.of(new CompositeTestSource(sources));
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

    private MethodSource resolveTestSource(StepDefinition stepDefinition) {
        String details = stepDefinition.getLocation(true);
        Method method = methodResolver.resolve(details);
        return new MethodSource(method);
    }
}
