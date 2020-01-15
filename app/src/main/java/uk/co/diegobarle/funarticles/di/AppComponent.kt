package uk.co.diegobarle.funarticles.di

import android.app.Application
import android.content.Context
import android.content.res.Resources
import dagger.BindsInstance
import dagger.Component
import uk.co.diegobarle.funarticles.articledetails.ArticleDetailsViewModel
import uk.co.diegobarle.funarticles.articles.ArticlesViewModel
import javax.inject.Singleton

@Singleton
@Component(modules = [ AppModule::class ])
interface AppComponent{
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder
        @BindsInstance
        fun applicationContext(applicationContext: Context): Builder
        @BindsInstance
        fun applicationResources(applicationResources: Resources): Builder
        fun build(): AppComponent
    }

    //ViewModels factory
    fun articlesViewModelFactory(): ViewModelFactory<ArticlesViewModel>
    fun articleDetailsViewModelFactory(): ViewModelFactory<ArticleDetailsViewModel>
}