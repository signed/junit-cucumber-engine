package cucumber.runtime.junit;

import org.junit.platform.engine.EngineExecutionListener;
import org.junit.platform.engine.TestDescriptor;
import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.engine.reporting.ReportEntry;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.joining;

class CapturingEngineExecutionListener implements EngineExecutionListener {

    private final LinkedHashMap<TestDescriptor, ExecutionRecord> recordedExecution = new LinkedHashMap<>();

    @Override
    public void dynamicTestRegistered(TestDescriptor testDescriptor) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void executionSkipped(TestDescriptor testDescriptor, String reason) {
        ensureThereIsNoExecutionRecordFor(testDescriptor);
        recordedExecution.put(testDescriptor, new ExecutionRecord(testDescriptor).skipped(reason));
    }

    @Override
    public void executionStarted(TestDescriptor testDescriptor) {
        ensureThereIsNoExecutionRecordFor(testDescriptor);
        recordedExecution.put(testDescriptor, new ExecutionRecord(testDescriptor).started());
    }

    @Override
    public void executionFinished(TestDescriptor testDescriptor, TestExecutionResult testExecutionResult) {
        ensureWasStarted(testDescriptor);
        recordedExecution.get(testDescriptor).finishedWith(testExecutionResult);
    }

    @Override
    public void reportingEntryPublished(TestDescriptor testDescriptor, ReportEntry entry) {
        throw new UnsupportedOperationException();
    }

    private void ensureWasStarted(TestDescriptor testDescriptor) {
        if (!recordedExecution.get(testDescriptor).wasStarted()) {
            throw new IllegalStateException("was not started before " + testDescriptor);
        }
    }

    private void ensureThereIsNoExecutionRecordFor(TestDescriptor testDescriptor) {
        if (recordedExecution.containsKey(testDescriptor)) {
            throw new IllegalStateException("already recorded something for test descriptor " + testDescriptor);
        }
    }

    void ensureAllInProperEndState() {
        List<TestDescriptor> notInProperEndState = recordedExecution.values().stream().filter((executionRecord) -> !executionRecord.isInProperEndState()).map(executionRecord -> executionRecord.testDescriptor).collect(Collectors.toList());
        if (!notInProperEndState.isEmpty()) {
            String message = notInProperEndState.stream().map(Object::toString).collect(joining("\n"));
            throw new RuntimeException(message);
        }
    }

    ExecutionRecord executionRecordFor(String theString) {
        List<ExecutionRecord> collect = recordedExecution.values().stream().filter(recorded -> theString.equals(recorded.testDescriptor.getDisplayName())).collect(Collectors.toList());
        if (collect.size() > 1) {
            throw new IllegalStateException("there should be exactly one");
        }
        if (collect.isEmpty()) {
            throw new IllegalStateException("there is no execution record for '" + theString + "'");
        }
        return collect.get(0);
    }
}
