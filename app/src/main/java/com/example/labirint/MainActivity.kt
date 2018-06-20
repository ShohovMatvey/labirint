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
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
//import com.megaplanner.myapplication3.R


class MainActivity : AppCompatActivity() {

    val kletka = 30
    var klet_width : Int = 19
    var klet_height : Int = 16
    var Mas_klet: Array<Array<Int>> = Array(klet_height, { Array(klet_width, { 10000 }) })
    var Mas_sten: Array<Int> = Array(((klet_height *(klet_width - 1)) + (klet_width *(klet_height - 1))), { 0 })
    var k : Int = 0
    var n : Int = 0
    var generate_sten = ((klet_height *(klet_width - 1)) + (klet_width *(klet_height - 1)))/2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_main)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE)

        // Initializing a String Array
        val difficult = arrayOf("Легко","Нормально","Сложно","Хард")

        // Initializing an ArrayAdapter
        val adapter = ArrayAdapter(
                this, // Context
                android.R.layout.simple_spinner_item, // Layout
                difficult // Array
        )

        // Set the drop down view resource
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)

        // Finally, data bind the spinner object with dapter
        spinner.adapter = adapter;

        // Set an on item selected listener for spinner object
        spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent:AdapterView<*>, view: View, position: Int, id: Long){
                // Display the selected item text on text view

            }

            override fun onNothingSelected(parent: AdapterView<*>){
                // Another interface callback
            }
        }
        val dataBase = DBHelper(this)

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

        contin.setOnClickListener {
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
            val kletka = 30
            Xpoint = ((0..19).random()) * kletka + kletka / 2
            Ypoint = ((0..16).random()) * kletka + kletka / 2
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

            dataBase.add(GameState(
                    (Xpoint).toString(),
                    (Ypoint).toString(),
                    (Xpoint_start).toString(),
                    (Ypoint_start).toString(),
                    (Xpoint_old).toString(),
                    (Ypoint_old).toString(),
                    (Left_time).toString()))
            //intent.putExtra("Mas_klet", (Mas_klet).toString())
            //intent.putExtra("Mas_sten", (Mas_sten).toString())
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
    }

    override fun onUserLeaveHint() {

        finish()
        super.onUserLeaveHint()
    }

    fun generate() {
        Mas_klet = Array(klet_height, { Array(klet_width, { 10000 }) })
        Mas_sten = Array(((klet_height *(klet_width - 1)) + (klet_width *(klet_height - 1))), { 0 })
        for (i in 0 until generate_sten) {
            n = (0..((klet_height *(klet_width - 1)) + (klet_width *(klet_height - 1)))).random()
            if (n < (klet_height *(klet_width - 1))) {
                Mas_klet[n / (klet_width - 1)][n % (klet_width - 1)] = Mas_klet[n / (klet_width - 1)][n % (klet_width - 1)] / 1000 * 1000 + Mas_klet[n / (klet_width - 1)][n % (klet_width - 1)] % 100 + 200
                Mas_klet[n / (klet_width - 1)][n % (klet_width - 1) + 1] = Mas_klet[n / (klet_width - 1)][n % (klet_width - 1) + 1] / 10 * 10 + 4
                Mas_sten[n] = 1
            } else {
                n = n - (klet_height *(klet_width - 1))
                Mas_klet[n % (klet_height - 1)][n / (klet_height - 1)] = Mas_klet[n % (klet_height - 1)][n / (klet_height - 1)] / 100 * 100 + Mas_klet[n % (klet_height - 1)][n / (klet_height - 1)] % 10 + 30
                Mas_klet[n % (klet_height - 1) + 1][n / (klet_height - 1)] = Mas_klet[n % (klet_height - 1) + 1][n / (klet_height - 1)] / 10000 * 10000 + Mas_klet[n % (klet_height - 1) + 1][n / (klet_height - 1)] % 1000 + 1000
                Mas_sten[n + (klet_height *(klet_width - 1))] = 1
            }
        }

        for (i in 0 until klet_height) {
            for (j in 0 until klet_width) {
                if (Mas_klet[i][j] % 10 != 0) k = k + 1
                if (Mas_klet[i][j] % 100 - Mas_klet[i][j] % 10 != 0) k = k + 1
                if (Mas_klet[i][j] % 1000 - Mas_klet[i][j] % 100 != 0) k = k + 1
                if (Mas_klet[i][j] % 10000 - Mas_klet[i][j]% 1000 != 0) k = k + 1
                Mas_klet[i][j] = 10000 * k + Mas_klet[i][j] % 10000
                k = 0
            }
        }
    }

    fun ClosedRange<Int>.random() =
            Random().nextInt(endInclusive - start) +  start
}