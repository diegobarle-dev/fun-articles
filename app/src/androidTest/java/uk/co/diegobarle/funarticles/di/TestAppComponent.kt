package uk.co.diegobarle.funarticles.di

import android.app.Application
import android.content.Context
import android.content.res.Resources
import dagger.BindsInstance
import dagger.Component
import kotlinx.coroutines.ExperimentalCoroutinesApi
import uk.co.diegobarle.funarticles.FunArticlesAppTest
import uk.co.diegobarle.funarticles.articledetails.ArticleDetailsActivityTest
import uk.co.diegobarle.funarticles.articles.ArticlesActivityTest
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@Singleton
@Component(modules = [ TestAppModule::class ])
interface TestAppComponent: AppComponent{
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder
        @BindsInstance
        fun applicationContext(applicationContext: Context): Builder
        @BindsInstance
        fun applicationResources(applicationResources: Resources): Builder
        fun build(): TestAppComponent
    }

    //Test files to inject
    fun inject(test: ArticlesActivityTest)
    fun inject(test: FunArticlesAppTest)
    fun inject(test: ArticleDetailsActivityTest)
}