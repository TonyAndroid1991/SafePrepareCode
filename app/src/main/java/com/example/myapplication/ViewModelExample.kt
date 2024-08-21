package com.example.myapplication

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart

class ViewModelExample(
    /**inyectar estos campos aqui*/
    val useCaseExample: UseCaseExample,
    val safeFlowUseCaseDelegate: SafeFlowUseCaseDelegate
) : ViewModel(), SafeFlowUseCaseDelegate by safeFlowUseCaseDelegate {


    private val _state = MutableStateFlow<State>(State.Loading)
    val state = _state


    fun getState() {
        useCaseExample.safePrepare(3,
            onNetworkUnavailable = {
                state.value = State.Error(true)
                Log.i("TAG", "getState: onNetworkUnavailable")
            }
        )
            .onStart {
                Log.i("TAG", "getState: starting......")
            }
            .onEach { response ->
                /**ver como se entrelaza el onSuccess*/
                response
                    .onSuccess {
                        _state.value = State.Success(it)
                    }
                    .onFailure {
                        _state.value = State.Error(it)
                    }
            }.catch {
                _state.value = State.Error(true)
            }
            .launchIn(viewModelScope)

    }


    sealed interface State {
        object Loading : State
        data class Success(val string: String) : State
        data class Error(val boolean: Boolean) : State
    }
}

