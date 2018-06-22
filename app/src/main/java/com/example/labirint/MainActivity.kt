package com.example.labirint


import android.content.Intent
import android.content.pm.ActivityInfo
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.util.DisplayMetrics
import android.view.*
import android.widget.*
import kotlinx.android.synthetic.main.activity_main.*
import com.example.labirint.GameActivity.Companion.all_time
import kotlinx.android.synthetic.main.dialog_main.view.*
import java.util.*
import kotlinx.android.synthetic.main.my_cool_spinner_item.view.*


class MainActivity : AppCompatActivity() {

    //val kletka = 30
    var klet_width : Int = 16
    var klet_height : Int = 16
    //var Mas_klet: Array<Array<Int>> = Array(klet_height, { Array(klet_width, { 10000 }) })
    //var Mas_sten: Array<Int> = Array(((klet_height *(klet_width - 1)) + (klet_width *(klet_height - 1))), { 0 })
    var k : Int = 0
    var n : Int = 0
    var Xkey = 0
    var Ykey = 0
    var key_yes_was = false
    var key_yes = false
    lateinit var dif : String
    //var generate_sten = ((klet_height *(klet_width - 1)) + (klet_width *(klet_height - 1)))/2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_main)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE)
        val display = windowManager.defaultDisplay
        val metricsB = DisplayMetrics()
        display.getMetrics(metricsB)

        val max_height = metricsB.heightPixels
        val kletka = max_height / klet_height
        //val max_width = kletka * klet_width

        val difficult = arrayOf("ЛЕГКО","НОРМАЛЬНО","СЛОЖНО","ХАРД")
        val adapter = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                difficult
        )

        adapter.setDropDownViewResource(R.layout.my_cool_spinner_item)

        spinner.adapter = adapter

        spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent:AdapterView<*>, view: View, position: Int, id: Long){
                dif = difficult[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>){
            }
        }


        val dataBase = DBHelper(this)

        var Xpoint : Int
        var Ypoint : Int
        var Xpoint_start: Int
        var Ypoint_start : Int
        var Xpoint_old : Int
        var Ypoint_old : Int
        var Left_time : Long
        var But_home : String = "no"

        val but_home = intent.getStringExtra("But_home")
        if (but_home != null) But_home = (but_home).toString()
        if (But_home == "yes") {
            finish()
            super.onUserLeaveHint()
        }

        contin.setOnClickListener {
            val intent = Intent(this, GameActivity::class.java)
            intent.putExtra("New_game", ("no").toString())
            intent.putExtra("Dif", (dif).toString())
            finish()
            startActivity(intent)
        }

        new_game.setOnClickListener {
            val intent = Intent(this, GameActivity::class.java)
            Xpoint = ((0..klet_width).random()) * kletka + kletka / 2
            Ypoint = ((0..klet_height).random()) * kletka + kletka / 2
            Xpoint_start = Xpoint
            Ypoint_start = Ypoint
            Xpoint_old = Xpoint
            Ypoint_old = Ypoint
            Left_time = all_time
            dataBase.clear()
            dataBase.add(GameState(
                    (Xpoint).toString(),
                    (Ypoint).toString(),
                    (Xpoint_start).toString(),
                    (Ypoint_start).toString(),
                    (Xpoint_old).toString(),
                    (Ypoint_old).toString(),
                    (Xkey).toString(),
                    (Ykey).toString(),
                    (key_yes_was).toString(),
                    (key_yes).toString(),
                    (Left_time).toString()))
            intent.putExtra("New_game", ("yes").toString())
            intent.putExtra("Dif", (dif).toString())
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
            finish()
            startActivity(intent)
        }
    }

    override fun onUserLeaveHint() {

        finish()
        super.onUserLeaveHint()
    }

    fun ClosedRange<Int>.random() =
            Random().nextInt(endInclusive - start) +  start
}