package pt.ipt.finalproject

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class PictureAdapter(private val pictureList: List<Int>) :
    RecyclerView.Adapter<PictureAdapter.PictureViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PictureViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.gallery_item, parent, false)
        return PictureViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PictureViewHolder, position: Int) {
        val picture = pictureList[position]
        holder.bindPicture(picture)
    }

    override fun getItemCount(): Int {
        return pictureList.size
    }

    inner class PictureViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.imageView)

        fun bindPicture(picture: Int) {
            imageView.setImageResource(picture)
        }
    }
}
