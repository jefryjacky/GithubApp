package p.com.githubapp.entity

data class SearchUserResult(
    val total:Int,
    val incomplete:Boolean,
    val previousPage:Int,
    val nextPage:Int,
    val users:List<User>
)