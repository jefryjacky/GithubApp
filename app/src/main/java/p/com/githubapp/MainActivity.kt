package p.com.githubapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import dagger.Component
import p.com.githubapp.di.ApplicationComponent

class MainActivity : AppCompatActivity() {
    lateinit var appComponent: ApplicationComponent
    override fun onCreate(savedInstanceState: Bundle?) {
        appComponent = (application as GithubApp).appComponent
        appComponent.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}