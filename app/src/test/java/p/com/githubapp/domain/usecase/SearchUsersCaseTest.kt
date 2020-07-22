package p.com.githubapp.domain.usecase

import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.mock
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import p.com.githubapp.domain.entity.SearchUserResult
import p.com.githubapp.domain.entity.User
import p.com.githubapp.repository.GithubRepository
import kotlin.random.Random

class SearchUsersCaseTest: Spek({
    lateinit var githubRepository:GithubRepository
    lateinit var usecase:SearchUsersUseCase

    beforeEachGroup {
        githubRepository = mock()
        usecase = SearchUsersUseCase(githubRepository)
    }

    Feature("search"){
        lateinit var query:String
        var page = 0
        lateinit var testObserver:TestObserver<SearchUserResult>
        lateinit var result:SearchUserResult
        Scenario("search query isn't blank then success return user list"){
            Given("query is jef"){
                query = "jef"
            }
            Given("page is not zero"){
                page = Random.nextInt(1, Int.MAX_VALUE)
            }
            Given("search user result"){
                val user = User(0,
                    "https://avatars0.githubusercontent.com/u/20434351?v=4",
                "test")
                result = SearchUserResult(64, false, listOf(user))
                given(githubRepository.search(query, page)).willReturn(Single.just(result.copy()))
            }
            When("search users"){
                testObserver = usecase.search(query, page).test()
            }
            Then("usecase return search result"){
                testObserver.assertValue(result)
            }
        }

        Scenario("search query is blank then return nothing"){
            Given("query is jef"){
                query = ""
            }
            Given("page is 1"){
                page = Random.nextInt(1, Int.MAX_VALUE)
            }
            Given("search user result"){
                val user = User(0,
                    "https://avatars0.githubusercontent.com/u/20434351?v=4",
                    "test")
                result = SearchUserResult(64, false, listOf(user))
                given(githubRepository.search(query, page)).willReturn(Single.just(result.copy()))
            }
            When("search users"){
                testObserver = usecase.search(query, page).test()
            }
            Then("usecase return search result"){
                testObserver.assertNoValues()
            }
            Then("no error"){
                testObserver.assertNoErrors()
            }
        }

        Scenario("search page is 0 then return nothing"){
            Given("query is jef"){
                query = "Jef"
            }
            Given("page is 0"){
                page = 0
            }
            Given("search user result"){
                val user = User(0,
                    "https://avatars0.githubusercontent.com/u/20434351?v=4",
                    "test")
                result = SearchUserResult(64, false, listOf(user))
                given(githubRepository.search(query, page)).willReturn(Single.just(result.copy()))
            }
            When("search users"){
                testObserver = usecase.search(query, page).test()
            }
            Then("usecase return search result"){
                testObserver.assertNoValues()
            }
            Then("no error"){
                testObserver.assertNoErrors()
            }
        }

        Scenario("There is no matching account"){
            Given("query is jef"){
                query = "Jef"
            }
            Given("page 1..max"){
                page = Random.nextInt(1, Int.MAX_VALUE)
            }
            Given("search user result total is zero and empty list"){
                var total = 0
                lateinit var list:List<User>
                result = SearchUserResult(total, false, list)
                given(githubRepository.search(query, page)).willReturn(Single.just(result.copy()))
            }
            When("search users"){
                testObserver = usecase.search(query, page).test()
            }
            Then("usecase return no matching account error"){
                testObserver.assertError {
                    it.message == SearchUsersUseCase.ERROR_MESSAGE_NO_MATCHING_ACCOUNT
                }
            }
        }

        Scenario("end of page"){
            Given("query is jef"){
                query = "Jef"
            }
            Given("page is 2..max"){
                page = Random.nextInt(2, Int.MAX_VALUE)
            }
            Given("search user result total is not zero and list is empty"){
                val total = Random.nextInt(1, Int.MAX_VALUE)
                val list = listOf<User>()
                result = SearchUserResult(total, false, list)
                given(githubRepository.search(query, page)).willReturn(Single.just(result.copy()))
            }
            When("search users"){
                testObserver = usecase.search(query, page).test()
            }
            Then("usecase return error"){
                testObserver.assertError {
                    it.message == SearchUsersUseCase.ERROR_MESSAGE_END_OF_PAGE
                }
            }
        }
    }
})