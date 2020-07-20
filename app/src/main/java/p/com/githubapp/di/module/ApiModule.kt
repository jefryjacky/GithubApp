package p.com.githubapp.di.module

import dagger.Binds
import dagger.Module
import p.com.githubapp.api.GithubApiImpl
import p.com.githubapp.repository.api.GithubApi
import javax.inject.Singleton

@Module
abstract class ApiModule {
    @Binds
    @Singleton
    abstract fun bindGithubApi(githubApi:GithubApiImpl):GithubApi
}