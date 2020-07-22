package p.com.githubapp.common.scheduler

import io.reactivex.Scheduler

interface RxSchedulers {
    fun mainThread():Scheduler
    fun networkThread():Scheduler
    fun databaseThread():Scheduler
}