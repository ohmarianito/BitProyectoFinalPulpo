package com.bit.bitproyectofinalpulpo.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.bit.bitproyectofinalpulpo.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.android.synthetic.main.fragment_profile.view.*

class StoreFragment : Fragment() {

    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_store, container, false)

        start(view)
        getDataFromFirebase(view)

        return view
    }

    private fun start( view: View){
        view.buttonCloseSession.setOnClickListener{
            FirebaseAuth.getInstance().signOut()

            //Como costo llegar a esta linea jaja
            getActivity()?.onBackPressed();

            //esto redirige a actividad login pero no mata las capaz de abajo si le doy al boton back vuelve a la app

            //val i = Intent(activity, AuthenticationActivity::class.java)
            //startActivity(i)
            //(activity as Activity?)!!.overridePendingTransition(0, 0)

            // esto aca cierra app y mata todito es una bomba nuclear

            // exitProcess(-1)
            println("other message   CERRO SESSION ")
        }
    }

    private fun getDataFromFirebase(view: View) {
        // busca los datos en firebase
        db.collection("productos").get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    // aca iria eso de que guarda los datos y los pasa a la lista, pero mientras el ultimo dato
                    view.findViewById<TextView>(R.id.productName).text = document.id as String?
                    view.findViewById<TextView>(R.id.productValue).text =
                        document.get("productoValor") as String?
                }
            }
    }
}