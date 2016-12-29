package cucumber.runtime.junit;

import cucumber.runtime.Backend;
import cucumber.runtime.ClassFinder;
import cucumber.runtime.Reflections;
import cucumber.runtime.Runtime;
import cucumber.runtime.RuntimeOptions;
import cucumber.runtime.RuntimeOptionsFactory;
import cucumber.runtime.StopWatch;
import cucumber.runtime.io.MultiLoader;
import cucumber.runtime.io.ResourceLoader;
import cucumber.runtime.io.ResourceLoaderClassFinder;
import cucumber.runtime.model.CucumberFeature;
import org.junit.platform.engine.EngineDiscoveryRequest;
import org.junit.platform.engine.ExecutionRequest;
import org.junit.platform.engine.TestDescriptor;
import org.junit.platform.engine.UniqueId;
import org.junit.platform.engine.discovery.ClassSelector;
import org.junit.platform.engine.support.hierarchical.HierarchicalTestEngine;

import java.util.Collection;
import java.util.List;

public class CucumberEngine extends HierarchicalTestEngine<CucumberExecutionContext> {

    static final String ENGINE_ID = "cucumber-jvm";

    private static Collection<? extends Backend> loadBackends(ResourceLoader resourceLoader, ClassFinder classFinder) {
        Reflections reflections = new Reflections(classFinder);
        return reflections.instantiateSubclasses(Backend.class, "cucumber.runtime", new Class[]{ResourceLoader.class}, new Object[]{resourceLoader});
    }

    @Override
    public String getId() {
        return ENGINE_ID;
    }

    @Override
    public TestDescriptor discover(EngineDiscoveryRequest discoveryRequest, UniqueId uniqueId) {
        ClassSelector selector = discoveryRequest.getSelectorsByType(ClassSelector.class).get(0);
        Class<?> clazz = selector.getJavaClass();

        RuntimeOptionsFactory runtimeOptionsFactory = new RuntimeOptionsFactory(clazz);
        RuntimeOptions runtimeOptions = runtimeOptionsFactory.create();
        runtimeOptions.getFeaturePaths().add("classpath:features");
        runtimeOptions.getGlue().add("glue");

        ClassLoader classLoader = clazz.getClassLoader();
        ResourceLoader resourceLoader = new MultiLoader(classLoader);
        final List<CucumberFeature> cucumberFeatures = runtimeOptions.cucumberFeatures(resourceLoader);

        ClassFinder classFinder = new ResourceLoaderClassFinder(resourceLoader, classLoader);
        Collection<? extends Backend> backends = loadBackends(resourceLoader, classFinder);

        Runtime runtime = new Runtime(resourceLoader, classLoader, backends, runtimeOptions, StopWatch.SYSTEM, null);
        MethodResolver methodResolver = new MethodResolver();
        return new TestDescriptorCreator(uniqueId, runtimeOptions, runtime, methodResolver).createEngineDescriptorFor(cucumberFeatures);
    }

    @Override
    protected CucumberExecutionContext createExecutionContext(ExecutionRequest request) {
        CucumberEngineDescriptor rootTestDescriptor = (CucumberEngineDescriptor) request.getRootTestDescriptor();
        return new CucumberExecutionContext(request, rootTestDescriptor.runtime(), rootTestDescriptor.runtimeOptions());
    }

}
