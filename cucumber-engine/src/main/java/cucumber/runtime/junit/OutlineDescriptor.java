package cucumber.runtime.junit;

import cucumber.runtime.model.CucumberScenarioOutline;
import org.junit.platform.engine.UniqueId;
import org.junit.platform.engine.support.descriptor.AbstractTestDescriptor;
import org.junit.platform.engine.support.hierarchical.Node;

class OutlineDescriptor extends AbstractTestDescriptor implements Node<CucumberExecutionContext> {

    private final CucumberScenarioOutline cucumberScenarioOutline;

    protected OutlineDescriptor(UniqueId uniqueId, String displayName, CucumberScenarioOutline cucumberScenarioOutline) {
        super(uniqueId, displayName);
        this.cucumberScenarioOutline = cucumberScenarioOutline;
    }

    @Override
    public CucumberExecutionContext execute(CucumberExecutionContext context) throws Exception {
        System.out.println();
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
