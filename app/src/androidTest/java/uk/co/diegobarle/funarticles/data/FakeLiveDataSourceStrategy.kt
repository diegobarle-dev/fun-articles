package uk.co.diegobarle.funarticles.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.jetbrains.annotations.TestOnly
import uk.co.diegobarle.funarticles.network.DataResult

fun <T> resultLiveData(databaseQuery: suspend () -> LiveData<T>): LiveData<DataResult<T>> =
    liveData(Dispatchers.IO) {
        try {
            emit(DataResult.loading())
            var databaseResult: LiveData<T>? = null
            withContext(Dispatchers.Main) {
                databaseResult = databaseQuery.invoke()
            }
            val source = databaseResult?.map {
                DataResult.success(
                    it
                )
            }
            emitSource(source!!)
        }catch (ex: Exception){
            val errorResult: DataResult<T> = DataResult.error("")
            emit(errorResult)
        }
    }

@TestOnly
fun insertData(saveCall: suspend () -> Unit) = runBlocking {
    saveCall.invoke()
}
