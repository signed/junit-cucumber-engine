package cucumber.runtime.junit;

import gherkin.formatter.model.Step;
import org.junit.Test;

import java.util.Collections;

import static cucumber.runtime.junit.DisplayNames.displayNameFor;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;

public class DisplayNamesTest {

    @Test
    public void aStepsDisplayNameIsTheKeyWordFollowedByTheStepsName() throws Exception {
        Step step = new Step(Collections.emptyList(), "Given ", "the steps name", 42, Collections.emptyList(), null);
        assertThat(displayNameFor(step), equalTo("Given the steps name"));
    }
}