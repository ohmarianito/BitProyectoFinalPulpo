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
    var titulosEncuestas : MutableList<String> = mutableListOf()
    var encuestasCompletadas : MutableList<Int> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val btnEncuesta  = view.findViewById<View>(R.id.buttonEncuesta) as Button
        // obtengo mail
        val userEmail = requireArguments().getString("email")
        val fromHome = requireArguments().getBoolean("fromHome")
        this.requireArguments().remove("fromHome")


        if (userEmail != null) {
            iniciarEncuestasCompletadas(userEmail, fromHome)
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
    private fun iniciarEncuestasCompletadas(email: String, fromHome: Boolean){
        if(fromHome){
            db.collection("usuarioEncuesta").document(email).set(
                hashMapOf(
                    "encuestaUno" to 9,
                    "encuestaDos" to 9,
                    "encuestaTres" to 9
                )
            )
        }else{
            encuestasCompletadas.clear()
            encuestas.clear()
            titulosEncuestas.clear()
            println("other message CANTIDADDDDDDDDDDDDDD " + encuestasCompletadas.count())
            db.collection("usuarioEncuesta").document(email).get().addOnSuccessListener {
                var valueUno = it.get("encuestaUno").toString().toInt()
                var valueDos = it.get("encuestaDos").toString().toInt()
                var valueTres = it.get("encuestaTres").toString().toInt()
                encuestasCompletadas.add(valueUno)
                encuestasCompletadas.add(valueDos)
                encuestasCompletadas.add(valueTres)
                println("other message VALORESSSSSSSSSsadasfsd43556354 " +  valueUno + " - " + valueDos  + " - " + valueTres)
            }
            println("other message CANTIDADDDDDDDDDDDDDD 222222222222 " + encuestasCompletadas.count())
        }
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
                    println("other message ANTES VALIDAR" + encuestasCompletadas.count())
                    if (!encuestasCompletadas.contains(idEncuesta)){
                        println("other message METIOOOOO")
                        encuestas.put(idEncuesta, tituloEncuesta + " - ("  + cantMonedas + " coins)")
                    }
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