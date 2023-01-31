package pt.ipt.finalproject.utilities

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import pt.ipt.finalproject.dependencyinjection.repositoryModule
import pt.ipt.finalproject.dependencyinjection.viewModelModule

@Suppress("unused")
class AppConfig : Application() {
    override fun onCreate(){
        super.onCreate()
        startKoin{
            androidContext(this@AppConfig)
            modules(listOf(repositoryModule, viewModelModule))
        }
    }
}