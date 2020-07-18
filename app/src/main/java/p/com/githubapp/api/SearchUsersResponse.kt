package p.com.githubapp.api

import com.google.gson.annotations.SerializedName

data class SearchUsersResponse(
    @SerializedName("total_count")
    val total:Int?,
    @SerializedName("incomplete_results")
    val incomplete:Boolean?,
    @SerializedName("items")
    val users:List<UserResponse>
)