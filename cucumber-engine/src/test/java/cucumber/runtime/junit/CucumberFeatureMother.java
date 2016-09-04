package cucumber.runtime.junit;

import static cucumber.runtime.junit.CucumberFeatureBuilder.featureAt;

class CucumberFeatureMother {

    static CucumberFeatureBuilder anyScenario() {
        return anyFeatureFile().Scenario("scenario name");
    }

    static CucumberFeatureBuilder anyScenarioOutline(){
        return anyFeatureFile().ScenarioOutline("scenario outline name");
    }

    static CucumberFeatureBuilder anyFeatureFile() {
        return featureAt("feature/Path")
                .Feature("feature name");
    }
}
