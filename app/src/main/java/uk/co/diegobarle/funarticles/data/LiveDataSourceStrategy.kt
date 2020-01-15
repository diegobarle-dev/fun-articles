package uk.co.diegobarle.funarticles.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import kotlinx.coroutines.Dispatchers
import uk.co.diegobarle.funarticles.network.DataResult

/**
 * The database serves as the single source of truth.
 * Therefore UI can receive data updates from database only.
 * Function notify UI about:
 * [DataResult.Status.SUCCESS] - with data from database
 * [DataResult.Status.ERROR] - if error has occurred from any source
 * [DataResult.Status.LOADING] - while the data is being fetched
 */
fun <T, A> resultLiveData(databaseQuery: () -> LiveData<T>,
                          networkCall: suspend () -> DataResult<A>,
                          saveCallResult: suspend (A) -> Unit
): LiveData<DataResult<T>> =
    liveData(Dispatchers.IO) {
        emit(DataResult.loading())
        val source = databaseQuery.invoke().map {
            DataResult.success(
                it
            )
        }
        emitSource(source)

        val responseStatus = networkCall.invoke()
        if (responseStatus.status == DataResult.Status.SUCCESS) {
            saveCallResult(responseStatus.data!!)
        } else if (responseStatus.status == DataResult.Status.ERROR) {
            emit(DataResult.error(responseStatus.message!!))
        }
    }
