package cucumber.runtime.junit;

import cucumber.runtime.model.CucumberScenario;
import org.junit.platform.engine.UniqueId;
import org.junit.platform.engine.support.descriptor.AbstractTestDescriptor;
import org.junit.platform.engine.support.hierarchical.Node;

class ScenarioDescriptor extends AbstractTestDescriptor implements Node<CucumberExecutionContext> {

    private final CucumberScenario cucumberScenario;

    protected ScenarioDescriptor(UniqueId uniqueId, String displayName, CucumberScenario cucumberScenario) {
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
