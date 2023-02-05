package pt.ipt.finalproject

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_chosen_photo.*
import kotlinx.android.synthetic.main.activity_main.*
import pt.ipt.finalproject.adapters.MenuArrayAdapter
import pt.ipt.finalproject.databinding.ActivityMainBinding
import pt.ipt.finalproject.utilities.Constant
import pt.ipt.finalproject.utilities.Constant.Companion.helper
import pt.ipt.finalproject.utilities.MenuList
import pt.ipt.finalproject.utilities.displayToast

class MainActivity : AppCompatActivity() {
    companion object {
        private const val REQUEST_CODE_PICK_IMAGE = 1
        const val KEY_IMAGE_URI = "imageUri"
        var appContext: Context? = null
            private set
    }

    private lateinit var binding: ActivityMainBinding

    //Main page
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appContext = applicationContext
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setListener()
        sspinner.visibility = View.INVISIBLE
        setupCustomSpinner()
        // setupSpinner()

    }

    private fun setupCustomSpinner() {
        val adapter = MenuArrayAdapter(this, MenuList.list!!)

        //   adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        sspinner.adapter = adapter

        sspinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                val selectedItem = parent!!.getItemAtPosition(pos).toString()
                val d = selectedItem!!.split("=")[2].split(")")[0]
                if (d == "Log out") {
                    basicAlert(
                        "Log out",
                        "Are you sure you want to log out from the account?",
                        true, "No"
                    )
                } else if (d == "Info") {
                    basicAlert(
                        "Information",
                        "Instituto Politecnico de Tomar\nEngenharia Informatica\nDesenvolvimento de Aplicacoes Movies\nAlunos: 25153, 21075",
                        false,
                        "OK"
                    )
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
    }

    val positiveButtonClick = { dialog: DialogInterface, which: Int ->
        val editor: SharedPreferences.Editor = Constant.sharedPreferences.edit()
        editor.putBoolean(Constant.IS_LOGGED_KEY, false)
        editor.apply()
        Intent(applicationContext, UserAuthentication::class.java).also {
            startActivity(it)
        }
        finish()
    }
    val negativeButtonClick = { dialog: DialogInterface, which: Int ->
//        Toast.makeText(
//            applicationContext,
//            android.R.string.no, Toast.LENGTH_SHORT
//        ).show()
        dialog.cancel()
    }

    fun basicAlert(name: String, desc: String, boolean: Boolean, button: String) {
        val builder = AlertDialog.Builder(this)
        with(builder)
        {
            setTitle(name)
            setMessage(desc)
            setNegativeButton(button, negativeButtonClick)
            if (boolean) {
                setPositiveButton(
                    "Yes",
                    DialogInterface.OnClickListener(function = positiveButtonClick)
                )
            }
            show()
        }
    }

    private fun setListener() {
        binding.buttonCamera.setOnClickListener {
            Intent(applicationContext, CameraActivity::class.java).also {
                startActivity(it)
            }
        }

        binding.acc.setOnClickListener {
            sspinner.performClick();
        }
    }
}