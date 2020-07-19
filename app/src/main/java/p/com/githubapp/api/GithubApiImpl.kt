package p.com.githubapp.api

import com.google.gson.Gson
import io.reactivex.Single
import p.com.githubapp.domain.entity.SearchUserResult
import p.com.githubapp.exception.ErrorType
import p.com.githubapp.exception.GithubException
import p.com.githubapp.repository.api.GithubApi
import retrofit2.HttpException
import java.io.IOException

class GithubApiImpl constructor(
    private val api:GithubApiService
):GithubApi {
    override fun searchUsers(query: String, page: Int): Single<SearchUserResult> {
        return api.searchUser(query, page).map{
           it.toEntity()
        }.onErrorResumeNext {
            if(it is HttpException){
                val gson = Gson()
                val json = it.response()?.errorBody()?.string()
                val response = gson.fromJson(json, SearchUsersErrorResponse::class.java)
                return@onErrorResumeNext Single.error<SearchUserResult>(GithubException(ErrorType.HTTP, response.message?:"", it.code()))
            } else if(it is IOException){
                return@onErrorResumeNext Single.error<SearchUserResult>(GithubException(ErrorType.NETWORK, it.localizedMessage ?:"", 0))
            }
            return@onErrorResumeNext Single.error(it)
        }
    }
}