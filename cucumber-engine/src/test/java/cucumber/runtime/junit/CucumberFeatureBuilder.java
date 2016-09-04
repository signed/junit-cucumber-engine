package cucumber.runtime.junit;

import cucumber.runtime.FeatureBuilder;
import cucumber.runtime.io.Resource;
import cucumber.runtime.model.CucumberFeature;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

class CucumberFeatureBuilder {

    private static final String IndentationExamples = "      ";

    static CucumberFeatureBuilder featureAt(String path) {
        return new CucumberFeatureBuilder().path(path);
    }

    private String path;
    private String featureDescription;
    private String scenarioDescription;
    private String scenarioOutlineDescription;
    private List<String> steps = new ArrayList<>();
    private List<String[]> arguments = new ArrayList<>();

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

    CucumberFeatureBuilder ScenarioOutline(String scenarioOutlineDescription) {
        this.scenarioOutlineDescription = scenarioOutlineDescription;
        return this;
    }

    CucumberFeatureBuilder AStep(String text) {
        return When(text);
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

    CucumberFeatureBuilder Example(String... exampleSet) {
        this.arguments.add(exampleSet);
        return this;
    }

    CucumberFeature build() {
        return feature(path, buildFeatureText());
    }

    String buildFeatureText() {
        List<String> allLines = new ArrayList<>();
        allLines.add("Feature: " + featureDescription);
        if (null != scenarioDescription) {
            allLines.add("  Scenario: " + scenarioDescription);
            allLines.addAll(stepLines());
        }
        if (null != scenarioOutlineDescription) {
            allLines.add("  Scenario Outline: " + scenarioOutlineDescription);
            allLines.addAll(stepLines());
            if (!arguments.isEmpty()) {
                allLines.add("    Examples:");
                Collector<CharSequence, ?, String> exampleFormat = Collectors.joining("|", "|", "|");
                allLines.add(IndentationExamples + parameterNames().stream().collect(exampleFormat));
                allLines.addAll(arguments.stream().map(argumentSet -> IndentationExamples + stream(argumentSet).collect(exampleFormat)).collect(toList()));
            }
        }
        return allLines.stream().collect(Collectors.joining("\n"));
    }

    private List<String> parameterNames() {
        Pattern pattern = Pattern.compile("<([^>]+)>");
        List<String> argumentNames = new ArrayList<>();
        Matcher matcher = pattern.matcher(allStepsAsSingleLine());
        while (matcher.find()) {
            argumentNames.add(matcher.group(1));
        }
        return argumentNames;
    }

    private String allStepsAsSingleLine() {
        return stepLines().stream().collect(Collectors.joining());
    }

    private List<String> stepLines() {
        return steps.stream().map(step -> "    " + step).collect(toList());
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
