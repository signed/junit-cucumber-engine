package cucumber.runtime.junit;

import static cucumber.runtime.junit.CucumberFeatureBuilder.featureAt;

class CucumberFeatureMother {

    static CucumberFeatureBuilder anyScenario() {
        return featureAt("feature/Path")
                .Feature("feature name")
                .Scenario("scenario name");
    }
}
