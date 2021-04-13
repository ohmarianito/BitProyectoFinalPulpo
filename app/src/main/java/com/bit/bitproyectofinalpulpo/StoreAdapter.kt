package com.bit.bitproyectofinalpulpo

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.store_item.view.*

class StoreAdapter(private val context: Context): RecyclerView.Adapter<StoreAdapter.StoreViewHolder>() {

    private var dataList = mutableListOf<Producto>()

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
        val item = dataList[position]
        holder.bindView(item)
    }

    inner class StoreViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        fun bindView(item:Producto){
            Glide.with(context).load(item.productoURL).into(itemView.findViewById(R.id.card_view_image))
            itemView.findViewById<TextView>(R.id.productName).text = item.productoNombre
            itemView.findViewById<TextView>(R.id.productValue).text = item.productoValor
        }
    }


}