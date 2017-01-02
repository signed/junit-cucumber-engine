# Purpose
1. A project for me to tinker with junit and cucumber internals and get to learn the concepts and capabilities.
1. Provide a native junit engine to run cucumber features.

# How can I use the junit cucumber engine?
There is no public release yet.
Just clone the repository and install the engine into your local maven repository.

````
git clone https://github.com/signed/junit-cucumber-engine.git
cd junit-cucumber-engine
./gradlew clean install

````

## Use the engine with maven
````
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>...</groupId>
    <artifactId>...</artifactId>
    <version>...</version>

    <properties>
        <cucumber.version>1.2.5</cucumber.version>
        <junit.platform.version>1.0.0-M3</junit.platform.version>
        <surefire.plugin.version>2.19.1</surefire.plugin.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>info.cukes</groupId>
            <artifactId>cucumber-java</artifactId>
            <version>${cucumber.version}</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.junit.platform</groupId>
            <artifactId>junit-platform-runner</artifactId>
            <version>${junit.platform.version}</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>com.github.signed.junit-cucumber-engine</groupId>
            <artifactId>junit-cucumber-engine</artifactId>
            <version>0.0.1</version>
            <scope>test</scope>
        </dependency>
    </dependencies>
</project>
````

# Repository layout
- feature/

  Contains a sample feature with the corresponding step definitions.
  *vanilla* and *cucumber-engine* have a compile scope dependency to this project and share this feature for a quick test run for comparisons.
- cucumber-engine/

  Contains the implementation of the junit-cucumber-engine.
  To run the sample feature with the engine execute *EngineRunner*
- vanilla/

  Contains code to execute the sample feature with the existing cucumber-junit.
  Serves as a gold master for the new engine.
  To run the sample feature with cucumber-junit execute *CucumberRunner*

