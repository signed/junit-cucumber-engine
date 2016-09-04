package cucumber.runtime.junit;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.junit.platform.engine.TestExecutionResult;

import static org.hamcrest.CoreMatchers.any;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.platform.engine.TestExecutionResult.Status.FAILED;
import static org.junit.platform.engine.TestExecutionResult.Status.SUCCESSFUL;

class ExecutionRecordMatcher extends TypeSafeDiagnosingMatcher<ExecutionRecord> {

    static Matcher<ExecutionRecord> skipped() {
        return new ExecutionRecordMatcher().expectSkipped();
    }

    static Matcher<ExecutionRecord> failed() {
        return new ExecutionRecordMatcher().expectResult(FAILED);
    }

    static Matcher<ExecutionRecord> successful() {
        return new ExecutionRecordMatcher().expectResult(SUCCESSFUL);
    }

    private Matcher<Boolean> skippedMatcher = any(Boolean.class);
    private Matcher<TestExecutionResult.Status> statusMatcher = any(TestExecutionResult.Status.class);

    private ExecutionRecordMatcher expectSkipped() {
        skippedMatcher = equalTo(true);
        return this;
    }

    private Matcher<ExecutionRecord> expectResult(TestExecutionResult.Status expectedStatus) {
        statusMatcher = equalTo(expectedStatus);
        return this;
    }

    @Override
    protected boolean matchesSafely(ExecutionRecord item, Description mismatchDescription) {
        boolean skippedMatches = skippedMatcher.matches(item.wasSkipped());
        if (!skippedMatches) {
            mismatchDescription
                    .appendText("started: ").appendValue(item.wasStarted())
                    .appendText(" finished: ").appendValue(item.wasFinished())
                    .appendText(" result: ").appendValue(item.testExecutionResult);
        }
        TestExecutionResult testExecutionResult = item.testExecutionResult;
        boolean statusMatches = testExecutionResult == null || statusMatcher.matches(testExecutionResult.getStatus());
        if (!statusMatches) {
            if (item.wasSkipped()) {
                mismatchDescription.appendText("skipped");
            } else if (!item.wasStarted()) {
                mismatchDescription.appendText("not started");
            } else if (!item.wasFinished()) {
                mismatchDescription.appendText("not finished");
            } else {
                mismatchDescription.appendText("finished with ").appendValue(testExecutionResult.getStatus());
            }
        }
        return skippedMatches && statusMatches;
    }

    @Override
    public void describeTo(Description description) {
        description
                .appendText("skipped: ").appendDescriptionOf(skippedMatcher)
                .appendText(" status: ").appendDescriptionOf(statusMatcher);
    }
}
