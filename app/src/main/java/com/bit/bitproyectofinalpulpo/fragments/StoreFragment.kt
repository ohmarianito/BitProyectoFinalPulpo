package com.bit.bitproyectofinalpulpo.fragments

import android.content.Intent.getIntent
import android.content.Intent.getIntentOld
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.app.ActivityCompat.recreate
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bit.bitproyectofinalpulpo.Producto
import com.bit.bitproyectofinalpulpo.R
import com.bit.bitproyectofinalpulpo.StoreAdapter
import com.google.firebase.firestore.FirebaseFirestore


class StoreFragment : Fragment() {

    private lateinit var adapter:StoreAdapter

    private val db = FirebaseFirestore.getInstance()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_store, container, false)

        val userEmail = requireArguments().getString("email")

        var email = ""
        var monedas = 0
        if (userEmail != null) {
            /*getDataFromFirebase(userEmail, object: CoinCallback {
                override fun onCallback(coins: Int) {
                    view.findViewById<TextView>(R.id.store_coins).text = coins.toString()
                    monedas = coins.toInt()
                }
            } )*/
            getCoinsFromFirebase(view, userEmail)
            email = userEmail
        }

        adapter = StoreAdapter(context!!, email, view)

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



    private fun getCoinsFromFirebase( view: View, userEmail: String){
        // al cargar el fragment va a buscar la info
        db.collection("usuarios").document(userEmail).get().addOnSuccessListener {
            view.findViewById<TextView>(R.id.store_coins).text = (it.get("monedas") as Long?).toString()
        }
    }



    private fun getDataFromFirebase(email: String, myCallback:CoinCallback){
        // al cargar el fragment va a buscar la info
        db.collection("usuarios").document(email).get().addOnSuccessListener {
            var coins = (it.get("monedas") as Long?).toString()
            myCallback.onCallback(coins.toInt())
        }
    }

    interface CoinCallback {
        fun onCallback(value: Int)
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


