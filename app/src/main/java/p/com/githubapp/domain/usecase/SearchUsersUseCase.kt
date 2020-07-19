package p.com.githubapp.domain.usecase

import io.reactivex.Maybe
import p.com.githubapp.domain.entity.SearchUserResult
import p.com.githubapp.repository.GithubRepository
import java.lang.Exception

class SearchUsersUseCase constructor(
    private val githubRepository:GithubRepository) {

    fun search(query:String, page:Int):Maybe<SearchUserResult>{
        if(query.isBlank() || page == 0) return Maybe.empty()
        return githubRepository.search(query, page)
            .flatMapMaybe {
                if(it.users.isEmpty()) Maybe.error(Exception(ERROR_MESSAGE_NO_MATCHING_ACCOUNT))
                else Maybe.just(it)
            }
    }

    companion object{
        const val ERROR_MESSAGE_NO_MATCHING_ACCOUNT = "There is not matching account"
    }
}