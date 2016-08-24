package cucumber.runtime.junit;

import cucumber.runtime.Runtime;
import org.junit.platform.engine.UniqueId;
import org.junit.platform.engine.support.descriptor.EngineDescriptor;
import org.junit.platform.engine.support.hierarchical.Node;

public class CucumberEngineDescriptor extends EngineDescriptor implements Node<CucumberExecutionContext> {

    private final Runtime runtime;

    public CucumberEngineDescriptor(UniqueId uniqueId, String displayName, Runtime runtime) {
        super(uniqueId, displayName);
        this.runtime = runtime;
    }

    public Runtime runtime() {
        return runtime;
    }

    @Override
    public CucumberExecutionContext execute(CucumberExecutionContext context) throws Exception {
        System.out.println(getUniqueId());
        return context;
    }
}
