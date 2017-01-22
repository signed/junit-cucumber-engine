package cucumber.runtime.junit;

import cucumber.runtime.model.CucumberFeature;
import org.junit.platform.engine.TestSource;
import org.junit.platform.engine.UniqueId;
import org.junit.platform.engine.support.descriptor.AbstractTestDescriptor;
import org.junit.platform.engine.support.hierarchical.Node;

import java.util.Optional;

import static cucumber.runtime.junit.DisplayNames.displayNameFor;

class FeatureDescriptor extends AbstractTestDescriptor implements Node<CucumberExecutionContext> {

    FeatureDescriptor(UniqueId featureFileId, CucumberFeature cucumberFeature, Optional<TestSource> maybeTestSource) {
        super(featureFileId, displayNameFor(cucumberFeature));
        maybeTestSource.ifPresent(this::setSource);
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
