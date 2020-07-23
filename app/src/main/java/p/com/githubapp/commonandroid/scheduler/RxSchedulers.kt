package p.com.githubapp.commonandroid.scheduler

import io.reactivex.Scheduler

interface RxSchedulers {
    fun mainThread():Scheduler
    fun networkThread():Scheduler
    fun databaseThread():Scheduler
}