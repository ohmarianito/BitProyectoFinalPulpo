package com.bit.bitproyectofinalpulpo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private lateinit var adapter: MainAdapter
    private val viewModel by lazy { ViewModelProvider(this).get(MainViewModel::class.java)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        adapter = MainAdapter(this)
        recyclerview.layoutManager = LinearLayoutManager (this)
        recyclerview.adapter = adapter

    }

     override fun setContentView(fragmentHome: Int) {

    }
//Creo un observer para recibir los datos de Firebase
    fun observeData() {
    viewModel.fetchEncuesta().observe(
        this,
        Observer { it ->
            adapter.setListData(it)
            adapter.notifyDataSetChanged()
        })
    }
}
//}


