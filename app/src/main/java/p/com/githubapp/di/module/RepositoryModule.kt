package p.com.githubapp.di.module

import dagger.Binds
import dagger.Module
import p.com.githubapp.repository.GithubRepository
import p.com.githubapp.repository.GithubRepositoryImpl
import javax.inject.Singleton

@Module
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindGithubRepository(repository: GithubRepositoryImpl):GithubRepository
}