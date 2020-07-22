package p.com.githubapp.ui.searchusers.pagination

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import io.reactivex.Completable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import p.com.githubapp.domain.entity.User
import p.com.githubapp.domain.usecase.SearchUsersUseCase
import p.com.githubapp.exception.ErrorType
import p.com.githubapp.exception.GithubException
import p.com.githubapp.ui.Event
import p.com.githubapp.ui.NetworkState

class SearchUserPageKeyDataSource constructor(
    private val searchUsersUseCase: SearchUsersUseCase,
    private val query: String,
    private val disposeables:CompositeDisposable
) : PageKeyedDataSource<Int, User>() {

    var networkStateEvent = MutableLiveData<Event<NetworkState>>()
    var noMatchingAccountEvent = MutableLiveData<Event<Boolean>>()
    private var retry:(()->Unit)? = null

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, User>
    ) {
        disposeables.add(searchUsersUseCase.search(query, 1)
            .doOnSubscribe { networkStateEvent.postValue(Event(NetworkState.LOADING)) }
            .doOnComplete {
                networkStateEvent.postValue(Event(NetworkState.LOADED))
            }
            .subscribe({
                callback.onResult(it.users,
                    0,
                    it.total,
                    0,
                    2)
            },{
                handleError(it)
                setRetryLoadInitial(params, callback)
            }))
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, User>) {
        disposeables.add(searchUsersUseCase.search(query, params.key)
            .doOnSubscribe { networkStateEvent.postValue(Event(NetworkState.LOADING)) }
            .doOnComplete {
                networkStateEvent.postValue(Event(NetworkState.LOADED))
            }
            .subscribe({
                callback.onResult(it.users,
                    params.key + 1)
            },{
                handleError(it)
                setRetryLoadAfter(params, callback)
            }))
    }

    @VisibleForTesting
    internal fun setRetryLoadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, User>){
        retry = {
            loadAfter(params, callback)
        }
    }

    @VisibleForTesting
    internal fun setRetryLoadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, User>){
        retry = {
            loadInitial(params, callback)
        }
    }

    private fun handleError(t: Throwable){
        if(t is GithubException){
            if(t.type == ErrorType.COMMON){
                when (t.message) {
                    SearchUsersUseCase.ERROR_MESSAGE_NO_MATCHING_ACCOUNT -> {
                        noMatchingAccountEvent.postValue(Event(true))
                        networkStateEvent.postValue(Event(NetworkState.LOADED))
                        return
                    }
                    SearchUsersUseCase.ERROR_MESSAGE_END_OF_PAGE -> {
                        networkStateEvent.postValue(Event(NetworkState.LOADED))
                        return
                    }
                }
            }
        }
        networkStateEvent.postValue(Event(NetworkState.error(t.message)))
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, User>) {
    }

    fun runRetry(){
        val prevRetry = retry
        retry = null
        prevRetry?.let {
            disposeables.add(Completable.fromRunnable {
                it.invoke()
            }.subscribe())
        }
    }
}