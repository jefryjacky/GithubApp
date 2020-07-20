package p.com.githubapp.di

import dagger.Component
import p.com.githubapp.MainActivity
import p.com.githubapp.di.module.ApiModule
import p.com.githubapp.di.module.NetworkModule
import p.com.githubapp.di.module.RepositoryModule
import p.com.githubapp.di.module.ViewModelModule
import p.com.githubapp.ui.searchusers.SearchUsersFragment
import javax.inject.Singleton

@Singleton
@Component(modules = [
    ViewModelModule::class,
    RepositoryModule::class,
    ApiModule::class,
    NetworkModule::class
])
interface ApplicationComponent {
    fun inject(mainActivity: MainActivity)
    fun inject(searchUsersFragment: SearchUsersFragment)
}