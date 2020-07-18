package p.com.githubapp.extension

import org.hamcrest.CoreMatchers.*
import org.junit.Assert.*
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import java.net.URI

class URIExtTest: Spek({
    lateinit var uri: URI
    lateinit var result:String

    Feature("queryParam"){
        Scenario("key found"){
            Given("url"){
                uri = URI.create("https://api.github.com/search/users?q=j&page=33")
            }
            When("when query param with page key"){
                result = uri.queryParam("page")
            }
            Then("value will be 33"){
                assertThat(result, `is`("33"))
            }
        }
        Scenario("key not found"){
            Given("url"){
                uri = URI.create("https://api.github.com/search/users?q=j&order=asc")
            }
            When("when query param with page key"){
                result = uri.queryParam("page")
            }
            Then("value will be empty"){
                assertThat(result, `is`(""))
            }
        }
        Scenario("url dont have query"){
            Given("url"){
                uri = URI.create("https://api.github.com/search/users")
            }
            When("when query param with page key"){
                result = uri.queryParam("page")
            }
            Then("value will be empty"){
                assertThat(result, `is`(""))
            }
        }
    }
})