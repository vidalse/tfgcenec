package com.example.tfgceneclm.clases

import android.media.Image

class Quedada {
    var nombreQuedada: String
        get() {
            TODO()
        }
        set(value) {}

    // como us puede ingresar una localizacion
    var localizacionLong: Int
        get() {
            TODO()
        }
        set(value) {}
    var loccalizacionLat: Int
        get() {
            TODO()
        }
        set(value) {}

    //int?
    var idquedada: Int
        get() {
            TODO()
        }
        set(value) {}
    var descripcion: String
        get() {
            TODO()
        }
        set(value) {}

    //como poner para q us ingrese fotos varias
    var foto: Image
        get() {
            TODO()
        }
        set(value) {}
    var precioEstimado: Double
        get() {
            TODO()
        }
        set(value) {}

    var participantes = arrayOf<Usuario>()
    var tipoDeQuedada =
        arrayOf<String>("Deporte", "Cultural", "Senderismo", "Intercambio de Idiomas", "Activismo")

}