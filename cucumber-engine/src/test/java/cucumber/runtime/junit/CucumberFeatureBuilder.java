package cucumber.runtime.junit;

import cucumber.runtime.FeatureBuilder;
import cucumber.runtime.io.Resource;
import cucumber.runtime.model.CucumberFeature;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

class CucumberFeatureBuilder {

    static CucumberFeatureBuilder featureAt(String path) {
        return new CucumberFeatureBuilder().path(path);
    }

    private String path;
    private String featureDescription;
    private String scenarioDescription;
    private List<String> steps = new ArrayList<>();

    CucumberFeatureBuilder path(String path) {
        this.path = path;
        return this;
    }

    CucumberFeatureBuilder Feature(String description) {
        this.featureDescription = description;
        return this;
    }

    CucumberFeatureBuilder Scenario(String description) {
        this.scenarioDescription = description;
        return this;
    }

    CucumberFeatureBuilder Given(String text) {
        return Step("Given", text);
    }

    CucumberFeatureBuilder When(String text) {
        return Step("When", text);
    }

    CucumberFeatureBuilder Then(String text) {
        return Step("Then", text);
    }

    CucumberFeatureBuilder Step(String keyword, String text) {
        steps.add(keyword + " " + text);
        return this;
    }

    CucumberFeature build() {
        List<String> allLines = new ArrayList<>();
        allLines.add("Feature: " + featureDescription);
        allLines.add("  Scenario: " + scenarioDescription);
        allLines.addAll(steps.stream().map(step -> "    " + step).collect(toList()));
        String featureText = allLines.stream().collect(Collectors.joining("\n"));
        return feature(path, featureText);
    }

    private CucumberFeature feature(final String path, final String source) {
        ArrayList<CucumberFeature> cucumberFeatures = new ArrayList<>();
        FeatureBuilder featureBuilder = new FeatureBuilder(cucumberFeatures);
        featureBuilder.parse(new Resource() {
            @Override
            public String getPath() {
                return path;
            }

            @Override
            public String getAbsolutePath() {
                throw new UnsupportedOperationException();
            }

            @Override
            public InputStream getInputStream() {
                try {
                    return new ByteArrayInputStream(source.getBytes("UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public String getClassName(String extension) {
                throw new UnsupportedOperationException();
            }
        }, new ArrayList<>());
        featureBuilder.close();
        return cucumberFeatures.get(0);
    }

}
