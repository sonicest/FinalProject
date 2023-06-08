package pt.ipt.finalproject

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_map.maap
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.compass.CompassOverlay
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import pt.ipt.finalproject.databinding.ActivityMapBinding
import pt.ipt.finalproject.utilities.Constant


class MapActivity : AppCompatActivity(), LocationListener {

    private val REQUEST_PERMISSIONS_REQUEST_CODE = 1;
    private val LOCATION_PERMISSION_CODE = 2;

    private lateinit var locationManager: LocationManager
    private lateinit var binding: ActivityMapBinding
    private lateinit var map: MapView
    private lateinit var positionll: GeoPoint
    private lateinit var infoTextView: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
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
        getLocation()
        showMap()
        setListener()

    }

    //add the OpenStreetMap to activity
    private fun showMap() {

        basicAlert(
            "Your task for today",
            "Visit a new place and track it on the map.\nNotice what you have felt and note it down.",
            false,
            "Sure"
        )
        //defines only one map in all project (using singleton pattern)
        Configuration.getInstance().setUserAgentValue(this.getPackageName())

        map = maap
        map.setTileSource(TileSourceFactory.MAPNIK)
        map.controller.zoomTo(17.0)
        map.zoomController.setVisibility(CustomZoomButtonsController.Visibility.ALWAYS)
        map.setMultiTouchControls(true) // para poder fazer zoom com os dedos

        var compassOverlay = CompassOverlay(this, map)
        compassOverlay.enableCompass()
        map.overlays.add(compassOverlay)

        var myLocationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(this), map)
        myLocationOverlay.enableMyLocation()
        map.overlays.add(myLocationOverlay)

        var currentLocation = myLocationOverlay.myLocation
        if (currentLocation != null) {
            val latLng = GeoPoint(currentLocation.latitude, currentLocation.longitude)
            positionll = currentLocation
            map.controller.setCenter(latLng)
        }
        Handler(Looper.getMainLooper()).postDelayed({
            map.controller.setCenter(currentLocation)
        }, 1000) // waits one second to center map

        val tapOverlay = MapEventsOverlay(object : MapEventsReceiver {
            override fun singleTapConfirmedHelper(p: GeoPoint?): Boolean {
                // Do whatever
                return true
            }


            override fun longPressHelper(p: GeoPoint?): Boolean {

                val point2 = GeoPoint(p)

                if (basicAlert(
                        "Question",
                        "Do you want to save this place?",
                        true,
                        "No"
                    )
                ) {

                    val startMarker2 = Marker(map)
                    startMarker2.setIcon(resources.getDrawable(R.drawable.ic_circle))
                    startMarker2.position = point2
                    startMarker2.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
                    map.overlays.add(startMarker2)

                    Log.d("Place position", point2.toString())
                    startMarker2.id = "last"
                    /** зберегти ці дані**/

                }
                return true
            }
        })
        map.overlays.add(tapOverlay)

    }

    fun basicAlert(name: String, desc: String, boolean: Boolean, button: String): Boolean {
        val builder = AlertDialog.Builder(this, R.style.AlertDialog)
        with(builder)
        {
            setTitle(name)
            setMessage(desc)
            setNegativeButton(button, negativeButtonClick)
            if (boolean) {
                setPositiveButton("Yes", positiveButtonClick)
            }
            show()
            return false
        }
    }

    val negativeButtonClick = { dialog: DialogInterface, which: Int ->
        dialog.dismiss()
        centerMap()
    }

    val positiveButtonClick = { dialog: DialogInterface, which: Int ->
        openF()
    }

    fun openF() {
        Intent(applicationContext, EmotionsActivity::class.java).also {
            startActivity(it)
        }
    }

    override fun onPause() {
        super.onPause()
        map.onPause()
    }

    override fun onResume() {
        super.onResume()
        map.onResume()
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
            );
        }
    }

    private fun getLocation() {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        // if ((ContextCompat.checkSelfPermission(
        if ((ActivityCompat.checkSelfPermission(
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

    //the function is called when the location is changed
    override fun onLocationChanged(location: Location) {
        positionll = GeoPoint(location.latitude, location.longitude)
    }

    private fun setListener() {
        binding.myloc.setOnClickListener {
            getLocation()
            centerMap()
        }
    }

    private fun centerMap() {
        val latLng = GeoPoint(positionll.latitude, positionll.longitude)
        map.controller.setCenter(latLng)
        map.controller.zoomTo(17.0)
    }

}
