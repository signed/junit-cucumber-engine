package cucumber.runtime.junit;

import org.junit.Test;
import org.junit.platform.engine.UniqueId;

import static org.hamcrest.MatcherAssert.assertThat;

public class ScenarioDescriptorTest {

    @Test
    public void hierarchicalEngineShouldNotRunTheChildStepDescriptors() throws Exception {
        ScenarioDescriptor scenarioDescriptor = new ScenarioDescriptor(UniqueId.forEngine("stand-in"), "any display name", null);
        assertThat("hierarchical engine should not run the children", scenarioDescriptor.isLeaf());
    }
}