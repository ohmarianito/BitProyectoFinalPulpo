package com.bit.bitproyectofinalpulpo.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bit.bitproyectofinalpulpo.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.android.synthetic.main.fragment_profile.view.*
import kotlinx.android.synthetic.main.fragment_store.*
import kotlinx.coroutines.tasks.await


class StoreFragment : Fragment() {

    private lateinit var adapter:StoreAdapter

    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_store, container, false)

        val userEmail = requireArguments().getString("email")

        if (userEmail != null) {
            getDataFromFirebase(view, userEmail)
        }


        adapter = StoreAdapter(context!!)

        var recyclerView = view.findViewById<RecyclerView>(R.id.rvStore)

        recyclerView.layoutManager = LinearLayoutManager(context!!) //este seria el this
        recyclerView.adapter = adapter






        getProductsFromFirebase(object: ProductsListCallback {
            override fun onCallback(productos:MutableList<Producto>) {
                adapter.setListData(productos)
                adapter.notifyDataSetChanged()
            }
        })



        return view
    }



    private fun getDataFromFirebase( view: View, userEmail: String){
        // al cargar el fragment va a buscar la info
        db.collection("usuarios").document(userEmail).get().addOnSuccessListener {
            view.findViewById<TextView>(R.id.store_coins).text = (it.get("monedas") as Long?).toString()
        }
    }



    interface ProductsListCallback {
        fun onCallback(value:MutableList<Producto>)
    }

    fun getProductsFromFirebase(myCallback:ProductsListCallback) {
        var mutableData = MutableLiveData <MutableList<Producto>>()
        var listData = mutableListOf<Producto>()
        db.collection("productos").get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    var productoId = document.id
                    var productoNombre = document.get("productoNombre") as String?
                    var productoValor = document.get("productoValor") as Long?
                    var productoDescripcion = document.get("productoDescripcion") as String?
                    var productoURL = document.get("productoURL") as String?
                    var producto = Producto(
                        productoId,
                        productoNombre!!,
                        productoValor!!,
                        productoDescripcion!!,
                        productoURL!!
                    )
                    listData.add((producto))
                }

                mutableData.value = listData
                myCallback.onCallback(listData)

            }

    }

    }


