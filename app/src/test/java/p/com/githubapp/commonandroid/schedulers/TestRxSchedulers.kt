package p.com.githubapp.commonandroid.schedulers

import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import p.com.githubapp.commonandroid.scheduler.RxSchedulers

class TestRxSchedulers:RxSchedulers {
    override fun mainThread(): Scheduler {
        return Schedulers.trampoline()
    }

    override fun networkThread(): Scheduler {
        return Schedulers.trampoline()
    }

    override fun databaseThread(): Scheduler {
        return Schedulers.trampoline()
    }
}