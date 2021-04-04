package com.bit.bitproyectofinalpulpo

import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot


// Desde aca se importaran los valores de Firebase, los campos tienen que tener el mismo nombre que en la base de Firebase
class Repo {
    fun getEncuesta(param: (Any) -> Unit) {
        val mutableData = MutableLiveData <MutableList<Encuesta>>()
            FirebaseFirestore.getInstance().collection("Encuestas").get().addOnSuccessListener { result ->
                val  listData = mutableListOf<Encuesta>()
                for (document: QueryDocumentSnapshot in result) {
                val  EncuestaID = document.getString("EncuestaID")
                val encuestaNombre = document.getString("encuestaNombre")
                val encuestaMonedas = document.getString("encuestaMonedas")
                val encuestaDescripcion = document.getString("encuestaDescripcion")
                val encuesta = Encuesta(EncuestaID!!, encuestaNombre!!, encuestaDescripcion!!, encuestaMonedas!!)
                listData.add((encuesta))
                }
                mutableData.value = listData
        }

    }
}