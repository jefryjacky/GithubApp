package p.com.githubapp.exception

enum class ErrorType{
    HTTP,
    NETWORK,
    COMMON
}

class GithubException(val type: ErrorType, message:String, val code:Int):Exception(message)