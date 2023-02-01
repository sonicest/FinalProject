package pt.ipt.finalproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_map_info_window.*
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.infowindow.InfoWindow
import pt.ipt.finalproject.databinding.ActivityMapInfoWindowBinding
import pt.ipt.finalproject.databinding.ActivityMapTrackingBinding

class MapInfoWindow : InfoWindow {
    private var parent: MapTracking
    private var text: String
    private lateinit var binding: ActivityMapInfoWindowBinding

    constructor(
        mapView: MapView,
        parent: MapTracking,
        text: String
    ) :  super(R.layout.activity_map_info_window, mapView) {
        this.parent = parent
        this.text = text
    }

    override fun onOpen(item: Any?) {
        //close all windows at beginning
        closeAllInfoWindowsOn(mapView)

        //access to button and textView
        val myHelloButton = mView.findViewById<Button>(R.id.hellobt)
        val myTextView = mView.findViewById<TextView>(R.id.textView)

        //define value to textView
        myTextView.text = text

        //define what happens when we click on button
        myHelloButton.setOnClickListener {
            Toast.makeText(
                parent,
                "Hi",
                Toast.LENGTH_SHORT
            ).show()
        }

        //when we click on the area out of marker
        mView.setOnClickListener {
            close()
        }
    }

    override fun onClose() {
        TODO("Not yet implemented")
    }
}