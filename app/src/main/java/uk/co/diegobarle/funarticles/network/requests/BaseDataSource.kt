package uk.co.diegobarle.funarticles.network.requests

import retrofit2.Response
import uk.co.diegobarle.funarticles.network.DataResult

abstract class BaseDataSource<R,T> {

    protected suspend fun getResult(call: suspend () -> Response<T>): DataResult<T> {
        try {
            val response = call()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null){
                    return DataResult.success(body)
                }
            }
            return error(" ${response.code()} ${response.message()}")
        } catch (e: Exception) {
            return error(e.message ?: e.toString())
        }
    }

    private fun error(message: String): DataResult<T> {
        return DataResult.error("Network call has failed: $message", null)
    }

    abstract suspend fun execute(request: R): DataResult<T>
}