package pt.ipt.finalproject.utilities

import pt.ipt.finalproject.R

data class Menu(val images: Int, val name: String)

object MenuList{
    private val images = intArrayOf(
      //  R.drawable.ic_baseline_account_circle_24,
        R.drawable.ic_info,
        R.drawable.ic_logout
    )

    private val menus = arrayOf(
       /// "Account",
        "Info",
        "Log out"
    )

    var list: ArrayList<Menu>? = null
        get() {

            if (field != null)
                return field

            field = ArrayList()
            for (i in images.indices) {

                val imageId = images[i]
                val desc = menus[i]

                val option = Menu(imageId, desc)
                field!!.add(option)
            }

            return field
        }
}
