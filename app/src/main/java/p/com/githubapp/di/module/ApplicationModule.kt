package p.com.githubapp.di.module

import dagger.Binds
import dagger.Module
import p.com.githubapp.commonandroid.scheduler.RxSchedulers
import p.com.githubapp.commonandroid.scheduler.RxSchedulersImpl
import javax.inject.Singleton

@Module
abstract class ApplicationModule {
    @Binds
    @Singleton
    abstract fun bindSchedulers(rxSchedulers: RxSchedulersImpl):RxSchedulers
}