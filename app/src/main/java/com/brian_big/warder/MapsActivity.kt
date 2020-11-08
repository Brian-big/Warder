package com.brian_big.warder

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import java.util.*
import android.Manifest

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private val REQUEST_LOCATION_PERMISSION: Int = 1
    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val nairobi = LatLng(-1.308028, 36.823632)
        val zoom = 10.0F
        mMap.addMarker(MarkerOptions().position(nairobi).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(nairobi, zoom))
        setMapLongClick(mMap)
        setPoiListener(mMap)

        val homeOverlay = GroundOverlayOptions()
            .image(BitmapDescriptorFactory.fromResource(R.drawable.android))
            .position(nairobi, 100.0F)
        mMap.addGroundOverlay(homeOverlay)
        enableMyLocation()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.normal_map -> {
                mMap.mapType = GoogleMap.MAP_TYPE_NORMAL;
                return true
            }
            R.id.hybrid_map ->{
                mMap.mapType = GoogleMap.MAP_TYPE_HYBRID
                return true
            }
            R.id.satellite_map ->{
                mMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
                return true
            }
            R.id.terrain_map ->{
                mMap.mapType = GoogleMap.MAP_TYPE_TERRAIN
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
    private fun setMapLongClick(map: GoogleMap){
        map.setOnMapLongClickListener() {
            val snippet = String().format(Locale.getDefault(),
                "Lat: %1$.5f, Long: %2$.5f",
                it.latitude, it.longitude)
            map.addMarker(MarkerOptions().position(it)
                .title("Dropped Pin")
                .snippet(snippet))
                .setIcon(BitmapDescriptorFactory.defaultMarker
                    (BitmapDescriptorFactory.HUE_YELLOW))
        }
    }
    private fun setPoiListener(map: GoogleMap) {
        map.setOnPoiClickListener(){
            val poiMarker = map.addMarker(
                MarkerOptions()
                    .position(it.latLng)
                    .title(it.name)
            )
            poiMarker.showInfoWindow()
        }

    }
    private fun enableMyLocation(){
        if (ContextCompat.checkSelfPermission(this,
            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            mMap.isMyLocationEnabled = true
        else
            ActivityCompat.requestPermissions(this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION_PERMISSION)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            REQUEST_LOCATION_PERMISSION ->
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    enableMyLocation()
                }
        }
    }
}