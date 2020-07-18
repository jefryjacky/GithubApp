package p.com.githubapp.api

import io.reactivex.Single
import p.com.githubapp.entity.SearchUserResult
import p.com.githubapp.extension.queryParam
import p.com.githubapp.repository.api.GithubApi
import java.net.URI

class GithubApiImpl constructor(
    private val api:GithubApiService
):GithubApi {
    override fun searchUsers(query: String, page: Int): Single<SearchUserResult> {
        return api.searchUser(query, page).map { response->
            val userSearchResponse = response.body()
            val linkHeader = response.headers().get("link")
            if(userSearchResponse!=null){
                return@map SearchUserResult(
                         nextPage = getPageFromLinkHeader(linkHeader, "next"),
                 previousPage = getPageFromLinkHeader(linkHeader, "prev"),
                 total = userSearchResponse.total?:0,
                 incomplete = userSearchResponse.incomplete?:false,
                 users = userSearchResponse.users.map { userResponse->
                     userResponse.toEntity()
                 })
            }
            null
        }
    }

    private fun getPageFromLinkHeader(linkHeader:String?, rel:String):Int{
        if(linkHeader.isNullOrBlank()) return 0
        val linkWithRel = linkHeader.split(",")
        linkWithRel.forEach {
            val split = it.split(";")
            val localRel = split.last().split("=").last()
            val relValue = localRel
                .removePrefix("\"")
                .removeSuffix("\"")
            if(rel == relValue){
                val link = URI.create(split.first()
                    .removePrefix("<")
                    .removeSuffix(">"))
                val page = link.queryParam("page")
                return page.toInt()
            }
        }
        return 0
    }

}