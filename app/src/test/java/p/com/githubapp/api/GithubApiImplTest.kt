package p.com.githubapp.api

import io.reactivex.observers.TestObserver
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.*
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import p.com.githubapp.domain.entity.User
import p.com.githubapp.domain.entity.SearchUserResult
import p.com.githubapp.extension.createService
import p.com.githubapp.extension.getResponseString

class GithubApiImplTest:Spek({
    lateinit var server:MockWebServer
    lateinit var api:GithubApiImpl
    val json = getResponseString("SearchUserResponse.json")

    beforeGroup {
        server = MockWebServer()
        server.start()
    }

    beforeEachGroup {
        val url = server.url("/")
        val service = createService(url, GithubApiService::class.java)
        api = GithubApiImpl(service)
    }

    afterGroup { server.shutdown() }

    Feature("search user"){
        lateinit var testObserver:TestObserver<SearchUserResult>
        Scenario("first page"){
            Given("first page header mock response"){
                val response = MockResponse()
                response.addHeader("link", "<https://api.github.com/search/users?q=j&page=2>; rel=\"next\", <https://api.github.com/search/users?q=j&page=34>; rel=\"last\"")
                response.setBody(json)
                server.enqueue(response)
            }
            When("search users"){
                testObserver = api.searchUsers("jefry", 1).test()
            }
            Then("total is 68"){
                testObserver.assertValue {
                    it.total == 68
                }
            }
            Then("next page is 2"){
                testObserver.assertValue {
                    it.nextPage == 2
                }
            }
            Then("previous page is 0"){
                testObserver.assertValue {
                    it.previousPage == 0
                }
            }
            Then("first users"){
                val id = 2088724
                val profilePicture = "https://avatars2.githubusercontent.com/u/2088724?v=4"
                val username = "jefry"
                val expectedUser = User(id, profilePicture, username)
                val result = testObserver.values()[0]
                assertThat(result.users[0], `is`(expectedUser))
            }
            Then("second users"){
                val id = 20434351
                val profilePicture = "https://avatars0.githubusercontent.com/u/20434351?v=4"
                val username = "jefrydco"
                val expectedUser = User(id, profilePicture, username)
                val result = testObserver.values()[0]
                assertThat(result.users[1], `is`(expectedUser))
            }
        }

        Scenario("last page"){
            lateinit var searchUserResult:SearchUserResult
            Given("first page header mock response"){
                val response = MockResponse()
                response.addHeader("link", "<https://api.github.com/search/users?q=j&page=33>; rel=\"prev\", <https://api.github.com/search/users?q=j&page=1>; rel=\"first\"")
                response.setBody(json)
                server.enqueue(response)
            }
            When("search users"){
                testObserver = api.searchUsers("jefry", 1).test()
            }
            Then("total is 68"){
                testObserver.assertValue {
                    it.total == 68
                }
            }
            Then("next page is 0"){
                testObserver.assertValue {
                    it.nextPage == 0
                }
            }
            Then("previous page is 33"){
                testObserver.assertValue {
                    it.previousPage == 33
                }
            }
            Then("first users"){
                val id = 2088724
                val profilePicture = "https://avatars2.githubusercontent.com/u/2088724?v=4"
                val username = "jefry"
                val expectedUser = User(id, profilePicture, username)
                val result = testObserver.values()[0]
                assertThat(result.users[0], `is`(expectedUser))
            }
            Then("second users"){
                val id = 20434351
                val profilePicture = "https://avatars0.githubusercontent.com/u/20434351?v=4"
                val username = "jefrydco"
                val expectedUser = User(id, profilePicture, username)
                val result = testObserver.values()[0]
                assertThat(result.users[1], `is`(expectedUser))
            }
        }
    }
})