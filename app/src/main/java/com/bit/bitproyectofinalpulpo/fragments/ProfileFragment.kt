package com.bit.bitproyectofinalpulpo.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bit.bitproyectofinalpulpo.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_profile.view.*
import kotlin.system.exitProcess


class ProfileFragment : Fragment() {
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_profile, container, false)
        // obtengo mail
        val userEmail = requireArguments().getString("email")

        //println("other message2 userEmail " + userEmail)

        if (userEmail != null) {
            start(view, userEmail)
            getDataFromFirebase(view, userEmail)
            updateButtonImpl(view, userEmail)
        }
        return view
    }

    private fun start( view: View, userEmail: String){
        view.findViewById<TextView>(R.id.textViewEmail).text = userEmail
        view.buttonCloseSession.setOnClickListener{
            FirebaseAuth.getInstance().signOut()

            //Como costo llegar a esta linea jaja
            //getActivity()?.onBackPressed();

            //esto redirige a actividad login pero no mata las capaz de abajo si le doy al boton back vuelve a la app

            //val i = Intent(activity, AuthenticationActivity::class.java)
            //startActivity(i)
            //(activity as Activity?)!!.overridePendingTransition(0, 0)

            // esto aca cierra app y mata todito es una bomba nuclear

            exitProcess(-1)
            println("other message   CERRO SESSION ")
        }
    }
    private fun getDataFromFirebase( view: View, userEmail: String){
        // al cargar el fragment va a buscar la info
        db.collection("usuarios").document(userEmail).get().addOnSuccessListener {
            view.findViewById<TextView>(R.id.editTextName).text = it.get("nombre") as String?
            view.findViewById<TextView>(R.id.editTextSurname).text = it.get("apellido") as String?
            view.findViewById<TextView>(R.id.editTextPhone).text = it.get("nroCell") as String?
            view.findViewById<TextView>(R.id.editTextDate).text = it.get("fecha") as String?
            view.findViewById<TextView>(R.id.editTextCountry).text = it.get("pais") as String?
            view.findViewById<TextView>(R.id.editTextDepartment).text = it.get("depto") as String?
        }
    }
    private fun updateButtonImpl( view: View, userEmail: String){
        view.buttonUpdateData.setOnClickListener{

            val nombre = view.findViewById<EditText>(R.id.editTextName).text.toString()
            val apellido = view.findViewById<EditText>(R.id.editTextSurname).text.toString()
            val nroCell = view.findViewById<EditText>(R.id.editTextPhone).text.toString()
            val fecha = view.findViewById<EditText>(R.id.editTextDate).text.toString()
            val pais = view.findViewById<EditText>(R.id.editTextCountry).text.toString()
            val depto = view.findViewById<EditText>(R.id.editTextDepartment).text.toString()

            //println("other message ACTUALIZAR " + nombre + apeliido + nroCell + fecha + pais + depto)

            // db es la conexion global
            // collection seria lo que es la tabla
            // en este caso usuarios es el id
           /* db.collection("usuarios").document(userEmail).set(
                hashMapOf(
                    "nombre" to nombre,
                    "apellido" to apellido,
                    "nroCell" to nroCell,
                    "fecha" to fecha,
                    "pais" to pais,
                    "depto" to depto)
            )*/
            db.collection("usuarios").document(userEmail).update("nombre", nombre)
            db.collection("usuarios").document(userEmail).update("apellido", apellido)
            db.collection("usuarios").document(userEmail).update("nroCell", nroCell)
            db.collection("usuarios").document(userEmail).update("fecha", fecha)
            db.collection("usuarios").document(userEmail).update("pais", pais)
            db.collection("usuarios").document(userEmail).update("depto", depto)

            view.findViewById<TextView>(R.id.editTextName).text = nombre
            view.findViewById<TextView>(R.id.editTextSurname).text = apellido
            view.findViewById<TextView>(R.id.editTextPhone).text = nroCell
            view.findViewById<TextView>(R.id.editTextDate).text = fecha
            view.findViewById<TextView>(R.id.editTextCountry).text = pais
            view.findViewById<TextView>(R.id.editTextDepartment).text = depto
        }
    }
}