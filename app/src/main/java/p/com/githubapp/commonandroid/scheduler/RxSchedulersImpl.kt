package p.com.githubapp.commonandroid.scheduler

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.Executors
import javax.inject.Inject

class RxSchedulersImpl @Inject constructor():RxSchedulers {
    private val databaseExecutor = Executors.newSingleThreadExecutor()

    override fun mainThread(): Scheduler {
        return AndroidSchedulers.mainThread()
    }

    override fun networkThread(): Scheduler {
        return Schedulers.io()
    }

    override fun databaseThread(): Scheduler {
        return Schedulers.from(databaseExecutor)
    }
}