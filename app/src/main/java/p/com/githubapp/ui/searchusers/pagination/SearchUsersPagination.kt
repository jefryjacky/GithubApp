package p.com.githubapp.ui.searchusers.pagination

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import io.reactivex.disposables.CompositeDisposable
import p.com.githubapp.domain.entity.User
import p.com.githubapp.ui.Event
import p.com.githubapp.ui.NetworkState
import javax.inject.Inject

class SearchUsersPagination @Inject constructor(
    private val factory: SearchUserPageKeyDataSourceFactory
) {
    fun getDataSource(): LiveData<PagedList<User>> {
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPrefetchDistance(10)
            .build()
        return LivePagedListBuilder(factory, config).build()
    }

    fun setDisposeable(disposables: CompositeDisposable){
        factory.disposeables = disposables
    }

    fun setQuery(query: String){
        factory.query = query
    }

    fun getNetworkState():LiveData<Event<NetworkState>>{
        return Transformations.switchMap(factory.sourceLiveData){
            it.networkStateEvent
        }
    }

    fun getNoMatchingAccountEvent():LiveData<Event<Boolean>>{
        return Transformations.switchMap(factory.sourceLiveData){
            it.noMatchingAccountEvent
        }
    }

    fun invalidate(){
        factory.sourceLiveData.value?.invalidate()
    }

    fun retry(){
        factory.sourceLiveData.value?.runRetry()
    }
}