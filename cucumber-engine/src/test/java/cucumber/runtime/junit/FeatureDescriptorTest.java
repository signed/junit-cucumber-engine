package cucumber.runtime.junit;

import org.junit.Test;

import static cucumber.runtime.junit.CucumberFeatureMother.anyFeatureFile;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class FeatureDescriptorTest {

    private final CucumberFeatureBuilder featureFile = anyFeatureFile();

    @Test
    public void takeTheFirstLineOfTheFeatureAndPrependFeature() throws Exception {
        FeatureDescriptor featureDescriptor = new FeatureDescriptor(UniqueIdMother.anyUniqueId(), featureFile.Feature("first line\nsecond line").build());
        assertThat(featureDescriptor.getDisplayName(), equalTo("Feature: first line"));
    }

}