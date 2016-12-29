package glue;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import static java.lang.System.currentTimeMillis;

public class Steps {

    @Before
    public void before(){
        dumpMessage("execute before hook");
        sleep();
    }

    @After
    public void after(){
        dumpMessage("execute after hook");
        sleep();
    }

    @Given("^alpha$")
    public void alpha() throws Throwable {
        sleep();
    }

    @Given("^A$")
    public void a() throws Throwable {
        sleep();
    }

    @When("^B$")
    public void b() throws Throwable {
        sleep();
    }

    @Then("^C$")
    public void c() throws Throwable {
        sleep();
    }

    @Given("^person$")
    public void person() throws Throwable {
        sleep();
    }

    @When("^jump$")
    public void jump() throws Throwable {
        sleep();
    }

    @Then("^up$")
    public void up() throws Throwable {
        sleep();
    }

    @Given("^rabbit$")
    public void rabbit() throws Throwable {
        sleep();
    }

    @When("^duck$")
    public void duck() throws Throwable {
        sleep();
    }

    @Then("^hidden$")
    public void hidden() throws Throwable {
        sleep();
    }

    @Given("^(\\d+) concatenated with (\\d+) is (.+)$")
    public void concatenatedWithIs(int first, int second, String result) throws Throwable {
        sleep();
    }

    private void dumpMessage(String message) {
        System.out.println(String.format("[%d] %s", currentTimeMillis(), message));
        System.out.flush();
    }

    private void sleep(){
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
