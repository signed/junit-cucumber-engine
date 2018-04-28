# Junit Platform - concepts
* why is discover run twice? Sample code base
  Thats because of how the JunitPlatform.class is implemented at the moment.
  As this is a stopgap anyway until there is native junit-platform support just ignore this.  
* Is there a need for a cucumber engine at all? What to gain from a cucumber engine?
- IDEA uses cucumber CLI to run scenarios
* What platform features can be reuse in cucumber-engine?
  Look through TestDescriptor.class and see whats there.
* what is the @Step annotation?
  Comparable to what cucumber provides, but simpler.
  TestNG mode for Jupiter



* container vs. test in junit platform?

## Cucumber engine decisions
* where to actually try to check if steps are implemented? discover or run?
* How does a cucumber scenario map to a test?  
  Does a junit test have steps as well? Before After?
* What is a test in the context of cucumber features? Feature or a step?
* Only test descriptor for Scenario is started by the platform, steps are just synced with feature execution.
 
# Integration into surefire
* Discovery in Surefire is not supported because discovery only works for classes

# Report format
* Multiple report file formats?
* New Report file format? Keep file based test tools in mind
* How will the extension look like for the report format
* really statically typed implement all reporting formats?

# Junit Platform - execution and
* How to properly run a custom engine on junit-platform?
* How can I get rid of junit 4.12 on the class path?
* How to execute cucumber without a junit 4.12 dependency? Replacement for Runner?

# IDE integration
* How to run in intellij without a runner?
* How can the IDE figure out what engines are available to e.g. pass engine specific parameters to the discovery request?
* How should intellij figure out that junit5 should be used? Right now they check for the presence of the test annotation ('org.junit.jupiter.api.Test'')
* Activate junit5 runner in IDEA without having jupiter api on the class path?


## Parameters
* What platform parameters are available?
* How can an engine communicate what parameters are supported?
* How to pass engine specific parameters to a discovery request/execution?
* How to use configuration parameters over defaults