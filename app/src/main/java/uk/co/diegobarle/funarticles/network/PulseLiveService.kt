package uk.co.diegobarle.funarticles.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import uk.co.diegobarle.funarticles.network.responses.FetchArticlesResponse
import uk.co.diegobarle.funarticles.network.responses.GetArticleResponse

interface PulseLiveService {
    companion object {
        const val PULSELIVE_ENDPOINT = "https://dynamic.pulselive.com/test/native/"
    }

    /**
     * Returns a list of all the articles
     */
    @GET("contentList.json")
    suspend fun fetchArticles(): Response<FetchArticlesResponse>

    /**
     * Returns all the details for an article
     * @param id article identifier
     */
    @GET("content/{id}.json")
    suspend fun getArticle(@Path("id") id: Long): Response<GetArticleResponse>
}