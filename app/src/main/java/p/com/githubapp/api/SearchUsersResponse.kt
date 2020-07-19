package p.com.githubapp.api

import com.google.gson.annotations.SerializedName
import p.com.githubapp.domain.entity.SearchUserResult

data class SearchUsersResponse(
    @SerializedName("total_count")
    val total:Int?,
    @SerializedName("incomplete_results")
    val incomplete:Boolean?,
    @SerializedName("items")
    val users:List<UserResponse>
){
    fun toEntity():SearchUserResult{
        return SearchUserResult(
            total?:0,
            incomplete?:false,
            users.map { it.toEntity() }
        )
    }
}