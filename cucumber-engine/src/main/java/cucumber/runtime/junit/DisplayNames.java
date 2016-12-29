package cucumber.runtime.junit;

import cucumber.runtime.model.CucumberFeature;

class DisplayNames {

    static String displayNameFor(CucumberFeature cucumberFeature) {
        return cucumberFeature.getGherkinFeature().getKeyword() + ": " + cucumberFeature.getGherkinFeature().getName();
    }
}
