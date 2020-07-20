package p.com.githubapp.di.module

import dagger.Module
import dagger.Provides
import p.com.githubapp.api.GithubApiService
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class RetrofitServiceModule {
    @Provides
    @Singleton
    fun provideGithubApiService(retrofit: Retrofit):GithubApiService{
        return retrofit.create(GithubApiService::class.java)
    }
}