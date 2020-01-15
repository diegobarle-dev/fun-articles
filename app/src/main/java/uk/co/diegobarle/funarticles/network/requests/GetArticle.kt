package uk.co.diegobarle.funarticles.network.requests

import uk.co.diegobarle.funarticles.network.DataResult
import uk.co.diegobarle.funarticles.network.PulseLiveService
import uk.co.diegobarle.funarticles.network.responses.GetArticleResponse
import javax.inject.Inject

class GetArticle @Inject constructor(private val service: PulseLiveService) : BaseDataSource<GetArticle.GetArticleRequest, GetArticleResponse>() {

    override suspend fun execute(request: GetArticleRequest): DataResult<GetArticleResponse> {
        return getResult { service.getArticle(request.id) }
    }

    class GetArticleRequest(val id: Long): DataRequest()
}