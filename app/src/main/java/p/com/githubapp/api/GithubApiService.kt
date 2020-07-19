package p.com.githubapp.api

import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GithubApiService {
    @GET("search/users")
    fun searchUser(@Query("q") q:String,
                   @Query("page") page:Int):Single<SearchUsersResponse>
}