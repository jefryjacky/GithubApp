package p.com.githubapp.commonandroid

class Event<T>(private val content:T) {
    var hasBeenHandle =  false
        private set

    val contentIfNotHaveBeenHandle: T?
        get() {
            if(hasBeenHandle) {
                return null
            } else{
                hasBeenHandle = true
                return content
            }
        }

    /**
     * Returns the content, even if it's already been handled.
     */
    fun peekContent(): T = content
}