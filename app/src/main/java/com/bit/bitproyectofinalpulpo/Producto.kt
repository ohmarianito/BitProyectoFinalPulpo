package com.bit.bitproyectofinalpulpo

data class Producto(val productoId: String,
                    val productoNombre:String = "DEFAULT NAME",
                    val productoValor:String,
                    val productoDescripcion:String,
                    val productoURL:String) {


}