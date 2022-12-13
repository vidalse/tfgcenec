package com.example.tfgcenecmyapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class CreateQuedadaActivity : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_quedada)


        findViewById<Button>(R.id.crearbtn).setOnClickListener {
            var descripcionn:String?=""
            if(!findViewById<EditText>(R.id.inputDescripcion).text.isEmpty()){
                 descripcionn =findViewById<EditText>(R.id.inputDescripcion).text.toString()
            }
            var latitud: Double?=null
            if(!findViewById<EditText>(R.id.inputLatitud).text.isEmpty()){
                 latitud= findViewById<EditText>(R.id.inputLatitud).text.toString().toDouble()
            }
            var longitud: Double?=null
            if(!findViewById<EditText>(R.id.inputLong).text.isEmpty()){
                longitud = findViewById<EditText>(R.id.inputLong).text.toString().toDouble()
            }
            var nombrequedada: String=""
            if(!findViewById<EditText>(R.id.inputTitulo).text.isEmpty()){
                nombrequedada=findViewById<EditText>(R.id.inputTitulo).text.toString()
            }
            agregarDatos(descripcionn,latitud,longitud,nombrequedada)




        }
        findViewById<Button>(R.id.button2).setOnClickListener {
            val email= intent.getStringExtra("email")
            val name= intent.getStringExtra("name")

            val intent2=Intent(this, MapsActivity::class.java)
            intent2.putExtra("name",name)
            intent2.putExtra("email",email)


            startActivity(intent2)


        }/////////////////////////////////////////////////////////////////////////////////////////////////////////////// arriba

    }
    private fun agregarDatos(descripcion:String?,latitud:Double?,longitud:Double?,nombrequedada:String){
        val descripcionn = findViewById<EditText>(R.id.inputDescripcion)
        val nombrequedadaa = findViewById<EditText>(R.id.inputTitulo)

        if(latitud==null || descripcionn.text.isEmpty() || longitud==null || nombrequedadaa.text.isEmpty()) {

            Toast.makeText(this,"Rellene todos los campos",Toast.LENGTH_SHORT).show()
            if (nombrequedadaa.text.isEmpty()) {
                nombrequedadaa.setError( "Este campo es obligatorio")
                Toast.makeText(this, "Falta el campo titulo", Toast.LENGTH_SHORT).show()

            }

            if (descripcionn.text.isEmpty()) {
                    descripcionn.error = "Este campo es obligatorio"
                    Toast.makeText(this, "Falta el campo descripción", Toast.LENGTH_SHORT).show()


            }
            val latitudd = findViewById<EditText>(R.id.inputLatitud)

            if (latitud == null) {
                latitudd.error = "Este campo es obligatorio"
                Toast.makeText(this, "Falta el campo latitud", Toast.LENGTH_SHORT).show()

            }
            val longitudd = findViewById<EditText>(R.id.inputLong)

            if (longitud == null) {
                longitudd.error = "Este campo es obligatorio"
                Toast.makeText(this, "Falta el campo longitud", Toast.LENGTH_SHORT).show()

            }

            var botn:Button=findViewById(R.id.crearbtn)
            botn.error = ""
            descripcionn.text.clear()
            latitudd.text.clear()
            longitudd.text.clear()
            nombrequedadaa.text.clear()
        }else{
        val email3= intent.getStringExtra("email")
        val partcnom= email3
        var participantes = ArrayList<String>()
        participantes.add(partcnom as String)
        val nombrecreador= intent.getStringExtra("name")

        val quedada=hashMapOf(
            "descripcion" to descripcion,
            "latitud" to latitud,
            "longitud" to longitud,
            "nombre" to nombrequedada,
            "participantes" to participantes,
            "activaBool" to true,
            "nombrecreador" to nombrecreador
            //"hora" to Timestamp() ,
        )
        db.collection("quedadas").document(nombrequedada)
            .set(quedada)
            .addOnSuccessListener {
                descripcionn.text.clear()
                findViewById<EditText>(R.id.inputLatitud).text.clear()
                findViewById<EditText>(R.id.inputLong).text.clear()
                nombrequedadaa.text.clear()
                Toast.makeText(this,"Se guardo correctamente",Toast.LENGTH_LONG).show()
                Log.d("TAG","se guardo") }
            .addOnFailureListener{e->
                Toast.makeText(this,"Error: $e",Toast.LENGTH_LONG)
                Log.d("TAG", "error$e")}


    }}
}