package cucumber.runtime.junit;

import org.junit.platform.engine.TestDescriptor;
import org.junit.platform.engine.TestExecutionResult;

class ExecutionRecord {

    final TestDescriptor testDescriptor;
    private boolean skipped = false;
    private boolean started = false;
    private boolean finished = false;
    private String skipReason;
    TestExecutionResult testExecutionResult;

    ExecutionRecord(TestDescriptor testDescriptor) {
        this.testDescriptor = testDescriptor;
    }

    ExecutionRecord skipped(String skipReason) {
        this.skipped = true;
        this.skipReason = skipReason;
        return this;
    }

    ExecutionRecord started() {
        this.started = true;
        return this;
    }

    void finishedWith(TestExecutionResult testExecutionResult) {
        ensureNotSkipped();
        this.finished = true;
        this.testExecutionResult = testExecutionResult;
    }

    boolean isInProperEndState() {
        return skippedWithReason() || startedAndFinishedWithResult();
    }

    boolean wasSkipped(){
        return skipped;
    }

    boolean wasStarted() {
        return started;
    }

    boolean wasFinished() {
        return finished;
    }

    private boolean startedAndFinishedWithResult() {
        return started && finished && null != testExecutionResult;
    }

    private boolean skippedWithReason() {
        return skipped && null != skipReason;
    }

    private void ensureNotSkipped() {
        if (skipped) {
            throw new IllegalStateException(testDescriptor + " was skipped, how can it be finished?");
        }
    }
}
