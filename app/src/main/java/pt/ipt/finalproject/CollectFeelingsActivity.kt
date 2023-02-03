package pt.ipt.finalproject

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_chosen_photo.*
//import kotlinx.android.synthetic.main.activity_collect_feelings.*
//import kotlinx.android.synthetic.main.custom_item_layout.*
import pt.ipt.finalproject.databinding.ActivityChosenPhotoBinding
import pt.ipt.finalproject.utilities.Constant.Companion.helper
import java.text.SimpleDateFormat
import java.util.*

class CollectFeelingsActivity : AppCompatActivity() {

    private lateinit var fileUri: Uri
    private val pickImage = 100
    private var imageUri: Uri? = null
    private lateinit var binding: ActivityChosenPhotoBinding
    private lateinit var mId: String
    private var isUpdate = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChosenPhotoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Collect Feelings"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val id = intent.getStringExtra("id")
        if (id != null) {
            fileUri = Uri.parse(intent.getStringExtra("imgUri"))
            image_preview.setImageURI(fileUri)
            inputEmotions.setText(intent.getStringExtra("description"))
            mId = id
            isUpdate = true

            supportActionBar?.title = "Update Feelings"
            //    btn_saveMoments.text = getString(R.string.update_now)
        }

        setListener()
        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(gallery, pickImage)
        collectMoments()
        setupSpinner()
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
               // textEmotions = selectedItem
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
    }

    private fun setListener() {
        binding.save.setOnClickListener {
            Intent(applicationContext, MomentsActivity::class.java).also {
                startActivity(it)
            }
        }
//        binding.fabShare.setOnClickListener {
//            with(Intent(Intent.ACTION_SEND)) {
//                putExtra(Intent.EXTRA_STREAM, fileUri)
//                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
//                type = "image/*"
//                startActivity(this)
//            }
      //  }
    }

    private fun collectMoments() {
        val description = inputEmotions.text.toString().trim()
        if (isUriEmpty(fileUri))
            showToast("Please select img")
        else if (description.isEmpty())
            showToast("Please try to distinguish your feelings")
        else {
            val date: String = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
            if (isUpdate) {
                helper.deleteMoment(mId)
                helper.saveMoments(fileUri.toString(), description, date)
                showToast("Moments successfully updated")
                //startActivity<MomentsActivity>(finish = true)
            } else {
                helper.saveMoments(fileUri.toString(), description, date)
                showToast("Moment successfully saved")
                // startActivity<MainActivity>(finish = true)
            }
        }
    }

    private fun showToast(s: String) {
        Toast.makeText(
            applicationContext,
            s,
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun isUriEmpty(uri: Uri?): Boolean {
        return uri == null || uri == Uri.EMPTY
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == pickImage) {
            if (data != null) {
                fileUri = data.data!!
            }
            image_preview.setImageURI(fileUri)
        }
    }

    private fun EditText.isEmpty(): Boolean {
        val etMessage = findViewById<EditText>(R.id.inputEmotions)
        val msg: String = etMessage.text.toString()
        //check if the EditText have values or not
        return msg.trim().isEmpty()
    }

//    override fun onSupportNavigateUp(): Boolean {
//        if (isUpdate)
//            startActivity<MomentsActivity>(finish = true)
//        else
//            startActivity<MainActivity>(finish = true)
//        return true
//    }
}