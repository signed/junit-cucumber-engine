package cucumber.runtime.junit;

import static cucumber.runtime.junit.CucumberFeatureBuilder.featureAt;

class CucumberFeatureMother {

    static CucumberFeatureBuilder anyScenario() {
        return any().Scenario("scenario name");
    }

    static CucumberFeatureBuilder anyScenarioOutline(){
        return any().ScenarioOutline("scenario outline name");
    }

    private static CucumberFeatureBuilder any() {
        return featureAt("feature/Path")
                .Feature("feature name");
    }
}
