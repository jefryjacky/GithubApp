package p.com.githubapp.api

import com.google.gson.annotations.SerializedName
import p.com.githubapp.domain.entity.User

data class UserResponse(
    @SerializedName("id")
    val id:Int?,
    @SerializedName("login")
    val username:String?,
    @SerializedName("avatar_url")
    val profilePicture:String?
){
    fun toEntity():User{
        return User(
            id?:0,
            profilePicture?:"",
            username?:""
        )
    }
}