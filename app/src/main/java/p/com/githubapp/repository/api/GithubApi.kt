package p.com.githubapp.repository.api

import io.reactivex.Single
import p.com.githubapp.entity.SearchUserResult

interface GithubApi {
    fun searchUsers(query:String, page:Int):Single<SearchUserResult>
}