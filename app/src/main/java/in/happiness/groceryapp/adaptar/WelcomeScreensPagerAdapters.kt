package `in`.happiness.groceryapp.adaptar

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import `in`.happiness.groceryapp.R
import `in`.happiness.groceryapp.model.IntroModel
import java.util.*

class WelcomeScreensPagerAdapters(
        var context: Context,
        var imageList: ArrayList<IntroModel>
) : PagerAdapter() {

    //private var advertiseBinding:AdvertiseItemBinding?=null
    override fun getCount(): Int {
        return imageList!!.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object` as RelativeLayout
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        var imageLayout: View? = null
        var itemImage: ImageView?
        var itemImage2: ImageView?

        imageLayout = LayoutInflater.from(container.context).inflate(
                R.layout.advertise_item,
                container,
                false
        )!!

        itemImage = imageLayout!!.findViewById(R.id.image) as ImageView
        itemImage2 = imageLayout!!.findViewById(R.id.image2) as ImageView
        itemImage.visibility = View.VISIBLE
        itemImage2.visibility = View.GONE
        imageList[position].image_drawable.let {
            Glide.with(context).load(it).into(itemImage)
        }
//        itemImage.setImageResource (imageList[position].getImage_drawables())
        container!!.addView(imageLayout, 0)
        itemImage!!.setOnClickListener {
//            (context as MainActivity).launchFragment(NewsTabFragment(),context.getString(R.string.drawer_news))
//            (context as BaseDrawerActivity).showToast("you clicked image " + (position + 1))
        }
        return imageLayout!!
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as RelativeLayout)
    }
}
