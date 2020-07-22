package p.com.githubapp.common.schedulers

import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import p.com.githubapp.common.scheduler.RxSchedulers

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