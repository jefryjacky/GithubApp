package p.com.githubapp.domain.entity

data class SearchUserResult(
    val total:Int,
    val incomplete:Boolean,
    val users:List<User>
)