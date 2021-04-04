package com.bit.bitproyectofinalpulpo

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_home.view.*

class MainAdapter(private val context: Context): RecyclerView.Adapter <MainAdapter.MainViewHolder>() {

    private var  dataList = mutableListOf<Encuesta>()

    fun setListData (data:MutableList<Encuesta>){
        dataList = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.fragment_home, parent)
        return MainViewHolder(view)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        TODO("Not yet implemented")
        val encuesta:Encuesta = dataList [position]
        holder.bindView(encuesta)

    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    //Traspaso los datos recibidos a la vista de fragment home
    inner class MainViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bindView(encuesta: Encuesta){
            itemView.ID.text = encuesta.EncuestaID
            itemView.Nombre.text = encuesta.encuestaNombre
            itemView.Moneda.text = encuesta.encuestaMonedas
            itemView.Descripcion.text = encuesta.encuestaDescripcion
        }
    }
}