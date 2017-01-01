package cucumber.runtime.junit;

import cucumber.api.CucumberOptions;
import org.junit.platform.runner.IncludeEngines;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

/**
 * Test suite for the JUnit Platform.
 *
 * <h3>Logging Configuration</h3>
 *
 * <p>In order for our log4j2 configuration to be used in an IDE, you must
 * set the following system property before running any tests &mdash; for
 * example, in <em>Run Configurations</em> in Eclipse.
 *
 * <pre style="code">
 * -Djava.util.logging.manager=org.apache.logging.log4j.jul.LogManager
 * </pre>
 *
 * @since 1.0
 */
@RunWith(JUnitPlatform.class)
@IncludeEngines("cucumber-jvm")
@CucumberOptions(strict = true, glue = {"glue"}, features = {"classpath:features"})
public class EngineRunner {
}
