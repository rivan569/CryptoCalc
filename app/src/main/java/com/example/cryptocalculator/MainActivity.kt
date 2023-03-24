package com.example.cryptocalculator

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputBinding
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.viewpager.widget.ViewPager
import com.example.cryptocalculator.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator

class MainActivity : AppCompatActivity() {



    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var viewPagerAdapter: ViewPagerAdapter
    private lateinit var viewPager: ViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()

        binding.button1.setOnClickListener {
            startActivity(Intent(this, Loginpage::class.java))
        }

        val relativeLayout =findViewById<RelativeLayout>(R.id.layout1)
        val dotsIndicator = findViewById<DotsIndicator>(R.id.dots_indicator)
        val button1 = findViewById<Button>(R.id.button1)
        val animationDrawable=relativeLayout.background as AnimationDrawable
        addAnimation(animationDrawable)

        viewPager = findViewById(R.id.viewpager1)
        viewPagerAdapter= ViewPagerAdapter(this)
        viewPager.adapter = viewPagerAdapter
        dotsIndicator.attachTo(viewPager)

        viewPager.addOnPageChangeListener(object: ViewPager.OnPageChangeListener{
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                    Log.i("Tag",position.toString())
                if(position == 0){
                    relativeLayout.setBackgroundResource(R.drawable.gradientanimation)
                    val animationDrawable=relativeLayout.background as AnimationDrawable
                    addAnimation(animationDrawable)
                }

                if(position == 1){
                    relativeLayout.setBackgroundResource(R.drawable.gradientanimation)
                    val animationDrawable=relativeLayout.background as AnimationDrawable
                    addAnimation(animationDrawable)
                }

                if(position == 2){
                    relativeLayout.setBackgroundResource(R.drawable.gradientanimation)
                    val animationDrawable=relativeLayout.background as AnimationDrawable
                    addAnimation(animationDrawable)
                    button1.visibility= View.VISIBLE
                }
            }

            override fun onPageScrollStateChanged(state: Int) {
            }

        })

    }
    fun addAnimation(animationDrawable: AnimationDrawable){
        animationDrawable.setEnterFadeDuration(10)
        animationDrawable.setExitFadeDuration(5000)
        animationDrawable.start()

    }


}

