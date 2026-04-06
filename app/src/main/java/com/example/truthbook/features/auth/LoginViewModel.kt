package com.example.truthbook.features.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.truthbook.data.models.LoginRequest
import com.example.truthbook.data.remote.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


sealed class LoginState{
    object  Idle : LoginState()
    object  Loading : LoginState()
    data class Success(val message: String?) : LoginState()
    data class Error(val message : String) : LoginState()

}
class LoginViewModel : ViewModel(){
    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState : StateFlow<LoginState> = _loginState

    fun login(identifier : String, password : String){
        if (identifier.isBlank() || password.isBlank()){
            _loginState.value = LoginState.Error("All fields are required")
            return
        }

        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            try{
                    val response = RetrofitInstance.api.login(
                        LoginRequest(identifier = identifier, password = password)
                    )
                if(response.isSuccessful){
                    _loginState.value = LoginState.Success(
                        response.body()?.message ?: "Login successful"
                    )

                }
                else{
                    _loginState.value = LoginState.Error(
                        response.errorBody()?.string() ?: "Login failed"
                    )
                }
            }catch (e : Exception){

                    _loginState.value = LoginState.Error("Network error : ${e.message}")
            }
        }
    }

}