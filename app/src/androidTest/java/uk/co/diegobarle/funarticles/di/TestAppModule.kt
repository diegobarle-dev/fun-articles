package uk.co.diegobarle.funarticles.di

import dagger.Module
import dagger.Provides
import org.mockito.Mockito
import uk.co.diegobarle.funarticles.repositories.ArticlesRep
import javax.inject.Singleton

@Module(includes = [AppModule::class])
class TestAppModule{

    @Singleton
    @Provides
    fun ProvideArticlesRep() = Mockito.mock(ArticlesRep::class.java)
}