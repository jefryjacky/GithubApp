package p.com.githubapp.ui.searchusers.pagination

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import io.reactivex.disposables.CompositeDisposable
import p.com.githubapp.domain.entity.User
import p.com.githubapp.domain.usecase.SearchUsersUseCase
import javax.inject.Inject

class SearchUserPageKeyDataSourceFactory @Inject constructor(
    private val searchUsersUseCase: SearchUsersUseCase
): DataSource.Factory<Int, User>() {
    var query:String? = null
    var disposeables:CompositeDisposable? = null
    val sourceLiveData = MutableLiveData<SearchUserPageKeyDataSource>()
    override fun create(): DataSource<Int, User> {
        val source = SearchUserPageKeyDataSource(searchUsersUseCase, query!!, disposeables!!)
        sourceLiveData.value = source
        return source
    }
}