package com.bit.bitproyectofinalpulpo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

//Recibo los datos a mostrar en la vista

class MainViewModel:ViewModel() {
    private val repo = Repo()
    fun fetchEncuesta():LiveData<MutableList<Encuesta>>{
        val mutableData = MutableLiveData<MutableList<Encuesta>>()
        repo.getEncuesta { it
        mutableData.value = it
        }
    return mutableData
    }

}