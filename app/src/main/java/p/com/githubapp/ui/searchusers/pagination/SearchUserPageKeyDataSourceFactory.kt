package p.com.githubapp.ui.searchusers.pagination

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import io.reactivex.disposables.CompositeDisposable
import p.com.githubapp.domain.entity.User
import p.com.githubapp.domain.usecase.SearchUsersUseCase

class SearchUserPageKeyDataSourceFactory constructor(
    private val searchUsersUseCase: SearchUsersUseCase,
    private val query:String,
    private val disposeables:CompositeDisposable
): DataSource.Factory<Int, User>() {

    val sourceLiveData = MutableLiveData<SearchUserPageKeyDataSource>()
    override fun create(): DataSource<Int, User> {
        val source = SearchUserPageKeyDataSource(searchUsersUseCase, query, disposeables)
        sourceLiveData.value = source
        return source
    }
}