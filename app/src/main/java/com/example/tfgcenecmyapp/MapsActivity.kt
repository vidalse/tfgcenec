package com.example.tfgcenecmyapp


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tfgcenecmyapp.clases.Quedada
import com.example.tfgcenecmyapp.clases.QuedadaModel
import com.example.tfgcenecmyapp.databinding.ActivityMapsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var auth: FirebaseAuth
    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private val db = Firebase.firestore
    private val senderismo = LatLng(39.0, -4.0)
    private var markerSenderismo: Marker? = null


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth= FirebaseAuth.getInstance()

        val email= intent.getStringExtra("email")
        val displayName= intent.getStringExtra("name")

        findViewById<TextView>(R.id.usertxt).text= "Welcome again, " + intent.getStringExtra("name") +"\n" + "mail: " +email

        findViewById<Button>(R.id.signoutbutton).setOnClickListener {
            auth.signOut()
            startActivity(Intent(this,MainActivity::class.java))
        }


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val email= intent.getStringExtra("email")
        val name= intent.getStringExtra("name")

        leer()

        mMap.setOnInfoWindowLongClickListener { marker: Marker ->
            val titulomarker:String=marker.title as String
            val marker:Marker=marker
            borrar(titulomarker,marker)

            var olaa:String=marker.title as String
            quienParticipa(olaa)
            // on below line we are displaying a toast message on clicking on marker
            Log.d("TAG","clickeando ${marker.title} ______  $olaa")

            val nombre= intent.getStringExtra("name")



        }
        findViewById<FloatingActionButton>(R.id.fabAñadirQuedada).setOnClickListener {
            val intent=Intent(this, CreateQuedadaActivity::class.java)
            intent.putExtra("email", email)
            intent.putExtra("name", name)

            startActivity(intent)

        }
        findViewById<FloatingActionButton>(R.id.fabQuedadasPendientes).setOnClickListener {
            val intent=Intent(this, QuedadasPendientesActivity::class.java)
            intent.putExtra("email", email)
            intent.putExtra("name", name)

            startActivity(intent)

        }
    }

    private fun borrar(titulomarker:String,marker: Marker) {
        findViewById<Button>(R.id.boton).setOnClickListener {
            db.collection("quedadas").document(titulomarker).get().addOnCompleteListener { it1 ->
                if(it1.isSuccessful){
                    val c= it1.result
                    if(c.exists()){
                        val creador:String? = c.getString("nombrecreador") as String
                        val email= intent.getStringExtra("email")
                        val nombre= intent.getStringExtra("name")
                        if(creador==nombre){
                            db.collection("quedadas").document(titulomarker)
                                .delete().addOnFailureListener { e ->
                                    Log.d("TAG","no se pudo eliminar, debido a : $e")
                                    Toast.makeText(this,"$e",Toast.LENGTH_LONG)


                                }.addOnSuccessListener {
                                    Toast.makeText(this,"Se elimino la quedada correctamente",Toast.LENGTH_LONG)
                                    Log.d("TAG","Se elimino la quedada de la base de datos exitosamente")
                                }
                            marker.remove()
                        }else{
                            Log.d("TAG","esta no es tu quedada bro")
                            Toast.makeText(this,"Usted no es el creador de esta quedada.",Toast.LENGTH_LONG)
                        }
                    }
                }
            }.addOnFailureListener {
                Log.d("TAG","fuckkkkked")

            }

        }
    }

    private fun quienParticipa(ola:String) {
        findViewById<Button>(R.id.botonAnad).setOnClickListener {
        db.collection("quedadas").document(ola).get().addOnCompleteListener { it1 ->
            if(it1.isSuccessful){
                val c= it1.result
                if(c.exists()){
                    val participantes:ArrayList<String>? = c.get("participantes") as ArrayList<String>?
                    val longitud:Double? = c.getDouble("longitud")
                    val latitud:Double? = c.getDouble("latitud")
                    val nombre:String? = c.getString("nombre")
                    val descripcion:String? = c.getString("descripcion")
                    val nombrecreador:String?= c.getString("nombrecreador")

                   val activaBool:Boolean?=c.getBoolean("activaBool")
                    val nombree= intent.getStringExtra("email") as String
                    if (participantes != null) {
                        if(participantes.contains(nombree)){
                            Log.d("TAG","Usted ya forma parte de esta quedada")
                            Toast.makeText(this,"Usted ya forma parte de esta quedada",Toast.LENGTH_LONG)
                        }else{
                            Toast.makeText(this,"Añadido a quedada: $nombree",Toast.LENGTH_LONG)

                            participantes?.add(nombree)
                        }
                    }
                    val quedadad= QuedadaModel(nombre,descripcion,latitud,longitud,participantes,activaBool,nombrecreador)
                    db.collection("quedadas").document(ola).set(quedadad)
                    Log.d("TAG","participants $participantes$longitud$latitud$nombre$descripcion$quedadad")

                }else{
                    Log.d("TAG","mal vamoss--------------------------36.719444 ")

                }
            }
        }.addOnSuccessListener {
            Log.d("TAG","bien o bien pero mal --------------------")
        }.addOnFailureListener {
            Log.d("TAG","mal --------------------------$it")
        }
    }}

    private fun leer(){
    db.collection("quedadas")
        .whereEqualTo("activaBool",true)
        .addSnapshotListener{ value, e->

            if(e!=null){
                Log.d("TAG", "la escucha fue fallida")
                return@addSnapshotListener
            }

            val booleanosLat = ArrayList<Number>()

            for(doc in value!!){
                doc.getDouble("latitud")?.let{
                    booleanosLat.add(it)
                }
            }
            Log.d("TAG","latitudes agregadas $booleanosLat")

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
            val creadores = ArrayList<String>()
            for(doc in value!!){
                doc.getString("nombrecreador")?.let{
                    creadores.add(it)
                }
            }
            Log.d("TAG","Descripciones quedadas pendientes $descripciones")

            val booleanosLong = ArrayList<Number>()
            for(doc in value!!){
                doc.getDouble("longitud")?.let{
                    booleanosLong.add(it)
                }
            }
            Log.d("TAG","longitudes agregadas $booleanosLong")

            for(i in 0..booleanosLat.size-1) {
                val sydney = LatLng(booleanosLat[i] as Double, booleanosLong[i] as Double)
                val displayName= intent.getStringExtra("name")
                db.collection("quedadas").document()
                mMap.addMarker(
                    MarkerOptions()
                        .position(sydney)
                        .title(nombres[i])
                        .snippet(descripciones[i] + "    creado por: " + creadores[i])
                )?.showInfoWindow()

                val ola:String=nombres[i]
                val olaa:String=descripciones[i]

                Log.d("TAG","$ola$olaa")

                val sydney1 = LatLng(41.0 as Double, 20.0 as Double)

                mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney1))

            }
        }







