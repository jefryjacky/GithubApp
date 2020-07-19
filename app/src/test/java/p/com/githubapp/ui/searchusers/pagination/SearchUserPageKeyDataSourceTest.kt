package p.com.githubapp.ui.searchusers.pagination

import androidx.lifecycle.Observer
import androidx.paging.PageKeyedDataSource.*
import com.nhaarman.mockitokotlin2.*
import io.reactivex.Maybe
import io.reactivex.disposables.CompositeDisposable
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.assertThat
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import p.com.githubapp.domain.entity.SearchUserResult
import p.com.githubapp.domain.entity.User
import p.com.githubapp.domain.usecase.SearchUsersUseCase
import p.com.githubapp.exception.ErrorType
import p.com.githubapp.exception.GithubException
import p.com.githubapp.extension.instantTaskExecutorRule
import p.com.githubapp.ui.NetworkState
import java.lang.Exception
import kotlin.random.Random

class SearchUserPageKeyDataSourceTest: Spek({
    instantTaskExecutorRule()

    lateinit var pageKeyDataSource:SearchUserPageKeyDataSource
    lateinit var searchUserUseCase:SearchUsersUseCase
    val query = "test"
    val disposeables = CompositeDisposable()
    lateinit var initialCallback: LoadInitialCallback<Int, User>
    lateinit var initialParams:LoadInitialParams<Int>
    lateinit var callBack: LoadCallback<Int, User>
    lateinit var params: LoadParams<Int>
    val total = Random.nextInt()
    lateinit var networkStateObser: Observer<NetworkState>
    lateinit var noMatchingAccountObserver: Observer<Boolean>

    beforeEachGroup {
        initialParams = mock()
        initialCallback = mock()
        searchUserUseCase = mock()
        params = mock()
        callBack = mock()
        networkStateObser = mock()
        noMatchingAccountObserver = mock()
        pageKeyDataSource = spy(SearchUserPageKeyDataSource(searchUserUseCase,
            query, disposeables))
        pageKeyDataSource.networkStateEvent.observeForever(networkStateObser)
        pageKeyDataSource.noMatchingAccountEvent.observeForever(noMatchingAccountObserver)
    }

    afterEachGroup {
        disposeables.clear()
    }

    Feature("load initial"){
        Scenario("success initial load"){
            val user = User(1,
                "https://avatars0.githubusercontent.com/u/20434351?v=4",
                "test")
            val result = SearchUserResult(
                total, false, listOf(user)
            )
            Given("search users result"){
               given(searchUserUseCase.search(query, 1))
                   .willReturn(Maybe.just(result.copy()))
            }
            When("load initial"){
                pageKeyDataSource.loadInitial(initialParams, initialCallback)
            }
            Then("callback"){
                verify(initialCallback).onResult(listOf(user), 0, total, 0, 2)
            }
            Then("loading"){
                argumentCaptor<NetworkState> {
                    verify(networkStateObser, times(2)).onChanged(capture())
                    assertThat(firstValue, `is`(NetworkState.LOADING))
                    assertThat(secondValue, `is`(NetworkState.LOADED))
                }
            }
        }

        Scenario("load initial failed exception"){
            val errorMessage = "error"
            val exception = Exception(errorMessage)
            Given("exception"){
                given(searchUserUseCase.search(query, 1))
                    .willReturn(Maybe.error(exception))
            }
            When("load initial"){
                pageKeyDataSource.loadInitial(initialParams, initialCallback)
            }
            Then("call set retry"){
                verify(pageKeyDataSource).setRetryLoadInitial(initialParams, initialCallback)
            }
            Then("loading"){
                argumentCaptor<NetworkState> {
                    verify(networkStateObser, times(2)).onChanged(capture())
                    assertThat(firstValue, `is`(NetworkState.LOADING))
                    assertThat(secondValue, `is`(NetworkState.error(errorMessage)))
                }
            }
        }

        Scenario("load initial failed no matching"){
            val errorMessage = SearchUsersUseCase.ERROR_MESSAGE_NO_MATCHING_ACCOUNT
            val exception = GithubException(ErrorType.COMMON, errorMessage, 0)
            Given("exception"){
                given(searchUserUseCase.search(query, 1))
                    .willReturn(Maybe.error(exception))
            }
            When("load initial"){
                pageKeyDataSource.loadInitial(initialParams, initialCallback)
            }
            Then("call set retry"){
                verify(pageKeyDataSource).setRetryLoadInitial(initialParams, initialCallback)
            }
            Then("loading"){
                argumentCaptor<NetworkState> {
                    verify(networkStateObser, times(2)).onChanged(capture())
                    assertThat(firstValue, `is`(NetworkState.LOADING))
                    assertThat(secondValue, `is`(NetworkState.LOADED))
                }
            }
            Then("notify no matching account observer"){
                then(noMatchingAccountObserver).should().onChanged(true)
            }
        }

        Scenario("load initial failed network error"){
            val errorMessage = "failed to connect"
            val exception = GithubException(ErrorType.NETWORK, errorMessage, 0)
            Given("exception"){
                given(searchUserUseCase.search(query, 1))
                    .willReturn(Maybe.error(exception))
            }
            When("load initial"){
                pageKeyDataSource.loadInitial(initialParams, initialCallback)
            }
            Then("call set retry"){
                verify(pageKeyDataSource).setRetryLoadInitial(initialParams, initialCallback)
            }
            Then("loading"){
                argumentCaptor<NetworkState> {
                    verify(networkStateObser, times(2)).onChanged(capture())
                    assertThat(firstValue, `is`(NetworkState.LOADING))
                    assertThat(secondValue, `is`(NetworkState.error(errorMessage)))
                }
            }
        }
    }

    Feature("load after"){
        val paramsKey = Random.nextInt(1, Int.MAX_VALUE-1)
        Scenario("success load after"){
            val user = User(1,
                "https://avatars0.githubusercontent.com/u/20434351?v=4",
                "test")
            val result = SearchUserResult(
                total, false, listOf(user)
            )
            Given("search users result"){
                given(searchUserUseCase.search(query, paramsKey))
                    .willReturn(Maybe.just(result.copy()))
            }
            Given("params key"){
                params = LoadParams(paramsKey, Random.nextInt())
            }
            When("load after"){
                pageKeyDataSource.loadAfter(params, callBack)
            }
            Then("callback"){
                verify(callBack).onResult(listOf(user), paramsKey+1)
            }
            Then("loading"){
                argumentCaptor<NetworkState> {
                    verify(networkStateObser, times(2)).onChanged(capture())
                    assertThat(firstValue, `is`(NetworkState.LOADING))
                    assertThat(secondValue, `is`(NetworkState.LOADED))
                }
            }
        }

        Scenario("load after failed exception"){
            val errorMessage = "error"
            val exception = Exception(errorMessage)
            Given("exception"){
                given(searchUserUseCase.search(query, paramsKey))
                    .willReturn(Maybe.error(exception))
            }
            Given("params key"){
                params = LoadParams(paramsKey, Random.nextInt())
            }
            When("load initial"){
                pageKeyDataSource.loadAfter(params, callBack)
            }
            Then("call set retry"){
                verify(pageKeyDataSource).setRetryLoadAfter(params, callBack)
            }
            Then("loading"){
                argumentCaptor<NetworkState> {
                    verify(networkStateObser, times(2)).onChanged(capture())
                    assertThat(firstValue, `is`(NetworkState.LOADING))
                    assertThat(secondValue, `is`(NetworkState.error(errorMessage)))
                }
            }
        }

        Scenario("load after failed end of page"){
            val errorMessage = SearchUsersUseCase.ERROR_MESSAGE_END_OF_PAGE
            val exception = GithubException(ErrorType.COMMON, errorMessage, 0)
            Given("exception"){
                given(searchUserUseCase.search(query, paramsKey))
                    .willReturn(Maybe.error(exception))
            }
            Given("params key"){
                params = LoadParams(paramsKey, Random.nextInt())
            }
            When("load initial"){
                pageKeyDataSource.loadAfter(params, callBack)
            }
            Then("call set retry"){
                verify(pageKeyDataSource).setRetryLoadAfter(params, callBack)
            }
            Then("loading"){
                argumentCaptor<NetworkState> {
                    verify(networkStateObser, times(2)).onChanged(capture())
                    assertThat(firstValue, `is`(NetworkState.LOADING))
                    assertThat(secondValue, `is`(NetworkState.LOADED))
                }
            }
        }

        Scenario("load after failed network error"){
            val errorMessage = "failed to connect"
            val exception = GithubException(ErrorType.NETWORK, errorMessage, 0)
            Given("exception"){
                given(searchUserUseCase.search(query, paramsKey))
                    .willReturn(Maybe.error(exception))
            }
            Given("params key"){
                params = LoadParams(paramsKey, Random.nextInt())
            }
            When("load initial"){
                pageKeyDataSource.loadAfter(params, callBack)
            }
            Then("call set retry"){
                verify(pageKeyDataSource).setRetryLoadAfter(params, callBack)
            }
            Then("loading"){
                argumentCaptor<NetworkState> {
                    verify(networkStateObser, times(2)).onChanged(capture())
                    assertThat(firstValue, `is`(NetworkState.LOADING))
                    assertThat(secondValue, `is`(NetworkState.error(errorMessage)))
                }
            }
        }
    }

    Feature("run retry"){
        Scenario("retry load initial"){
            Given("do nothing when call load initial"){
                doNothing().`when`(pageKeyDataSource).loadInitial(initialParams, initialCallback)
            }
           Given("set retry load initial"){
               pageKeyDataSource.setRetryLoadInitial(initialParams, initialCallback)
           }
            When("call retry"){
                pageKeyDataSource.runRetry()
            }
            Then("load initial"){
                then(pageKeyDataSource).should().loadInitial(initialParams,initialCallback)
            }
        }

        Scenario("retry load after"){
            Given("do nothing when call load after"){
                doNothing().`when`(pageKeyDataSource).loadAfter(params, callBack)
            }
            Given("set retry load initial"){
                pageKeyDataSource.setRetryLoadAfter(params, callBack)
            }
            When("call retry"){
                pageKeyDataSource.runRetry()
            }
            Then("load after"){
                then(pageKeyDataSource).should().loadAfter(params, callBack)
            }
        }
    }
})