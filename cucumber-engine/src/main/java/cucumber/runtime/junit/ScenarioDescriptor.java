package cucumber.runtime.junit;

import cucumber.runtime.model.CucumberScenario;
import org.junit.platform.engine.TestDescriptor;
import org.junit.platform.engine.TestSource;
import org.junit.platform.engine.UniqueId;
import org.junit.platform.engine.support.descriptor.AbstractTestDescriptor;
import org.junit.platform.engine.support.hierarchical.Node;

import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;

class ScenarioDescriptor extends AbstractTestDescriptor implements Node<CucumberExecutionContext> {

    private final CucumberScenario cucumberScenario;

    ScenarioDescriptor(UniqueId uniqueId, String displayName, CucumberScenario cucumberScenario, Optional<TestSource> featureSource) {
        super(uniqueId, displayName);
        this.cucumberScenario = cucumberScenario;
        featureSource.ifPresent(this::setSource);
    }

    @Override
    public CucumberExecutionContext execute(CucumberExecutionContext context) throws Exception {
        CucumberToJunitTranslator translator = new CucumberToJunitTranslator(context, this);
        this.cucumberScenario.run(translator, translator, context.runtime());
        if (null != translator.somethingToRethrow()) {
            throw translator.somethingToRethrow();
        }
        return context;
    }

    Queue<StepDescriptor> childrenAsQueue() {
        Set<? extends TestDescriptor> children = this.getChildren();
        Queue<StepDescriptor> steps = new LinkedList<>();
        for (TestDescriptor child : children) {
            steps.add((StepDescriptor) child);
        }
        return steps;
    }

    @Override
    public boolean isLeaf() {
        return true;
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
