package com.example.taskapp.ui.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import android.location.Location
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import com.example.taskapp.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsFragment : Fragment() {
    private lateinit var currentLocation: Location
    private val permissionCode = 101
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        val latLng = LatLng(currentLocation.latitude, currentLocation.longitude)
        val markerOptions = MarkerOptions().position(latLng).title("I am here!")
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng))
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 5f))
        googleMap.addMarker(markerOptions)
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        fusedLocationProviderClient =  LocationServices.getFusedLocationProviderClient(requireContext())
        val v = inflater.inflate(R.layout.fragment_maps, container, false)
        val toolbar=v.findViewById<ConstraintLayout>(R.id.map_include).findViewById<Toolbar>(R.id.toolbar)
        val activity=activity as AppCompatActivity
        activity.setSupportActionBar(toolbar)
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setHasOptionsMenu(true)
        toolbar.setNavigationOnClickListener{
            activity.onBackPressed()
        }
        return v
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.main_menu,menu)
    }
    override fun onResume() {
        super.onResume()
        permissionCheck()
    }
    private fun permissionCheck() {
        if (context?.applicationContext?.let {
                    ActivityCompat.checkSelfPermission(
                            it, Manifest.permission.ACCESS_FINE_LOCATION)
                } !=
                PackageManager.PERMISSION_GRANTED && context?.let {
                    ActivityCompat.checkSelfPermission(
                            it, Manifest.permission.ACCESS_COARSE_LOCATION)
                } !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity as Activity,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), permissionCode)
            return
        }
        else{
            fetchLocation()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if(requestCode==permissionCode&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
            fetchLocation()
        }
        else
        {
            Toast.makeText(context,"Permission denied",Toast.LENGTH_LONG).show()
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
    @SuppressLint("MissingPermission")
    private fun fetchLocation() {
        val task = fusedLocationProviderClient.lastLocation

        task.addOnSuccessListener {
            if (it != null) {
                currentLocation = it

                val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
                mapFragment?.getMapAsync(callback)
            }
        }
    }

}