package uk.co.diegobarle.funarticles.network.requests

import uk.co.diegobarle.funarticles.network.DataResult
import uk.co.diegobarle.funarticles.network.PulseLiveService
import uk.co.diegobarle.funarticles.network.responses.FetchArticlesResponse
import javax.inject.Inject

class FetchArticles @Inject constructor(private val service: PulseLiveService) : BaseDataSource<FetchArticles.FetchArticlesRequest, FetchArticlesResponse>() {

    override suspend fun execute(request: FetchArticlesRequest): DataResult<FetchArticlesResponse> {
        return getResult { service.fetchArticles() }
    }

    class FetchArticlesRequest: DataRequest()
}