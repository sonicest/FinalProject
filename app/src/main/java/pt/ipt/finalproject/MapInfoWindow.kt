package pt.ipt.finalproject

import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.net.toUri
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.infowindow.InfoWindow
import pt.ipt.finalproject.databinding.ActivityMapInfoWindowBinding

class MapInfoWindow : InfoWindow {
    private var parent: MapTracking
    private var text: String
    private var img: String
    private var date: String

    constructor(
        mapView: MapView,
        parent: MapTracking,
        text: String,
        img: String,
        date: String
    ) : super(R.layout.activity_map_info_window, mapView) {
        this.parent = parent
        this.text = text.lowercase()
        this.img = img
        this.date = date
    }

    override fun onOpen(item: Any?) {
        //close all windows at beginning
        closeAllInfoWindowsOn(mapView)

        //access to button and textView
        val myImg = mView.findViewById<ImageView>(R.id.img)
        val myTextView = mView.findViewById<TextView>(R.id.textView)

        //define value to textView
        myTextView.text = date
        myImg.setImageURI(img.toUri())
        text.replace(", $", "");

        //define what happens when we click on button
        myImg.setOnClickListener {
            Toast.makeText(
                parent,
                "You were feeling $text.",
                Toast.LENGTH_SHORT
            ).show()
        }

        //when we click on the area out of marker
        mView.setOnClickListener {
            close()
        }
    }

    override fun onClose() {
        closeAllInfoWindowsOn(mapView)
    }
}