package org.example;

import cucumber.runtime.ClassFinder;
import cucumber.runtime.RuntimeOptions;
import cucumber.runtime.RuntimeOptionsFactory;
import cucumber.runtime.io.MultiLoader;
import cucumber.runtime.io.ResourceLoader;
import cucumber.runtime.io.ResourceLoaderClassFinder;
import cucumber.runtime.model.CucumberFeature;
import cucumber.runtime.Runtime;
import gherkin.formatter.Formatter;
import gherkin.formatter.Reporter;
import gherkin.formatter.model.Background;
import gherkin.formatter.model.Examples;
import gherkin.formatter.model.Feature;
import gherkin.formatter.model.Match;
import gherkin.formatter.model.Result;
import gherkin.formatter.model.Scenario;
import gherkin.formatter.model.ScenarioOutline;
import gherkin.formatter.model.Step;

import java.util.List;

public class HandCrafted {
    public static void main(String[] args) {
        new HandCrafted().run();
    }

    private void run() {
        Class<?> clazz = HandCrafted.class;
        RuntimeOptionsFactory runtimeOptionsFactory = new RuntimeOptionsFactory(clazz);
        RuntimeOptions runtimeOptions = runtimeOptionsFactory.create();
        runtimeOptions.getFeaturePaths().add("classpath:features");
        runtimeOptions.getGlue().add("glue");

        ClassLoader classLoader = clazz.getClassLoader();
        ResourceLoader resourceLoader = new MultiLoader(classLoader);
        final List<CucumberFeature> cucumberFeatures = runtimeOptions.cucumberFeatures(resourceLoader);

        ClassFinder classFinder = new ResourceLoaderClassFinder(resourceLoader, classLoader);
        Runtime runtime = new Runtime(resourceLoader, classFinder, classLoader, runtimeOptions);
        Formatter formatter = runtimeOptions.formatter(classLoader);
        Reporter reporter = runtimeOptions.reporter(classLoader);
        cucumberFeatures.forEach(cucumberFeature -> cucumberFeature.run(formatter(formatter), reporter(reporter), runtime));
    }

    private Formatter formatter(Formatter formatter) {
        return new Formatter() {
            @Override
            public void syntaxError(String state, String event, List<String> legalEvents, String uri, Integer line) {
                System.out.println("HandCrafted.syntaxError");
            }

            @Override
            public void uri(String uri) {
                System.out.println("file: " + uri);
            }

            @Override
            public void feature(Feature feature) {
                System.out.println("HandCrafted.feature");
            }

            @Override
            public void scenarioOutline(ScenarioOutline scenarioOutline) {
                System.out.println("HandCrafted.scenarioOutline");
            }

            @Override
            public void examples(Examples examples) {
                System.out.println("HandCrafted.examples");
            }

            @Override
            public void startOfScenarioLifeCycle(Scenario scenario) {
                System.out.println("HandCrafted.startOfScenarioLifeCycle");
            }

            @Override
            public void background(Background background) {
                System.out.println("HandCrafted.background");
            }

            @Override
            public void scenario(Scenario scenario) {
                System.out.println("HandCrafted.scenario");
            }

            @Override
            public void step(Step step) {
                System.out.println("HandCrafted.step");
            }

            @Override
            public void endOfScenarioLifeCycle(Scenario scenario) {
                System.out.println("HandCrafted.endOfScenarioLifeCycle");
            }

            @Override
            public void done() {
                System.out.println("HandCrafted.done");
            }

            @Override
            public void close() {
                System.out.println("HandCrafted.close");
            }

            @Override
            public void eof() {
                System.out.println("HandCrafted.eof");
            }
        };
    }

    private Reporter reporter(Reporter reporter) {
        return new Reporter() {
            @Override
            public void before(Match match, Result result) {
                System.out.println("HandCrafted.before");
            }

            @Override
            public void result(Result result) {
                System.out.println("HandCrafted.result");
            }

            @Override
            public void after(Match match, Result result) {
                System.out.println("HandCrafted.after");
            }

            @Override
            public void match(Match match) {
                System.out.println("HandCrafted.match");
            }

            @Override
            public void embedding(String mimeType, byte[] data) {
                System.out.println("HandCrafted.embedding");
            }

            @Override
            public void write(String text) {
                System.out.println("HandCrafted.write");
            }
        };
    }
}
