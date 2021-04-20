package com.bit.bitproyectofinalpulpo.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.FragmentTransaction
import com.bit.bitproyectofinalpulpo.R
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {
    private val db = FirebaseFirestore.getInstance()
    var encuestas : HashMap<Int, String>
            = HashMap<Int, String> ()
    var titulosEncuestas : ArrayList<String> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val btnEncuesta  = view.findViewById<View>(R.id.buttonEncuesta) as Button
        // obtengo mail
        val userEmail = requireArguments().getString("email")

        if (userEmail != null) {
            cargarEncuestas(userEmail)
        }

        btnEncuesta.setOnClickListener(){
            val args = Bundle()
            val id = "1"
            val fragment = SurveyFragment()
            args.putString("encuestaId", id)
            args.putString("email", userEmail)
            fragment.arguments = args
            val fragmentManager = activity!!.supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragmentContainer, fragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

        return view
    }
    private fun cargarEncuestas(mail: String){
        var idEncuesta = 0
        var tituloEncuesta = ""
        var cantMonedas = ""

        db.collection("encuestas")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    idEncuesta =  document.data["encuestaId"].toString().toInt()
                    tituloEncuesta = document.data["encuestaNombre"].toString()
                    cantMonedas =  document.data["encuestaMonedas"].toString()
                    println("other message VALORESSSS $idEncuesta - $tituloEncuesta"  )
                    encuestas.put(idEncuesta, tituloEncuesta + " - ("  + cantMonedas + " coins)")
                }
                titulosEncuestas = ArrayList(encuestas.values)
                mostrarEncuestas(mail)
            }
            .addOnFailureListener { exception ->
                println("other message Error getting documents: $exception")
            }
    }

    private fun mostrarEncuestas(mail: String){
        //val data = ArrayList(encuestas.values)
        val adapter = ArrayAdapter(context!!, android.R.layout.simple_list_item_1, titulosEncuestas)

        val listViewSample = view!!.findViewById<View>(R.id.listaEncuestas) as ListView

        listViewSample.adapter = adapter

        listViewSample.setOnItemClickListener { parent, _, position, _ ->
            val selectedItem = parent.getItemAtPosition(position) as String
            val idSelected = encuestas.filterValues { it == selectedItem }.keys

            //println("other message SELECTED " + selectedItem )
            println("other message ID " + idSelected)

            val args = Bundle()
            //val id = "1"
            val fragment = SurveyFragment()
            args.putString("encuestaId", idSelected.toString().replace("[", "").replace("]", ""))
            args.putString("email", mail)
            args.putString("encuestaTitulo", selectedItem)
            fragment.arguments = args
            val fragmentManager = activity!!.supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragmentContainer, fragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()


        }
    }
}