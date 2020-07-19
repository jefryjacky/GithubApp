package p.com.githubapp.extension

import androidx.arch.core.executor.ArchTaskExecutor
import androidx.arch.core.executor.TaskExecutor
import okhttp3.HttpUrl
import okio.buffer
import okio.source
import org.spekframework.spek2.dsl.Root
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

fun Root.getResponseString(filename:String):String{
    val inputStream = javaClass.classLoader
        ?.getResourceAsStream("response/${filename}")
    val source = inputStream!!.source().buffer()
    return source.readString(Charsets.UTF_8)
}

fun Root.instantTaskExecutorRule(){
    beforeGroup {
        ArchTaskExecutor.getInstance().setDelegate(object : TaskExecutor() {
            override fun executeOnDiskIO(runnable: Runnable) {
                runnable.run()
            }

            override fun postToMainThread(runnable: Runnable) {
                runnable.run()
            }

            override fun isMainThread(): Boolean {
                return true
            }
        })
    }

    afterGroup {
        ArchTaskExecutor.getInstance().setDelegate(null)
    }
}

fun <T> createService(url:HttpUrl, service:Class<T>):T{
    val retrofit = Retrofit.Builder()
        .baseUrl(url)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
    return retrofit.create(service)
}