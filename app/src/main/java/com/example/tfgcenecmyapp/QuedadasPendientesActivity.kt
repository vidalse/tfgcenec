package com.example.tfgcenecmyapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import com.example.tfgcenecmyapp.clases.Quedada
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.w3c.dom.Text

class QuedadasPendientesActivity : AppCompatActivity() {
    private val db = Firebase.firestore
    private lateinit var quedadapend:Quedada
    private  lateinit var listviee:ListView
    private lateinit var mAdapter:ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quedadas_pendientes)

        var mlistaa:List<String>  = ArrayList<String>()

        val email= intent.getStringExtra("email") as Any
        val displayName= intent.getStringExtra("name")
        findViewById<Button>(R.id.btnAtrss).setOnClickListener {
                val email= intent.getStringExtra("email")
                val name= intent.getStringExtra("name")

                val intent2= Intent(this, MapsActivity::class.java)
                intent2.putExtra("email", email)
                intent2.putExtra("name", name)

                startActivity(intent2) }

        listviee=findViewById(R.id.milist)
        db.collection("quedadas")
            .whereArrayContains("participantes",email)
            .whereEqualTo("activaBool",true)
            .addSnapshotListener { value, e ->
                if(e!=null){
                    Log.d("TAG", "la escucha fue fallida")
                    return@addSnapshotListener
                }

                val nombres = ArrayList<String>()
                for(doc in value!!){
                    doc.getString("nombre")?.let{
                        nombres.add(it)
                    }
                }
                Log.d("TAG","nombres quedadas pendientes $nombres")

                val descripciones = ArrayList<String>()
                for(doc in value!!){
                    doc.getString("descripcion")?.let{
                        descripciones.add(it)
                    }
                }
                Log.d("TAG","Descripciones quedadas pendientes $descripciones")
                val booleanosLat = ArrayList<Number>()

                for(doc in value!!){
                    doc.getDouble("latitud")?.let{
                        booleanosLat.add(it)
                    }
                }
                Log.d("TAG","latitudes agregadas $booleanosLat")


                val booleanosLong = ArrayList<Number>()
                for(doc in value!!){
                    doc.getDouble("longitud")?.let{
                        booleanosLong.add(it)
                    }
                }
                Log.d("TAG","longitudes agregadas $booleanosLong")

                //val participantes:ArrayList<String>

                val arrayProb:ArrayList<ArrayList<String>> =ArrayList<ArrayList<String>>()

                for(doc in value!!){
                    doc.get("participantes")?.let{ it as ArrayList<String>
                        arrayProb.add(it)


                    }
                }
                Log.d("TAG","array de arraylist de participantes $arrayProb")

                var activaBool = ArrayList<Boolean>()
                for(doc in value!!){
                    doc.getBoolean("activaBool")?.let{
                        activaBool.add(it)
                    }
                }
                Log.d("TAG","valores boolean activos quedadas $activaBool")

                val nombrescreadores = ArrayList<String>()
                for(doc in value!!){
                    doc.getString("nombrecreador")?.let{
                        nombrescreadores.add(it)
                    }
                }
                Log.d("TAG","nombres creadores quedadas pendientes $nombrescreadores")
                val arrayListQuedadas = ArrayList<Quedada>()
                val helpss:ArrayList<String>  = ArrayList()

                for (i in 0 .. nombrescreadores.size-1){

                    val olaa:ArrayList<String> = mlistaa as ArrayList<String>

                    quedadapend=Quedada(nombres[i],descripciones[i],booleanosLat[i] as Double,booleanosLong[i] as Double,arrayProb[i],activaBool[i],nombrescreadores[i])
                    val probstin:String="Titulo: "+nombres[i]+"\nDescripcion: "+descripciones[i]+"\nLatitud: "+booleanosLat[i]+"\nLongitud: "+booleanosLong[i]+"\nParticipantes: "+arrayProb[i]+"\nCreador: "+nombrescreadores[i]
                    olaa.add(nombres[i])
                    helpss.add(probstin)
                    mlistaa=olaa

                    arrayListQuedadas.add(quedadapend)
                    Log.d("TAG","quedadas pendientes $probstin")

                    Log.d("TAG","quedadas pendientes $arrayListQuedadas")

                }
                for(i in 0 .. helpss.size-1){
                    val ola:String=helpss[i]
                    Log.d("TAG","helpsss $ola")

                }
                Log.d("TAG","quedadas pendientes nombre $mlistaa")
                mAdapter= ArrayAdapter(this,android.R.layout.simple_list_item_1,mlistaa)
                listviee.setAdapter(mAdapter)
                var textoxd:TextView=findViewById(R.id.txtHelps)

                listviee.setOnItemClickListener { parent, view, position, id ->
                var toast:Toast = Toast.makeText(this,helpss[position],Toast.LENGTH_LONG)
                    toast.show()
                    textoxd.setText(helpss[position])
                }

                Log.d("TAG","quedadas pendientes $arrayListQuedadas")

            }

    }
}