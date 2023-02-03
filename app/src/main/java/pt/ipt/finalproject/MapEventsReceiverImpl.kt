package pt.ipt.finalproject

import android.util.Log
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.util.GeoPoint

class MapEventsReceiverImpl: MapEventsReceiver {

    var positionla: Double = 0.0
    var positionlo: Double = 0.0
    lateinit var selectedPoint: GeoPoint

    override fun singleTapConfirmedHelper(p: GeoPoint?): Boolean {
        Log.d("singleTapConfirmedHelpe", "${p?.latitude} - ${p?.longitude}")
        positionla = p?.latitude!!
        positionlo =  p?.longitude!!
        onPointSelected(p);
        return true
    }

    override fun longPressHelper(p: GeoPoint?): Boolean {
        Log.d("longPressHelper", "${p?.latitude} - ${p?.longitude}")
        positionla = p?.latitude!!
        positionlo =  p?.longitude!!
        return false
    }

    private fun onPointSelected(p: GeoPoint) {
        //do whatever you want with the point. For example: store it in a field
        this.selectedPoint = p
    }
}