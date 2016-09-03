package cucumber.runtime.junit;

import cucumber.runtime.Backend;
import cucumber.runtime.Glue;
import cucumber.runtime.UnreportedStepExecutor;
import cucumber.runtime.snippets.FunctionNameGenerator;
import gherkin.formatter.model.Step;

import java.util.List;

class BackendStub implements Backend {
    @Override
    public void loadGlue(Glue glue, List<String> gluePaths) {

    }

    @Override
    public void setUnreportedStepExecutor(UnreportedStepExecutor executor) {

    }

    @Override
    public void buildWorld() {

    }

    @Override
    public void disposeWorld() {

    }

    @Override
    public String getSnippet(Step step, FunctionNameGenerator functionNameGenerator) {
        return null;
    }
}
