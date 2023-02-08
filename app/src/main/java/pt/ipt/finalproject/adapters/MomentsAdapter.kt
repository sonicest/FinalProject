package pt.ipt.finalproject.adapters


import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pt.ipt.finalproject.CameraActivity
import pt.ipt.finalproject.MainActivity
import pt.ipt.finalproject.MapTracking
import pt.ipt.finalproject.R
import pt.ipt.finalproject.models.Moment
import pt.ipt.finalproject.utilities.Constant

// the page that have the moments will be made by a arraylist called MOMENT
class MomentsAdapter(private var list: ArrayList<Moment>) :
    //recyclerview is made to display a long list of items
    RecyclerView.Adapter<MomentsAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.saved_pic_layout, parent, false)
        return MyViewHolder(view)
    }
// reclying the view (moments list)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
    //position value that to show the correct view fo the correct moment
        val pos = list[position]

        holder.apply {
            val cxt = this.view.context
            val id = pos.id
            val imgUri = pos.ImgUri
            val description = pos.description
            val date = pos.date
            val location = pos.location
            // val userId = pos.userId

            ivImg.setImageURI(Uri.parse(imgUri))
            tvDescription.text = description
            tvDate.text = date


            but.setOnClickListener {
                Constant.helper.deleteMoment(id)
                val intent = Intent(cxt, MainActivity::class.java)
                cxt.startActivity(intent)
            }
            //when i click on the item the show teh details
            view.setOnClickListener {
                val intent = Intent(cxt, MapTracking::class.java)
                //   intent.putExtra("id", id)
                intent.putExtra("imgUri", imgUri)//is made to connect with db and show the right image
                intent.putExtra("description", description)//string name and keyidentifier
                intent.putExtra("date", date)
                intent.putExtra("location", location)
                cxt.startActivity(intent)
            }
        }
    }
   //to help the onBindViewholder and oncreatviewholder we need to know the number its in our array list
    override fun getItemCount(): Int {
        return list.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tvDescription: TextView = itemView.findViewById(R.id.img_desc)
        val tvDate: TextView = itemView.findViewById(R.id.img_date)
        val ivImg: ImageView = itemView.findViewById(R.id.sv_img)
        val but: View = itemView.findViewById(R.id.delet)
        val view = itemView
    }
}