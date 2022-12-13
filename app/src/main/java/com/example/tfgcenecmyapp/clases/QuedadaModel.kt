package com.example.tfgcenecmyapp.clases

data class QuedadaModel (
    var nombre:String?=null,
    var descripcion:String?=null,
    var latitud:Double?=null,
    var longitud:Double?=null,
    var participantes:ArrayList<String>?=null,
    var activaBool:Boolean?=null,
    var nombrecreador:String?=null
        )