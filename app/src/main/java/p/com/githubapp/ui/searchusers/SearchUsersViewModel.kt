package p.com.githubapp.ui.searchusers

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import io.reactivex.disposables.CompositeDisposable
import p.com.githubapp.domain.entity.User
import p.com.githubapp.ui.NetworkState
import p.com.githubapp.ui.searchusers.pagination.SearchUsersPagination

class SearchUsersViewModel constructor(
    private val pagination: SearchUsersPagination
):ViewModel() {
    private val disposeables = CompositeDisposable()
    val users:LiveData<PagedList<User>>
    val networkStateEvent:LiveData<NetworkState> = pagination.getNetworkState()
    val noMatchingAccountEvent:LiveData<Boolean> = pagination.getNoMatchingAccountEvent()

    init {
        pagination.setDisposeable(disposeables)
        users = pagination.getDataSource()
    }

    fun query(query:String){
        pagination.setQuery(query)
        pagination.invalidate()
    }

    override fun onCleared() {
        super.onCleared()
        disposeables.clear()
    }
}