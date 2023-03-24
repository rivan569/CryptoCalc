package com.example.cryptocalculator

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter

class ViewPagerAdapter(val context: Context): PagerAdapter() {

    var layoutInflater: LayoutInflater? = null

    val imgArray =  arrayOf(
        R.drawable.intro1,
        R.drawable.intro2,
        R.drawable.intro3
    )
    val headarray= arrayOf(
        "Calculate and Convert Cryptocurrency",
        "Track the currennt exchange rates of Cryptocurrencies",
        "Monitor and Track your favorite Cryptocurrency"
    )

    override fun getCount(): Int {
        return headarray.size

    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object` as RelativeLayout

    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = layoutInflater!!.inflate(R.layout.slider,container,false)

        val img = view.findViewById<ImageView>(R.id.image)
        val txt_head = view.findViewById<TextView>(R.id.txt_head)

        img.setImageResource(imgArray[position])
        txt_head.text = headarray[position]

        container.addView(view)

        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as RelativeLayout)
    }
}