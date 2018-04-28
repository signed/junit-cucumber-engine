# Report
## New
### How to discover engines with file based tests
For class based tests like in jupiter there is @Testable for the IDE to pick up what engines to run.
How can we enable IDEs the detect if a source root / package contain test specifications like feature files?

### How to discover engine specific parameters?
One of the advantages of the junit-platform / engine approach for tool developers is that they only need to implement a single integration for junit-platform.
All engines build on top of junit-platform will work for free afterwards.

But right now there is no concept of engine specific parameters that can be presented/overridden in an e.g. Launch or Run Configuration.
To pass engine specific parameters to a discover/execution custom integration is still needed.

How can integrators automatically discover what parameters an engine supports and present those in an user interface?