package uk.co.diegobarle.funarticles

import android.app.Application
import uk.co.diegobarle.funarticles.di.AppComponent
import uk.co.diegobarle.funarticles.di.DaggerAppComponent
import uk.co.diegobarle.funarticles.di.DaggerComponentProvider

open class FunArticlesApp : Application(), DaggerComponentProvider {
    override val appComponent: AppComponent by lazy {
        initializeComponent()
    }

    open fun initializeComponent(): AppComponent {
        return DaggerAppComponent.builder()
            .application(this)
            .applicationContext(applicationContext)
            .applicationResources(resources)
            .build()
    }
}