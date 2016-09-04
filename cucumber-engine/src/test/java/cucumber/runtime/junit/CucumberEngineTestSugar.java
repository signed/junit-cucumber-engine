package cucumber.runtime.junit;

interface CucumberEngineTestSugar {

    default void stepImplementationFor(String stepText, Runnable stepImplementation) {
        fixture().stepImplementationFor(stepText, stepImplementation);
    }

    default void stepImplementationFor(String stepText) {
        fixture().stepImplementationFor(stepText);
    }

    default void beforeHookImplementation(Runnable hookImplementation) {
        fixture().beforeHookImplementation(hookImplementation);
    }

    default CucumberEngineDescriptor discoveredDescriptorsFor(CucumberFeatureBuilder feature){
        return fixture().discoveredDescriptorsFor(feature);
    }

    default void run(CucumberFeatureBuilder feature) {
        fixture().run(feature);
    }

    default ExecutionRecord executionRecordFor(String stepText){
        return fixture().executionRecordFor(stepText);
    }

    CucumberEngineFixture fixture();
}
