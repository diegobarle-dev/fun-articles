package uk.co.diegobarle.funarticles

import kotlinx.coroutines.ExperimentalCoroutinesApi
import uk.co.diegobarle.funarticles.di.AppComponent
import uk.co.diegobarle.funarticles.di.DaggerTestAppComponent

@ExperimentalCoroutinesApi
class TestFunArticlesApp: FunArticlesApp(){

    override fun initializeComponent(): AppComponent {
        val component =
        DaggerTestAppComponent.builder()
            .application(this)
            .applicationContext(applicationContext)
            .applicationResources(resources)
            .build()

        return component
    }
}