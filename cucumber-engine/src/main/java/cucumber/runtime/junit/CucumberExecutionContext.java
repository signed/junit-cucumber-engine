package cucumber.runtime.junit;

import org.junit.platform.engine.ExecutionRequest;
import org.junit.platform.engine.support.hierarchical.EngineExecutionContext;
import cucumber.runtime.Runtime;

public class CucumberExecutionContext implements EngineExecutionContext {
    private final ExecutionRequest request;
    private final Runtime runtime;

    public CucumberExecutionContext(ExecutionRequest request, Runtime runtime) {
        this.request = request;
        this.runtime = runtime;
    }

    public Runtime runtime(){
        return runtime;
    }

    public ExecutionRequest executionRequest(){
        return request;
    }
}
