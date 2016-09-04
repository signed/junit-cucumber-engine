package cucumber.runtime.junit;

import cucumber.runtime.ParameterInfo;
import cucumber.runtime.StepDefinition;
import gherkin.I18n;
import gherkin.formatter.Argument;
import gherkin.formatter.model.Step;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

class ConfigurableStepDefinition implements StepDefinition {
    private final String stepText;
    private final Runnable stepExecution;

    ConfigurableStepDefinition(String stepText, Runnable stepExecution) {
        this.stepText = stepText;
        this.stepExecution = stepExecution;
    }

    @Override
    public List<Argument> matchedArguments(Step step) {
        if (stepText.equals(step.getName())) {
            return new ArrayList<>();
        }
        return null;
    }

    @Override
    public String getLocation(boolean detail) {
        return "stand-in-location";
    }

    @Override
    public Integer getParameterCount() {
        return 0;
    }

    @Override
    public ParameterInfo getParameterType(int n, Type argumentType) throws IndexOutOfBoundsException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void execute(I18n i18n, Object[] args) throws Throwable {
        stepExecution.run();
    }

    @Override
    public boolean isDefinedAt(StackTraceElement stackTraceElement) {
        return true;
    }

    @Override
    public String getPattern() {
        return "^" + stepText + "$";
    }

    @Override
    public boolean isScenarioScoped() {
        return false;
    }

    @Override
    public String toString() {
        return stepText;
    }
}
