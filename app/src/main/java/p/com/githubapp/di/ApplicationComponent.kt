package p.com.githubapp.di

import dagger.Component
import p.com.githubapp.MainActivity
import p.com.githubapp.di.module.*
import p.com.githubapp.ui.searchusers.SearchUsersFragment
import javax.inject.Singleton

@Singleton
@Component(modules = [
    ApplicationModule::class,
    ViewModelModule::class,
    RepositoryModule::class,
    ApiModule::class,
    NetworkModule::class
])
interface ApplicationComponent {
    fun inject(mainActivity: MainActivity)
    fun inject(searchUsersFragment: SearchUsersFragment)
}