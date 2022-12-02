package com.example.tfgceneclm.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.tfgceneclm.R

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.tfgceneclm.databinding.ActivityMapsBinding
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Marker
import com.google.firebase.auth.FirebaseAuth


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    private val senderismo = LatLng(39.0, -4.0)
    private val actoMusical = LatLng(38.0, -7.0)
    private val deporte = LatLng(42.0, -7.0)

    private var markerSenderismo: Marker? = null
    private var markerActoMusical: Marker? = null
    private var markerDeporte: Marker? = null

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        /* auth = FirebaseAuth.getInstance()

         val email = intent.getStringExtra("email")
         val name = intent.getStringExtra("name")

         findViewById<TextView>(R.id.usertxt).text= email + "\n" + name

         findViewById<Button>(R.id.signoutbutton).setOnClickListener{
             auth.signOut()
             startActivity(Intent(this,MainActivity::class.java))
         }
     }*/
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
        markerSenderismo = mMap.addMarker(
            MarkerOptions()
                .position(senderismo)
                .title("Quedada Senderismo") //.draggable(true)
        )
        markerActoMusical = mMap.addMarker(
            MarkerOptions()
                .position(actoMusical)
                .title("Quedada Acto Musical")
                .draggable(true)
                .snippet("hello")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.letsmeetnmbremodifiedpeque))

            //  .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)),
        )
        markerDeporte = mMap.addMarker(
            MarkerOptions()
                .position(deporte)
                .title("Quedada Deporte") //.draggable(true)
        )


        mMap.moveCamera(CameraUpdateFactory.newLatLng(deporte))
    }
}