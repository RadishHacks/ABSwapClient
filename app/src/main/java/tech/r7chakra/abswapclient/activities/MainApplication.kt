package tech.r7chakra.abswapclient.activities

import android.app.Application
import tech.r7chakra.abswapclient.dagger.components.DaggerMainApplicationComponent
import tech.r7chakra.abswapclient.dagger.components.MainApplicationComponent
import tech.r7chakra.abswapclient.dagger.modules.MainApplicationModule

class MainApplication : Application() {

    companion object {
        lateinit var instance : MainApplication
        lateinit var mainApplicationComponent : MainApplicationComponent

    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        mainApplicationComponent =
            DaggerMainApplicationComponent
                .builder()
                .mainApplicationModule(MainApplicationModule(this))
                .build()
    }
}