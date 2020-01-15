package uk.co.diegobarle.funarticles.network.responses

import com.google.gson.annotations.SerializedName
import uk.co.diegobarle.funarticles.model.Article

data class GetArticleResponse(
    @SerializedName("item") val result: Article
): DataResultsResponse<Article>