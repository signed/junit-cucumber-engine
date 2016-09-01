package glue;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class Steps {

    @Before
    public void before(){
        System.out.println("execute before hook");
    }

    @After
    public void after(){
        System.out.println("execute after hook");
        throw new RuntimeException("blub");
    }

    @Given("^alpha$")
    public void alpha() throws Throwable {
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

    private void sleep(){
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
