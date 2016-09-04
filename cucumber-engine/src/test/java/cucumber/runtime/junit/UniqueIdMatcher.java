package cucumber.runtime.junit;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.junit.platform.engine.UniqueId;

import java.util.List;

class UniqueIdMatcher extends TypeSafeDiagnosingMatcher<UniqueId> {
    static UniqueIdMatcher endsWith(String segment, String value) {
        return new UniqueIdMatcher(segment, value);
    }

    private final String segment;
    private final String value;

    private UniqueIdMatcher(String segment, String value) {
        this.segment = segment;
        this.value = value;
    }

    @Override
    protected boolean matchesSafely(UniqueId item, Description mismatchDescription) {
        List<UniqueId.Segment> segments = item.getSegments();
        UniqueId.Segment lastSegment = segments.get(segments.size() - 1);

        boolean typeMatches = segment.equals(lastSegment.getType());
        if (!typeMatches) {
            mismatchDescription.appendValue(lastSegment);
        }
        boolean valueMatches = value.equals(lastSegment.getValue());
        if (!valueMatches) {
            mismatchDescription.appendValue(lastSegment);
        }
        return typeMatches && valueMatches;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("last segment type ").appendValue(segment).appendText(" value ").appendValue(value);
    }
}
