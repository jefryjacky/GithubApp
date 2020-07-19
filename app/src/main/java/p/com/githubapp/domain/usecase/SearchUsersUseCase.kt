package p.com.githubapp.domain.usecase

import io.reactivex.Maybe
import p.com.githubapp.domain.entity.SearchUserResult
import p.com.githubapp.repository.GithubRepository

class SearchUsersUseCase constructor(
    private val githubRepository:GithubRepository) {

    fun search(query:String, page:Int):Maybe<SearchUserResult>{
        if(query.isBlank() || page == 0) return Maybe.empty()
        return githubRepository.search(query, page).toMaybe()
    }
}