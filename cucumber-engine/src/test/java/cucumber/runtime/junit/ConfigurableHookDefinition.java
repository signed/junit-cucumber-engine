package cucumber.runtime.junit;

import cucumber.api.Scenario;
import cucumber.runtime.HookDefinition;
import gherkin.formatter.model.Tag;

import java.util.Collection;

class ConfigurableHookDefinition implements HookDefinition {
    private final Runnable runnable;

    ConfigurableHookDefinition(Runnable runnable) {
        this.runnable = runnable;
    }

    @Override
    public String getLocation(boolean detail) {
        return "stand in location for before hook";
    }

    @Override
    public void execute(Scenario scenario) throws Throwable {
        runnable.run();
    }

    @Override
    public boolean matches(Collection<Tag> tags) {
        return true;
    }

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public boolean isScenarioScoped() {
        return false;
    }
}
