package p.com.githubapp.repository

import io.reactivex.Single
import p.com.githubapp.domain.entity.SearchUserResult
import p.com.githubapp.repository.api.GithubApi
import javax.inject.Inject

class GithubRepositoryImpl @Inject constructor(
    private val api:GithubApi
):GithubRepository {
    override fun search(query: String, page: Int): Single<SearchUserResult> {
        return api.searchUsers(query, page)
    }
}