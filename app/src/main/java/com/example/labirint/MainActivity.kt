package com.example.labirint

import android.content.Intent
import android.content.pm.ActivityInfo
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import kotlinx.android.synthetic.main.activity_main.*
import android.view.Window
import android.view.WindowManager
import com.example.labirint.GameActivity.Companion.all_time
import kotlinx.android.synthetic.main.dialog_main.view.*
import java.util.*


class MainActivity : AppCompatActivity() {

    var mas_klet: Array<Array<Int>> = Array(10, { Array(10, { 10000 }) })
    var mas_sten: Array<Int> = Array(180, { 0 })
    var k : Int = 0
    var n : Int = 0
    var generate_sten = 70

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_main)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE)
        val kletka = 30

        var Xpoint: Int = ((0..19).random()) * kletka + kletka / 2
        var Ypoint: Int = ((0..16).random()) * kletka + kletka / 2
        var Xpoint_start = Xpoint
        var Ypoint_start = Ypoint
        var Xpoint_old = Xpoint
        var Ypoint_old = Ypoint
        var Left_time: Long = all_time
        var But_home: String = "no"


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
        if (But_home == "yes") {
            finish()
            super.onUserLeaveHint()
        }

        begin.setOnClickListener {
            val intent = Intent(this, GameActivity::class.java)
            intent.putExtra("Xpoint", (Xpoint).toString())
            intent.putExtra("Ypoint", (Ypoint).toString())
            intent.putExtra("Xpoint_start", (Xpoint_start).toString())
            intent.putExtra("Ypoint_start", (Ypoint_start).toString())
            intent.putExtra("Xpoint_old", (Xpoint_old).toString())
            intent.putExtra("Ypoint_old", (Ypoint_old).toString())
            intent.putExtra("Left_time", (Left_time).toString())
            finish()
            startActivity(intent)
        }

        new_game.setOnClickListener {
            val intent = Intent(this, GameActivity::class.java)
            var kletka = 30
            Xpoint = ((0..19).random()) * kletka + kletka
            Ypoint = ((0..16).random()) * kletka + kletka
            Xpoint_start = Xpoint
            Ypoint_start = Ypoint
            Xpoint_old = Xpoint
            Ypoint_old = Ypoint
            Left_time = all_time
            intent.putExtra("Xpoint", (Xpoint).toString())
            intent.putExtra("Ypoint", (Ypoint).toString())
            intent.putExtra("Xpoint_start", (Xpoint_start).toString())
            intent.putExtra("Ypoint_start", (Ypoint_start).toString())
            intent.putExtra("Xpoint_old", (Xpoint_start).toString())
            intent.putExtra("Ypoint_old", (Ypoint_start).toString())
            intent.putExtra("Left_time", (Left_time).toString())
            finish()
            startActivity(intent)
        }

        exit.setOnClickListener {
            val dial_view = LayoutInflater.from(this).inflate(R.layout.dialog_main_exit, null)
            val builder = AlertDialog.Builder(this)
                    .setView(dial_view)
            val AlertDialog = builder.show()
            dial_view.yes.setOnClickListener {
                AlertDialog.dismiss()
                finish()
            }
            dial_view.no.setOnClickListener {
                AlertDialog.dismiss()
            }
        }

        stat.setOnClickListener {
            val intent = Intent(this, StatActivity::class.java)
            intent.putExtra("Xpoint", (Xpoint).toString())
            intent.putExtra("Ypoint", (Ypoint).toString())
            intent.putExtra("Xpoint_start", (Xpoint_start).toString())
            intent.putExtra("Ypoint_start", (Ypoint_start).toString())
            intent.putExtra("Xpoint_old", (Xpoint_old).toString())
            intent.putExtra("Ypoint_old", (Ypoint_old).toString())
            intent.putExtra("Left_time", (Left_time).toString())
            finish()
            startActivity(intent)
        }

        //generate()
        //for (i in 0 until 10) {
        //    for (j in 0 until 10) {
        //        print(mas_sten[i])
        //    }
        //}
        //for (i in 0 until 10) {
        //    for (j in 0 until 10) {
        //        print(mas_klet[i])
        //    }
        //}
    }

    override fun onUserLeaveHint() {
        finish()
        super.onUserLeaveHint()
    }

    fun generate() {
        for (i in 0 until generate_sten) {
            n = (0..180).random()
            if (n < 90) {
                mas_klet[n / 9][n % 9] = mas_klet[n / 9][n % 9] / 1000 * 1000 + mas_klet[n / 9][n % 9] % 100 + 200
                mas_klet[n / 9][n % 9 + 1] = mas_klet[n / 9][n % 9 + 1] / 10 * 10 + 4
            } else {
                n = n - 90
                mas_klet[n % 9][n / 9] = mas_klet[n % 9][n / 9] / 100 * 100 + mas_klet[n % 9][n / 9] % 10 + 30
                mas_klet[n % 9 + 1][n / 9] = mas_klet[n % 9 + 1][n / 9] / 10000 * 10000 + mas_klet[n % 9 + 1][n / 9] % 1000 + 1000
            }
            mas_sten[n] = 1
        }

        for (i in 0 until 10) {
            for (j in 0 until 10) {
                if (mas_klet[i][j] % 10 != 0) k = k + 1
                if (mas_klet[i][j] % 100 - mas_klet[i][j] % 10 != 0) k = k + 1
                if (mas_klet[i][j] % 1000 - mas_klet[i][j] % 100 != 0) k = k + 1
                mas_klet[i][j] = 10000 * k + mas_klet[i][j] % 10000
                k = 0
            }
        }
    }

    fun ClosedRange<Int>.random() =
            Random().nextInt(endInclusive - start) +  start
}