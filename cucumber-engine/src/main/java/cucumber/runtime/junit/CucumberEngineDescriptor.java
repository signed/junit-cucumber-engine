package cucumber.runtime.junit;

import cucumber.runtime.Runtime;
import cucumber.runtime.RuntimeOptions;
import org.junit.platform.engine.UniqueId;
import org.junit.platform.engine.support.descriptor.EngineDescriptor;
import org.junit.platform.engine.support.hierarchical.Node;

public class CucumberEngineDescriptor extends EngineDescriptor implements Node<CucumberExecutionContext> {

    private final Runtime runtime;
    private final RuntimeOptions runtimeOptions;

    public CucumberEngineDescriptor(UniqueId uniqueId, Runtime runtime, RuntimeOptions runtimeOptions) {
        super(uniqueId, "cucumber");
        this.runtime = runtime;
        this.runtimeOptions = runtimeOptions;
    }

    public Runtime runtime() {
        return runtime;
    }

    public RuntimeOptions runtimeOptions(){
        return runtimeOptions;
    }

    @Override
    public CucumberExecutionContext execute(CucumberExecutionContext context) throws Exception {
        System.out.println(getUniqueId());
        return context;
    }
}
