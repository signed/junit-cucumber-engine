package cucumber.runtime.junit;

import cucumber.runtime.model.CucumberFeature;
import org.junit.platform.engine.UniqueId;
import org.junit.platform.engine.support.descriptor.AbstractTestDescriptor;
import org.junit.platform.engine.support.hierarchical.Node;

import static cucumber.runtime.junit.DisplayNames.displayNameFor;

class FeatureDescriptor extends AbstractTestDescriptor implements Node<CucumberExecutionContext> {

    FeatureDescriptor(UniqueId featureFileId, CucumberFeature cucumberFeature) {
        super(featureFileId, displayNameFor(cucumberFeature));
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
