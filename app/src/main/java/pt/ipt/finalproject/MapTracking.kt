package pt.ipt.finalproject

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_map_tracking.*
import kotlinx.android.synthetic.main.activity_map_tracking.view.*
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline
import org.osmdroid.views.overlay.compass.CompassOverlay
import pt.ipt.finalproject.databinding.ActivityMainBinding
import pt.ipt.finalproject.databinding.ActivityMapTrackingBinding

class MapTracking : AppCompatActivity() {

    private val REQUEST_PERMISSIONS_REQUEST_CODE = 1;
    private lateinit var map: MapView
    private lateinit var binding: ActivityMapTrackingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapTrackingBinding.inflate(layoutInflater)
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

        //define the point of the map(ipt)
        var point = GeoPoint(39.60068, -8.38967)       // 39.60199, -8.39675
        //define a marker to a point
        var startMarker = Marker(map)
        //assign a point to the marker
        startMarker.position = point
        //tell marker that the marker is there
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
        //define the content of infoWindow
        startMarker.infoWindow = MapInfoWindow(map, this, "IPT")
        //add the marker to the map
        map.overlays.add(startMarker)

        //defiine a second point
        //define the point of the map(ipt)
        var point2 = GeoPoint(40.60068, -8.38967)       // 39.60199, -8.39675
        var startMarker2 = Marker(map)
        startMarker2.position = point2
        startMarker2.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
        map.overlays.add(startMarker2)


        //draw a line between the two points
        val geoPoints = ArrayList<GeoPoint>();
        geoPoints.add(point)
        geoPoints.add(point2)
        val line = Polyline()
        line.setPoints(geoPoints);
        line.setOnClickListener { polyline: Polyline, mapView: MapView, geoPoint: GeoPoint ->
            Toast.makeText(
                mapView.context,
                "polyline with " + line.actualPoints.size + " pts was tapped",
                Toast.LENGTH_LONG
            ).show()
            false
        };
        map.overlays.add(line);

        Handler(Looper.getMainLooper()).postDelayed({
            map.controller.setCenter(point)

        }, 1000) // waits one second to center map


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
            )
        }
    }
}