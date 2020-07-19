package p.com.githubapp.api

import com.google.gson.annotations.SerializedName

data class SearchUsersErrorResponse(
    @SerializedName("message")
    val message:String?
)