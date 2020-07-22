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
    lateinit var query:String
    var page = 0
    var totalResult = 0
    var sizePerPage = 0
    var incompleteResult = false
    lateinit var list: List<User>


    beforeEachGroup {
        githubRepository = mock()
        usecase = SearchUsersUseCase(githubRepository)

        query = "jef"
        page = Random.nextInt(1, Int.MAX_VALUE)
        sizePerPage = Random.nextInt(1, 100)
        totalResult = Random.nextInt(1,100000)
        incompleteResult = Random.nextBoolean()

        val user = User(0,
            "https://avatars0.githubusercontent.com/u/20434351?v=4",
            "test")
        list = listOf(user)
    }

    Feature("search"){
        lateinit var testObserver:TestObserver<SearchUserResult>
        lateinit var result:SearchUserResult
        Scenario("search query isn't blank then success return user list"){
            Given("search user result"){
                result = SearchUserResult(totalResult, incompleteResult, list)
                given(githubRepository.search(query, page, sizePerPage)).willReturn(Single.just(result.copy()))
            }
            When("search users"){
                testObserver = usecase.search(query, page, sizePerPage).test()
            }
            Then("usecase return search result"){
                testObserver.assertValue(result)
            }
        }

        Scenario("search query is blank then return nothing"){
            Given("query is blank"){
                query = ""
            }
            Given("search user result"){
                result = SearchUserResult(totalResult, incompleteResult, list)
                given(githubRepository.search(query, page, sizePerPage)).willReturn(Single.just(result.copy()))
            }
            When("search users"){
                testObserver = usecase.search(query, page, sizePerPage).test()
            }
            Then("usecase return search result"){
                testObserver.assertNoValues()
            }
            Then("no error"){
                testObserver.assertNoErrors()
            }
        }

        Scenario("search page is 0 then return nothing"){
            Given("page is 0"){
                page = 0
            }
            Given("search user result"){
                val user = User(0,
                    "https://avatars0.githubusercontent.com/u/20434351?v=4",
                    "test")
                result = SearchUserResult(totalResult, incompleteResult, list)
                given(githubRepository.search(query, page, sizePerPage)).willReturn(Single.just(result.copy()))
            }
            When("search users"){
                testObserver = usecase.search(query, page, sizePerPage).test()
            }
            Then("usecase return search result"){
                testObserver.assertNoValues()
            }
            Then("no error"){
                testObserver.assertNoErrors()
            }
        }

        Scenario("There is no matching account"){
            Given("total result is zero"){
                totalResult = 0
            }
            Given("search user result total is zero and empty list"){
                result = SearchUserResult(totalResult, incompleteResult, list)
                given(githubRepository.search(query, page, sizePerPage)).willReturn(Single.just(result.copy()))
            }
            When("search users"){
                testObserver = usecase.search(query, page, sizePerPage).test()
            }
            Then("usecase return no matching account error"){
                testObserver.assertError {
                    it.message == SearchUsersUseCase.ERROR_MESSAGE_NO_MATCHING_ACCOUNT
                }
            }
        }

        Scenario("end of page"){
            Given("search user result list is empty"){
                val list = listOf<User>()
                result = SearchUserResult(totalResult, false, list)
                given(githubRepository.search(query, page, sizePerPage)).willReturn(Single.just(result.copy()))
            }
            When("search users"){
                testObserver = usecase.search(query, page, sizePerPage).test()
            }
            Then("usecase return error"){
                testObserver.assertError {
                    it.message == SearchUsersUseCase.ERROR_MESSAGE_END_OF_PAGE
                }
            }
        }
    }
})