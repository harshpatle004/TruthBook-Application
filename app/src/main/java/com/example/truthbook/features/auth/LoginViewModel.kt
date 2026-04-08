package com.example.truthbook.features.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.truthbook.data.TokenDataStore
import com.example.truthbook.data.models.LoginRequest
import com.example.truthbook.data.remote.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject

sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    data class Success(val message: String?) : LoginState()
    data class Error(val message: String) : LoginState()
}

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState

    fun login(identifier: String, password: String) {  // ✅ no context param
        if (identifier.isBlank() || password.isBlank()) {
            _loginState.value = LoginState.Error("All fields are required")
            return
        }

        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            try {
                val response = RetrofitInstance.api.login(
                    LoginRequest(identifier = identifier, password = password)
                )
                if (response.isSuccessful) {
                    val token = response.body()?.token
                    if (token != null) {
                        // ✅ context comes from AndroidViewModel internally
                        TokenDataStore.saveToken(getApplication(), token)
                    }
                    _loginState.value = LoginState.Success(
                        response.body()?.message ?: "Login successful"
                    )
                } else {
                    val errorMsg = try {
                        JSONObject(
                            response.errorBody()?.string() ?: ""
                        ).getString("message")
                    } catch (e: Exception) {
                        "Login failed. Please try again."
                    }
                    _loginState.value = LoginState.Error(errorMsg)
                }
            } catch (e: Exception) {
                _loginState.value = LoginState.Error("Network error: ${e.message}")
            }
        }
    }
}