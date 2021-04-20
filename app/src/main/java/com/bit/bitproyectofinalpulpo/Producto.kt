package com.bit.bitproyectofinalpulpo

data class Producto(val productoId: String,
                    val productoNombre:String = "DEFAULT NAME",
                    var productoValor:Long,
                    val productoDescripcion:String,
                    val productoURL:String) {


}