package pt.ipt.finalproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_moments.*
import pt.ipt.finalproject.adapters.MomentsAdapter
import pt.ipt.finalproject.utilities.Constant.Companion.helper

class MomentsActivity : AppCompatActivity() {
    private lateinit var adapter: MomentsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_moments)

        supportActionBar?.title = "Moments"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//geting the moments from the database function and showing in the reclying by adpater
        adapter = MomentsAdapter(helper.getMoments())
        if (adapter.itemCount > 0) {
            rv_list.layoutManager = LinearLayoutManager(this@MomentsActivity)
            rv_list.visibility = View.VISIBLE
            rv_list.adapter = adapter
            tv_list.visibility = View.GONE
        }
        // if there is no items saved will appear a text empty list
        else {
            rv_list.visibility = View.GONE
            tv_list.visibility = View.VISIBLE
            tv_list.text = getString(R.string.empty_list)
        }
    }
}