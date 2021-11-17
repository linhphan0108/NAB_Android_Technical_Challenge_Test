package com.linhphan.data.repository

import com.linhphan.domain.entity.ResultWrapper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException
import java.io.IOException

const val MESSAGE_UNKNOWN_ERROR = "Unknown Error"
const val MESSAGE_DATA_NULL = "Data is null"
abstract class BaseRepository(
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
)  {

    /**
     * this function assert that there is no exception thrown while running our query
     * @param dbCall to query data from local database
     * @param apiCall to fetching data from the remote server
     * @param forceApiCall if true the [apiCall] will be always called despite whatever data returned from [dbCall]
     * if [forceApiCall] returns false the [apiCall] will be called only if [dbCall] returns null.
     */
    suspend fun <T, E> safeCall(
        dispatcher: CoroutineDispatcher = ioDispatcher,
        dbCall: suspend () -> E?,
        apiCall: suspend () -> T,
        forceApiCall: (E) -> Boolean,
        mapper: (suspend (T) -> E)
    ): Flow<ResultWrapper<E>> {
        return flow {
            //fetching cached data if available
            val localData = dbCall.invoke()
            if(localData != null){
                emit(ResultWrapper.Success(localData))
            }
            if (localData == null || forceApiCall.invoke(localData)){
                emit(ResultWrapper.InProgress)
                val response = apiCall.invoke()
                val result = if (response != null) {
                    ResultWrapper.Success(mapper.invoke(response))
                } else {
                    ResultWrapper.GenericError(-1, MESSAGE_DATA_NULL)
                }
                emit(result)
            }
        }.catch { throwable ->
            val errorResult = when (throwable) {
                is IOException -> ResultWrapper.NetworkError
                is HttpException -> {
                    val code = throwable.code()
                    val errorResponse = convertErrorBody(throwable)
                    ResultWrapper.GenericError(code, errorResponse)
                }
                else -> {
                    val message = throwable.message ?: MESSAGE_UNKNOWN_ERROR
                    ResultWrapper.GenericError(-1, message)
                }
            }
            emit(errorResult)
        }.flowOn(dispatcher)
    }

    private fun convertErrorBody(throwable: HttpException): String {
        return throwable.message()
    }
}