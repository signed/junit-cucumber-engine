package cucumber.runtime.junit;

import cucumber.runtime.model.CucumberFeature;
import gherkin.formatter.model.Step;

class DisplayNames {

    static String displayNameFor(CucumberFeature cucumberFeature) {
        return cucumberFeature.getGherkinFeature().getKeyword() + ": " + cucumberFeature.getGherkinFeature().getName();
    }

    static String displayNameFor(Step step) {
        return step.getKeyword() + step.getName();
    }
}
