package cucumber.runtime.junit;

import cucumber.api.StepDefinitionReporter;
import cucumber.runtime.StepDefinition;
import cucumber.runtime.model.CucumberFeature;
import cucumber.runtime.model.CucumberScenario;
import gherkin.formatter.Argument;
import gherkin.formatter.model.BasicStatement;
import gherkin.formatter.model.Step;
import org.junit.platform.engine.TestSource;
import org.junit.platform.engine.support.descriptor.CompositeTestSource;
import org.junit.platform.engine.support.descriptor.FilePosition;
import org.junit.platform.engine.support.descriptor.FileSource;
import org.junit.platform.engine.support.descriptor.MethodSource;

import java.lang.reflect.Method;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Optional.ofNullable;

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

    Optional<TestSource> sourcesFor(CucumberFeature cucumberFeature) {
        return ofNullable(fileSourceFor(cucumberFeature.getGherkinFeature()));
    }

    Optional<TestSource> sourcesFor(CucumberScenario cucumberScenario) {
        return ofNullable(fileSourceFor(cucumberScenario.getGherkinModel()));
    }

    Optional<TestSource> sourcesFor(Step step) {
        List<TestSource> sources = new ArrayList<>();
        sources.add(fileSourceFor(step));
        stepDefinitionFor(step).map(this::resolveTestSource).ifPresent(sources::add);
        if(sources.isEmpty()){
            return Optional.empty();
        }
        if (sources.size() == 1) {
            return Optional.of(sources.get(0));
        }
        return Optional.of(new CompositeTestSource(sources));
    }

    private TestSource fileSourceFor(BasicStatement basicStatement) {
        String path = cucumberFeature.getPath();
        FilePosition filePosition = new FilePosition(basicStatement.getLine(), 1);
        return new FileSource(Paths.get(path).toAbsolutePath().toFile(), filePosition);
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
