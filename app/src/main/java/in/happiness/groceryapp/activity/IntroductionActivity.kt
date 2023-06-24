package `in`.happiness.groceryapp.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import `in`.happiness.groceryapp.R
import `in`.happiness.groceryapp.adaptar.WelcomeScreensPagerAdapters
import `in`.happiness.groceryapp.model.IntroModel
import `in`.happiness.groceryapp.utils.PreferenceManager
import `in`.happiness.groceryapp.utils.ZoomOutPageTransformer

import kotlinx.android.synthetic.main.activity_introduction.*
import java.util.*

class IntroductionActivity : AppCompatActivity() {
    private var imageModelList: ArrayList<IntroModel>? = null
    private var prefManager: PreferenceManager? = null

    companion object{
        private const val MIN_SCALE = 0.70f
        private const val MIN_ALPHA = 0.5f
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_introduction)
        setHomeViewPager(introViewPager)

        // setContentView(R.layout.activity_welcome);

        // Checking for first time launch - before calling setContentView()
        prefManager = PreferenceManager(this)

//        if (!prefManager!!.isFirstTimeLaunch) {
//            startActivity(
//                    Intent(this@IntroductionActivity,
//                            MainActivity::class.java))
//            finish()
//        }

        nextButton?.setOnClickListener {
            if(nextButton!!.text.toString() == "Finish")
            {
                prefManager!!.isFirstTimeLaunch=false
                startActivity(
                        Intent(this@IntroductionActivity,
                                OTPActivity::class.java))
                finish()
            }
            else
            {
                introViewPager!!.setCurrentItem(introViewPager!!.currentItem + 1, false)
            }
        }
    }



    private fun setHomeViewPager(advertiseViewPager: ViewPager?) {
        imageModelList = arrayListOf(
            IntroModel(R.drawable.select_product), IntroModel(R.drawable.pay_bills),
                IntroModel(R.drawable.product_delivery)
        )

        introViewPager!!.adapter = WelcomeScreensPagerAdapters(
                this,
                imageModelList!!
        )
        introViewPager!!.clipToPadding = false
//        introViewPager!!.setPadding(80, 0, 80, 0)
//        introViewPager!!.offscreenPageLimit = 3
        pagerIndicatorPanel!!.setupWithViewPager(introViewPager)
        introViewPager!!.setPageTransformer(true, ZoomOutPageTransformer())
        introViewPager!!.addOnPageChangeListener(object :ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {
            }
            override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
            ) {
            }
            override fun onPageSelected(position: Int) {
                nextButton!!.text = if (position==imageModelList!!.size-1) "Finish" else "Next"
            }
        })
    }
}