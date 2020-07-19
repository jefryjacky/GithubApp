package p.com.githubapp.api

import io.reactivex.Single
import p.com.githubapp.domain.entity.SearchUserResult
import p.com.githubapp.repository.api.GithubApi

class GithubApiImpl constructor(
    private val api:GithubApiService
):GithubApi {
    override fun searchUsers(query: String, page: Int): Single<SearchUserResult> {
        return api.searchUser(query, page).map{
           it.toEntity()
        }
    }
}