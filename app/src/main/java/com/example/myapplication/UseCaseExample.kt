package com.example.myapplication

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class UseCaseExample(
    private val repository: Repository,
    dispatcherProvider: DispatcherProvider
) : FlowUseCase<Int, Either<String, Boolean>>(dispatcherProvider) {

    override fun prepareFlow(input: Int): Flow<Either<String, Boolean>> = flow {
        if (input > 5) {
            emit( eitherSuccess("congrats input is bigger than 5"))
        } else {
            emit(eitherFailure( true))
        }
    }
}

interface Repository {
    fun getInfo()
}