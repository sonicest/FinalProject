package pt.ipt.finalproject

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.Settings.Global.putString
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_chosen_photo.*
import pt.ipt.finalproject.databinding.ActivityMapPopUpBinding

class MapPopUp : AppCompatActivity() {
    private lateinit var binding: ActivityMapPopUpBinding
    private lateinit var inEmotions: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapPopUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupSpinner()
        setListeners()
    }

    private fun setupSpinner() {
        ArrayAdapter.createFromResource(
            this,
            R.array.emotions,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            emSpinner.adapter = adapter
        }

        emSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                val selectedItem = parent!!.getItemAtPosition(pos).toString()
                if (inputEmotions.isEmpty())
                    inputEmotions.append(selectedItem)
                else inputEmotions.append(", $selectedItem")
                inEmotions = selectedItem
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
    }

    private fun setListeners() {
        binding.saveb.setOnClickListener {
            // filteredBitmap.value?.let{bitmap->
            if (inputEmotions.isEmpty()) {
                Toast.makeText(
                    applicationContext,
                    "Please try to distinguish your feelings",
                    Toast.LENGTH_SHORT
                ).show()
            } else
                Intent(applicationContext, MapTracking::class.java).also {
                    intent.putExtra("messaage", inEmotions)
                    setResult(Activity.RESULT_OK, intent);
                    startActivity(it)
                }

        }
    }

    private fun EditText.isEmpty(): Boolean {
        val etMessage = findViewById<EditText>(R.id.inputEmotions)
        val msg: String = etMessage.text.toString()
        //check if the EditText have values or not
        return msg.trim().isEmpty()
    }
}