/* FUNCIONA PARA LEER UN DOCUMENTO ESPECIFICO
        val docRef = db.collection("quedadas").document("probandooo")
        //    \/ get(source).
        docRef.addSnapshotListener { snapshot, e ->
            if(e != null){
                Log.d("TAG","escucha fallida $e")
                return@addSnapshotListener
            }
            if(snapshot!=null && snapshot.exists()){
                Log.d("TAG","los datos son: ${snapshot.data}")
            }else{
                Log.d("TAG","no hay datos en la escucha")
            }

        }
        // Add a marker in Sydney and move the camera  */



    }


}
//.icon(BitmapDescriptorFactory.fromResource(R.drawable.img))




////pruebas leer
//direccion del documento
/*
val docRef= db.collection("quedadas")
    .get()
    .addOnSuccessListener {result ->
        for(document in result) {
            val datosQue:Array<Double> = db.collection("quedadas")("longitud").get()
            Log.d("TAG","${document.id} => ${document.data}")
        }
    }
    .addOnFailureListener { exception ->
        Log.d("TAG", "error al sacar los documentos $exception")
    }*/
/*val docRef = db.collection("quedadas")
docRef.addSnapshotListener { snapshot, e ->
    if(e != null){
        Log.d("TAG","escucha fallida $e")
        return@addSnapshotListener
    }

    if(snapshot != null ){ //&& snapshot.exists()
        Log.d("TAG","Los datos son : ${snapshot.metadata}")
    }else{
        Log.d("TAG","no hay datos")
    }
}*//////////

/*var latitudE:Double = 0.2
val numberLa:Number?= quedadad?.latitud     //// <<<------------------------------------------
if (numberLa != null) {
    latitudE = numberLa.toDouble()
}
var longitudE:Double = 0.2
val numberLo:Number?= quedadad?.latitud
if (numberLo != null) {
    longitudE = numberLo.toDouble()
}

val malaga = LatLng(latitudE, longitudE)
//mMap.addMarker(MarkerOptions().position(malaga).title(quedadad?.nombre).snippet(quedadad?.descripcion+quedadad?.participantes.toString()))

mMap.addMarker(MarkerOptions().position(malaga))
mMap.moveCamera(CameraUpdateFactory.newLatLng(malaga))*/

//nMap.moveCamera(CameraUpdateFactory.newLatLng(malaga))

//val source = Source.SERVER