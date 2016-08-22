package cucumber.runtime.junit;

import org.junit.platform.engine.support.hierarchical.EngineExecutionContext;
import cucumber.runtime.Runtime;

public class CucumberExecutionContext implements EngineExecutionContext {
    private final Runtime runtime;

    public CucumberExecutionContext(Runtime runtime) {
        this.runtime = runtime;
    }

    public Runtime runtime(){
        return runtime;
    }
}
