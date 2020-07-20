package p.com.githubapp

import android.app.Application
import p.com.githubapp.di.DaggerApplicationComponent

class GithubApp:Application() {
    val appComponent = DaggerApplicationComponent.create()
}