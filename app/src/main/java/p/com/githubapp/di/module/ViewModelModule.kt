package p.com.githubapp.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import p.com.githubapp.di.ViewModelKey
import p.com.githubapp.ui.ViewModelFactory
import p.com.githubapp.ui.searchusers.SearchUsersViewModel
import javax.inject.Singleton

@Module
abstract class ViewModelModule {
    @Binds
    @Singleton
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(SearchUsersViewModel::class)
    abstract fun bindSearchUsersViewModel(searchUsersViewModel: SearchUsersViewModel):ViewModel
}