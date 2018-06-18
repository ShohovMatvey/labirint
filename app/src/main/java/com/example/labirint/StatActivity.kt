package com.example.labirint

import android.content.Intent
import android.content.pm.ActivityInfo
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import kotlinx.android.synthetic.main.activity_game.*
import java.util.*


class StatActivity : AppCompatActivity() {


    var Xpoint : Int = 0
    var Ypoint : Int = 0
    var Xpoint_start = Xpoint
    var Ypoint_start = Ypoint
    var Xpoint_old = Xpoint
    var Ypoint_old = Ypoint
    var Left_time : Long = GameActivity.all_time
    var But_home : String = "no"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_stat)
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);

        val xpoint = intent.getStringExtra("Xpoint")
        if (xpoint != null) Xpoint = (xpoint).toInt()
        val ypoint = intent.getStringExtra("Ypoint")
        if (ypoint != null) Ypoint = (ypoint).toInt()
        val xpoint_start = intent.getStringExtra("Xpoint_start")
        if (xpoint_start != null) Xpoint_start = (xpoint_start).toInt()
        val ypoint_start = intent.getStringExtra("Ypoint_start")
        if (ypoint_start != null) Ypoint_start = (ypoint_start).toInt()
        val xpoint_old = intent.getStringExtra("Xpoint_old")
        if (xpoint_old != null) Xpoint_old = (xpoint_old).toInt()
        val ypoint_old = intent.getStringExtra("Ypoint_old")
        if (ypoint_old != null) Ypoint_old = (ypoint_old).toInt()
        val left_time = intent.getStringExtra("Left_time")
        if (left_time != null) Left_time = (left_time).toLong()
        val but_home = intent.getStringExtra("But_home")
        if (but_home != null) But_home = (but_home).toString()


        menu.setOnClickListener{
            val intent = Intent(this,MainActivity :: class.java )
            intent.putExtra("Xpoint", (Xpoint).toString())
            intent.putExtra("Ypoint", (Ypoint).toString())
            intent.putExtra("Xpoint_start", (Xpoint_start).toString())
            intent.putExtra("Ypoint_start", (Ypoint_start).toString())
            intent.putExtra("Xpoint_old", (Xpoint_old).toString())
            intent.putExtra("Ypoint_old", (Ypoint_old).toString())
            intent.putExtra("Left_time", (Left_time).toString())
            intent.putExtra("But_home", ("no").toString())
            finish()
            startActivity(intent)
        }
    }

    override fun onUserLeaveHint() {

        val intent = Intent(this,MainActivity :: class.java )
        intent.putExtra("Xpoint", (Xpoint).toString())
        intent.putExtra("Ypoint", (Ypoint).toString())
        intent.putExtra("Xpoint_start", (Xpoint_start).toString())
        intent.putExtra("Ypoint_start", (Ypoint_start).toString())
        intent.putExtra("Xpoint_old", (Xpoint_old).toString())
        intent.putExtra("Ypoint_old", (Ypoint_old).toString())
        intent.putExtra("Left_time", (Left_time).toString())
        intent.putExtra("But_home", ("yes").toString())
        finish()
        startActivity(intent)
    }

}
