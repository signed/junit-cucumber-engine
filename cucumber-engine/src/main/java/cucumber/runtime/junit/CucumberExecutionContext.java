package cucumber.runtime.junit;

import cucumber.runtime.RuntimeOptions;
import org.junit.platform.engine.ExecutionRequest;
import org.junit.platform.engine.support.hierarchical.EngineExecutionContext;
import cucumber.runtime.Runtime;

public class CucumberExecutionContext implements EngineExecutionContext {
    private final ExecutionRequest request;
    private final Runtime runtime;
    private final RuntimeOptions runtimeOptions;

    public CucumberExecutionContext(ExecutionRequest request, Runtime runtime, RuntimeOptions runtimeOptions) {
        this.request = request;
        this.runtime = runtime;
        this.runtimeOptions = runtimeOptions;
    }

    public Runtime runtime(){
        return runtime;
    }

    public ExecutionRequest executionRequest(){
        return request;
    }

    public boolean isStrict(){
        return runtimeOptions.isStrict();
    }
}
