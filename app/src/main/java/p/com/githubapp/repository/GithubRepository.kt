package p.com.githubapp.repository

import io.reactivex.Single
import p.com.githubapp.domain.entity.SearchUserResult

interface GithubRepository {
    fun search(query:String, page:Int, sizePerPage:Int):Single<SearchUserResult>
}