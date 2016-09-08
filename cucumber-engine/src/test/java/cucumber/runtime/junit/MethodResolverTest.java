package cucumber.runtime.junit;

import org.junit.Test;

import java.lang.reflect.Method;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class MethodResolverTest {

    @Test
    public void withoutArguments() throws Exception {
        assertThat(resolveMethodFrom("glue.Steps.duck() in file:/Users"), notNullValue());
    }

    @Test
    public void withArguments() throws Exception {
        assertThat(resolveMethodFrom("glue.Steps.concatenatedWithIs(int,int,String) in file:/Users"), notNullValue());
    }

    private Method resolveMethodFrom(String locationFromCucumberStep) {
        return new MethodResolver().resolve(locationFromCucumberStep);
    }
}