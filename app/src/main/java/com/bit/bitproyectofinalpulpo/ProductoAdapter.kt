package com.bit.bitproyectofinalpulpo

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.store_item.view.*

class ProductoAdapter(private val context: Context): RecyclerView.Adapter <ProductoAdapter.StoreViewHolder>() {

    private var  dataList = mutableListOf<Producto>()

    fun setListData (data:MutableList<Producto>){
        dataList = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoreViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.fragment_home, parent)
        return StoreViewHolder(view)
    }

    override fun onBindViewHolder(holder: StoreViewHolder, position: Int) {
        TODO("Not yet implemented")
        val producto:Producto = dataList [position]
        holder.bindView(producto)

    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    //Traspaso los datos recibidos a la vista de fragment home
    inner class StoreViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bindView(producto: Producto){
            Glide.with(context).load(producto.productoURL).into(itemView.card_view_image)
            itemView.findViewById<TextView>(R.id.productName).text = producto.productoNombre
            itemView.findViewById<TextView>(R.id.productValue).text = producto.productoValor
        }
    }
}