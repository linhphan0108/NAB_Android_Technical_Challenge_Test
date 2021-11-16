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

    suspend fun <T, E> safeApiCall(
        dispatcher: CoroutineDispatcher = ioDispatcher,
        apiCall: suspend () -> T,
        mapper: (suspend (T) -> E)
    ): Flow<ResultWrapper<E>> {
        return flow {
            val response = apiCall.invoke()
            val result = if (response != null) {
                ResultWrapper.Success(mapper.invoke(response))
            } else {
                ResultWrapper.GenericError(-1, MESSAGE_DATA_NULL)
            }
            emit(result)
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