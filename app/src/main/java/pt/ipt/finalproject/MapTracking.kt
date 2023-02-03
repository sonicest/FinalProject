package pt.ipt.finalproject

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_map_tracking.*
import kotlinx.android.synthetic.main.saved_pic_layout.view.*
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.OverlayItem
import org.osmdroid.views.overlay.compass.CompassOverlay
import pt.ipt.finalproject.databinding.ActivityMapTrackingBinding


class MapTracking : AppCompatActivity(), LocationListener {


    private val REQUEST_PERMISSIONS_REQUEST_CODE = 1;
    private val LOCATION_PERMISSION_CODE = 2

    private lateinit var locationManager: LocationManager
    private lateinit var map: MapView
    private lateinit var binding: ActivityMapTrackingBinding
    private lateinit var positionll: Pair<Double, Double>
    private lateinit var inemotions: String


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMapTrackingBinding.inflate(layoutInflater)
        positionll = Pair(0.0, 0.0)
        inemotions = "How are you feeling?"


        //setContentView(R.layout.activity_main)
        setContentView(binding.root)
        requestPermissionIfNessesary(
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        )
        //add the OpenStreetMap to activity
        showMap()
        setListener()


    }

    private fun getLocation() {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if ((ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED)
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_CODE
            )
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0.1f, this)
    }

    override fun onLocationChanged(location: Location) {

        positionll = Pair(location.latitude, location.longitude)
        // tvGpsLocation = findViewById(R.id.textView)
        // tvGpsLocation.text = "Latitude: " + location.latitude + " , Longitude: " + location.longitude
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showPosition() {


        // define the point of the map(ipt)
        var point = GeoPoint(positionll.first, positionll.second)
        // var point = GeoPoint(37.421998333333335 , -122.084)


        //define a marker to a point
        var startMarker = Marker(map)
        //assign a point to the marker
        startMarker.position = point
        //tell marker that the marker is there
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
        //define the content of infoWindow
        startMarker.infoWindow = MapInfoWindow(map, this, inemotions)
        //add the marker to the map
        map.overlays.add(startMarker)


        //   Handler(Looper.getMainLooper()).postDelayed({
        map.controller.setCenter(point)
        // }, 1000) // waits one second to center map

    }


    //add the OpenStreetMap to activity
    private fun showMap() {
        //defines only one map in all project (using singleton pattern)
        Configuration.getInstance().userAgentValue = this.packageName

        map = mapp
        map.setTileSource(TileSourceFactory.MAPNIK)
        map.controller.zoomTo(17.0)
        map.zoomController.setVisibility(CustomZoomButtonsController.Visibility.ALWAYS)
        map.setMultiTouchControls(true) // para poder fazer zoom com os dedos

        var compassOverlay = CompassOverlay(this, map)
        compassOverlay.enableCompass()
        map.overlays.add(compassOverlay)


        //defiine a second point
        //define the point of the map(ipt)
//        var point2 = GeoPoint(40.60068, -8.38967)       // 39.60199, -8.39675
//        var startMarker2 = Marker(map)
//        startMarker2.position = point2
//        startMarker2.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
//        map.overlays.add(startMarker2)
//
//
//        //draw a line between the two points
//        val geoPoints = ArrayList<GeoPoint>();
//        geoPoints.add(point)
//        geoPoints.add(point2)
//        val line = Polyline()
//        line.setPoints(geoPoints);
//        line.setOnClickListener { polyline: Polyline, mapView: MapView, geoPoint: GeoPoint ->
//            Toast.makeText(
//                mapView.context,
//                "polyline with " + line.actualPoints.size + " pts was tapped",
//                Toast.LENGTH_LONG
//            ).show()
//            false
//        };
//        map.overlays.add(line);


    }


    override fun onPause() {
        super.onPause()
        map.onPause()
    }

    override fun onResume() {
        super.onResume()
        map.onResume()
    }

    private fun setListener() {

        binding.mapp.setOnClickListener {
            val mapEventsReceiver = MapEventsReceiverImpl()
            val mapEventsOverlay = MapEventsOverlay(mapEventsReceiver)
            map.overlays.add(mapEventsOverlay)
            Log.d("point", mapEventsReceiver.selectedPoint.latitude.toString())
        }



        binding.myloc.setOnClickListener {
//            val overlaySize = map.overlays.size
//            for (i in 0 until overlaySize) {
//                map.overlays.removeAt(i)
//            }
            getLocation()
            showPosition()
            map


            //   Intent(applicationContext, MapPopUp::class.java).also {
            // startActivityForResult(it, Activity.RESULT_OK)
//                val bundle = intent.extras
//                val mes = bundle!!.getString("message")
//                if (!mes.equals(null)) {
//                    inemotions = mes.toString()
//                } else
//                    inemotions = ""
            // }

//            var point = GeoPoint(39.60068, -8.38967)       // 39.60199, -8.39675
//            //define a marker to a point
//            var startMarker = Marker(map)
//            //assign a point to the marker
//            startMarker.position = point
//            //tell marker that the marker is there
//            startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
//            //define the content of infoWindow
//            startMarker.infoWindow = MapInfoWindow(map, this, "How were you feeling?")
//            //add the marker to the map
//            map.overlays.add(startMarker)
//            Intent(applicationContext, MapTracking::class.java).also {
//                startActivity(it)
//            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == resultCode) {
            inemotions = data?.getStringExtra("KEY").toString()
        }
    }

    //function to collect users' permission
    private fun requestPermissionIfNessesary(permissions: Array<out String>) {
        val permissionsToRequest = ArrayList<String>();
        permissions.forEach { permission ->
            if (ContextCompat.checkSelfPermission(
                    this,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // Permission is not granted
                permissionsToRequest.add(permission);
            }
        }
        if (permissionsToRequest.size > 0) {
            ActivityCompat.requestPermissions(
                this,
                permissionsToRequest.toArray(arrayOf<String>()),
                REQUEST_PERMISSIONS_REQUEST_CODE
            )
        }
    }

}