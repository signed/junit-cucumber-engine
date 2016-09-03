package cucumber.runtime.junit;

import org.junit.platform.engine.ConfigurationParameters;

import java.util.Optional;

class NoConfigurationParameters implements ConfigurationParameters {
    @Override
    public Optional<String> get(String key) {
        return Optional.empty();
    }

    @Override
    public int size() {
        return 0;
    }
}
