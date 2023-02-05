package pt.ipt.finalproject.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.spinner_item_menu.view.*
import pt.ipt.finalproject.R
import pt.ipt.finalproject.utilities.Menu

class MenuArrayAdapter(context: Context, menuList: List<Menu>) :
    ArrayAdapter<Menu>(context, 0, menuList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    private fun initView(position: Int, convertView: View?, parent: ViewGroup): View {
        val option = getItem(position)
        val view = LayoutInflater.from(context).inflate(R.layout.spinner_item_menu, parent, false)
        view.image.setImageResource(option!!.images)
        view.descr.text = option.name
        return view
    }
}