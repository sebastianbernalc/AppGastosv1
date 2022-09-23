package com.sebastianb.myapplication.ui.editarperfil

import android.util.Log
import androidx.core.util.PatternsCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sebastianb.myapplication.data.ResourceRemote
import com.sebastianb.myapplication.data.UserRepository
import com.sebastianb.myapplication.model.User
import kotlinx.coroutines.launch


class EditarperfilViewModel : ViewModel() {

    private val userRepository = UserRepository()
    private val _errormsg: MutableLiveData<String?> = MutableLiveData()
    val errorMsg: LiveData<String?> = _errormsg
    private lateinit var user: User
    private val _registerSuccess: MutableLiveData<String?> = MutableLiveData()
    val registerSuccess: LiveData<String?> = _registerSuccess
    fun validateFields(telefono:String,name:String,email: String, password: String){

        if(name.isEmpty() || email.isEmpty() || telefono.isEmpty()){
            _errormsg.value = "Debe llenar todos los campos"}
        else if (telefono.length!=10){
            _errormsg.value = "Su telefono debe contener 10 digitos"
        }
        else if (!PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()){
            _errormsg.value = "Ingrese un correo electronico valido"}


    }
    private fun updateUser(user: User) {
        viewModelScope.launch {
            val result = userRepository.createUser(user)
            result.let { resourceRemote ->
                sequenceOf(
                    when (resourceRemote) {
                        is ResourceRemote.Succes -> {
                            _registerSuccess.postValue(result.data)
                            _errormsg.postValue("Registro Exitoso!")
                        }
                        is ResourceRemote.Error -> {
                            var msg = result.message
                            _errormsg.postValue(msg)
                        }
                        else -> {
                            //dont use
                        }
                    }
                )

            }

        }

    }

}
