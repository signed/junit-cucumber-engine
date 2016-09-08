package cucumber.runtime.junit;

import gherkin.formatter.model.Step;
import org.junit.platform.engine.TestSource;
import org.junit.platform.engine.UniqueId;
import org.junit.platform.engine.support.descriptor.AbstractTestDescriptor;
import org.junit.platform.engine.support.hierarchical.Node;

import java.util.Optional;

class StepDescriptor extends AbstractTestDescriptor implements Node<CucumberExecutionContext> {

    private final Step step;

    StepDescriptor(UniqueId uniqueId, String displayName, Step step, Optional<TestSource> source) {
        super(uniqueId, displayName);
        this.step = step;
        source.ifPresent(this::setSource);
    }

    public Step step() {
        return step;
    }

    @Override
    public boolean isLeaf() {
        return true;
    }

    @Override
    public boolean isContainer() {
        return false;
    }

    @Override
    public boolean isTest() {
        return true;
    }

    @Override
    public CucumberExecutionContext execute(CucumberExecutionContext context) throws Exception {
        System.out.println(getUniqueId());
        return context;
    }
}
