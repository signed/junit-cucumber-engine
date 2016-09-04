package cucumber.runtime.junit;

import cucumber.runtime.model.CucumberFeature;
import org.junit.platform.engine.UniqueId;
import org.junit.platform.engine.support.descriptor.AbstractTestDescriptor;
import org.junit.platform.engine.support.hierarchical.Node;

class FeatureDescriptor extends AbstractTestDescriptor implements Node<CucumberExecutionContext> {

    private final CucumberFeature cucumberFeature;

    public FeatureDescriptor(UniqueId featureFileId, CucumberFeature cucumberFeature) {
        super(featureFileId, cucumberFeature.getGherkinFeature().getName());
        this.cucumberFeature = cucumberFeature;
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
