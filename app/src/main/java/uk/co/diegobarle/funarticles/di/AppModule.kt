package uk.co.diegobarle.funarticles.di

import android.app.Application
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import uk.co.diegobarle.funarticles.data.FunArticlesDB
import uk.co.diegobarle.funarticles.network.PulseLiveService
import uk.co.diegobarle.funarticles.network.requests.FetchArticles
import uk.co.diegobarle.funarticles.network.requests.GetArticle
import javax.inject.Singleton

@Module(includes = [CoreDataModule::class])
class AppModule {

    @Singleton
    @Provides
    fun providePulseLiveService(@PulseliveAPI okhttpClient: OkHttpClient,
                                  converterFactory: GsonConverterFactory
    ) = provideService(PulseLiveService.PULSELIVE_ENDPOINT, okhttpClient, converterFactory, PulseLiveService::class.java)

    @Singleton
    @Provides
    fun provideFetchArticles(service: PulseLiveService) = FetchArticles(service)

    @Singleton
    @Provides
    fun provideGetArticle(service: PulseLiveService) = GetArticle(service)

    @PulseliveAPI
    @Provides
    fun providePulseLivePrivateOkHttpClient(
        upstreamClient: OkHttpClient
    ): OkHttpClient {
        return upstreamClient.newBuilder()
            .build()
    }

    @Singleton
    @Provides
    fun provideDb(app: Application) = FunArticlesDB.getInstance(app)

    @Singleton
    @Provides
    fun provideArticleDao(db: FunArticlesDB) = db.articleDao()

    @CoroutineScropeIO
    @Provides
    fun provideCoroutineScopeIO() = CoroutineScope(Dispatchers.IO)


    private fun createRetrofit(
        serviceEndpoint: String,
        okhttpClient: OkHttpClient,
        converterFactory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(serviceEndpoint)
            .client(okhttpClient)
            .addConverterFactory(converterFactory)
            .build()
    }

    private fun <T> provideService(serviceEndpoint: String,
                                   okhttpClient: OkHttpClient,
                                   converterFactory: GsonConverterFactory, clazz: Class<T>): T {
        return createRetrofit(serviceEndpoint, okhttpClient, converterFactory).create(clazz)
    }
}
