package pt.ipt.finalproject.adapters

import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pt.ipt.finalproject.MapTracking
import pt.ipt.finalproject.R
import pt.ipt.finalproject.databinding.ActivityMainBinding
import pt.ipt.finalproject.models.Moment

class MomentsAdapter(private var list: ArrayList<Moment>) :
    RecyclerView.Adapter<MomentsAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.saved_pic_layout, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val pos = list[position]

        holder.apply {
            val cxt = this.view.context

            val id = pos.id
            val imgUri = pos.ImgUri
            val description = pos.description
            val date = pos.date
            //val location = pos.location

            ivImg.setImageURI(Uri.parse(imgUri))
            tvDescription.text = description
            tvDate.text = date

            view.setOnClickListener {
                val intent = Intent(cxt, MapTracking::class.java)
                //   intent.putExtra("id", id)
                intent.putExtra("imgUri", imgUri)
                intent.putExtra("description", description)
                intent.putExtra("date", date)
                intent.putExtra("location", "39.598851380869746, -8.391537079587579")
                cxt.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tvDescription: TextView = itemView.findViewById(R.id.img_desc)
        val tvDate: TextView = itemView.findViewById(R.id.img_date)
        val ivImg: ImageView = itemView.findViewById(R.id.sv_img)
        val view = itemView
    }
}