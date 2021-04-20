package com.bit.bitproyectofinalpulpo

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.bit.bitproyectofinalpulpo.fragments.StoreFragment
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore


class StoreAdapter(private val context: Context, val email: String, val view: View): RecyclerView.Adapter<StoreAdapter.StoreViewHolder>() {

    private var dataList = mutableListOf<Producto>()
    private val db = FirebaseFirestore.getInstance()

    fun setListData(data:MutableList<Producto>) {
        dataList = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoreViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.store_item, parent, false)
        return StoreViewHolder(view)
    }

    override fun getItemCount(): Int {
        return if (dataList.size > 0) {
            dataList.size
        } else {
            0
        }
    }

    override fun onBindViewHolder(holder: StoreViewHolder, position: Int) {
        var item = dataList[position]
        holder.bindView(item)
        holder.button.setOnClickListener {
            getDataFromFirebase(email, object: StoreFragment.CoinCallback {
                override fun onCallback(coins: Int) {
                    var monedas = coins.toInt()
                    var productoValor = item.productoValor
                    if (monedas >= productoValor) {
                        var monedas_act = monedas - productoValor
                        db.collection("usuarios").document(email).update("monedas", monedas_act)
                        actualizarMonedas(monedas_act.toInt())
                        var msg = "Producto canjeado con exito"
                        basicAlert(msg)
                    } else {
                        var msg = "No se puede canjear, saldo insuficiente"
                        basicAlert(msg)

                    }
                }
            } )

        }

    }

    fun basicAlert(msg: String){

        val ad =
            AlertDialog.Builder(context)
        ad.setTitle(msg)
        ad.setPositiveButton(
            "OK"
        ) { dialog, id -> dialog.dismiss() }
        ad.show()

    }

    private fun actualizarMonedas(monedas: Int) {
        view.findViewById<TextView>(R.id.store_coins).text = monedas.toString()
    }

    inner class StoreViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        val button = itemView.findViewById<Button>(R.id.boton_canjear)
        fun bindView(item:Producto){
            Glide.with(context).load(item.productoURL).into(itemView.findViewById(R.id.card_view_image))
            itemView.findViewById<TextView>(R.id.productName).text = item.productoNombre
            val value = (item.productoValor).toString()
            itemView.findViewById<TextView>(R.id.productValue).text = value
        }
    }

    private fun getDataFromFirebase(email: String, myCallback: StoreFragment.CoinCallback){
        // al cargar el fragment va a buscar la info
        db.collection("usuarios").document(email).get().addOnSuccessListener {
            var coins = (it.get("monedas") as Long?).toString().toInt()
            myCallback.onCallback(coins)
        }
    }

    interface CoinCallback {
        fun onCallback(value: Int)
    }





}