package p.com.githubapp.ui.searchusers

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import io.reactivex.disposables.CompositeDisposable
import p.com.githubapp.domain.entity.User
import p.com.githubapp.commonandroid.Event
import p.com.githubapp.commonandroid.NetworkState
import p.com.githubapp.ui.searchusers.pagination.SearchUsersPagination
import javax.inject.Inject

class SearchUsersViewModel @Inject constructor(
    private val pagination: SearchUsersPagination
):ViewModel() {
    private val disposeables = CompositeDisposable()
    val users:LiveData<PagedList<User>>
    val networkStateEvent:LiveData<Event<NetworkState>> = pagination.getNetworkState()
    val noMatchingAccountEvent:LiveData<Event<Boolean>> = pagination.getNoMatchingAccountEvent()

    init {
        pagination.setDisposeable(disposeables)
        users = pagination.getDataSource()
    }

    fun query(query:String){
        pagination.setQuery(query)
        pagination.invalidate()
    }

    fun retry(){
        pagination.retry()
    }

    override fun onCleared() {
        super.onCleared()
        disposeables.clear()
    }
}