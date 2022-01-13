package com.corrot.kwiatonomousapp.common

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

inline fun <ResultType, RequestType> networkBoundResource(
    crossinline query: () -> Flow<ResultType>,
    crossinline fetch: suspend () -> RequestType,
    crossinline saveFetchResult: suspend (RequestType) -> Unit,
    crossinline shouldFetch: (ResultType) -> Boolean = { true }
) = channelFlow {

    val data = query().first()
    if (shouldFetch(data)) {
        val loading = launch {
            query().collect { send(Result.Loading) }
        }

        try {
            saveFetchResult(fetch())
            loading.cancel()
            query().collect { send(Result.Success(it)) }
        } catch (t: Throwable) {
            loading.cancel()
            query().collect { send(Result.Error(t)) }
        }
    } else {
        query().collect { send(Result.Success(it)) }
    }
}