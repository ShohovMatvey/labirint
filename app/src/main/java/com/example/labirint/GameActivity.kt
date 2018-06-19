package com.example.labirint

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.support.constraint.ConstraintLayout
import android.view.View
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_game.*
import java.util.*
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.Window
import android.view.WindowManager
import kotlinx.android.synthetic.main.dialog_main.view.*


class GameActivity : AppCompatActivity() {

    val paint = Paint()
    var Xpoint : Int = 0
    var Ypoint : Int = 0
    var Xpoint_start : Int = 0
    var Ypoint_start : Int = 0
    var Xpoint_old : Int = 0
    var Ypoint_old : Int = 0
    var LeftInMillis : Long = 0
    var Left_time : Long = 0
    var klet_width : Int = 19
    var klet_height : Int = 16

    lateinit var background: Canvass
    var timer: TextView? = null
    var CountDownTimer: CountDownTimer? = null
    var Running: Boolean = false
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
        setContentView(R.layout.activity_game)
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        top.animate().rotation(90F)
        right.animate().rotation(180F)
        down.animate().rotation(270F)

        val dataBase = DBHelper(this)

        dataBase.read()

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
        LeftInMillis = Left_time

        //val mas_klet = intent.getStringExtra("Mas_klet")
        //for (i in 0 until 10) {
            //for (j in 0 until 10) {
                //if (mas_klet[i][j] != null) Mas_klet = (mas_klet[i][j]).toInt()
            //}
        //}



        startTimer()

        val layout1 = findViewById<ConstraintLayout>(R.id.layout1)
        val background = Canvass(this)
        layout1.addView(background)
        val kletka = 30
        val max_w = 480
        val max_h = 570

        menu.setOnClickListener{
            pauseTimer()

            val intent = Intent(this,MainActivity :: class.java )
            intent.putExtra("Xpoint", (Xpoint).toString())
            intent.putExtra("Ypoint", (Ypoint).toString())
            intent.putExtra("Xpoint_start", (Xpoint_start).toString())
            intent.putExtra("Ypoint_start", (Ypoint_start).toString())
            intent.putExtra("Xpoint_old", (Xpoint_old).toString())
            intent.putExtra("Ypoint_old", (Ypoint_old).toString())
            intent.putExtra("Left_time", (LeftInMillis).toString())
            intent.putExtra("But_home", ("no").toString())
            finish()
            startActivity(intent)
        }

        restart.setOnClickListener{
            pauseTimer()

            val dial_view = LayoutInflater.from(this).inflate(R.layout.dialog_main, null)
            val builder = AlertDialog.Builder(this)
                    .setView(dial_view)
            val AlertDialog = builder.show()
            dial_view.yes.setOnClickListener{
                AlertDialog.dismiss()
                resetTimer()
                startTimer()
                Xpoint = ((0..19).random())*30 + 15
                Ypoint = ((0..16).random())*30 + 15
                Xpoint_start = Xpoint
                Ypoint_start = Ypoint
                Xpoint_old = Xpoint
                Ypoint_old = Ypoint
                generate()
                background.invalidate()
            }
            dial_view.no.setOnClickListener{
                AlertDialog.dismiss()
                startTimer()
            }
        }

        top.setOnClickListener{
            if ((Ypoint < kletka)||
                    (Mas_klet[(Ypoint - kletka / 2) / kletka][(Xpoint - kletka / 2) / kletka] == 11000)||
                    (Mas_klet[(Ypoint - kletka / 2) / kletka][(Xpoint - kletka / 2) / kletka] == 21200)||
                    (Mas_klet[(Ypoint - kletka / 2) / kletka][(Xpoint - kletka / 2) / kletka] == 21030)||
                    (Mas_klet[(Ypoint - kletka / 2) / kletka][(Xpoint - kletka / 2) / kletka] == 21004)||
                    (Mas_klet[(Ypoint - kletka / 2) / kletka][(Xpoint - kletka / 2) / kletka] == 31230)||
                    (Mas_klet[(Ypoint - kletka / 2) / kletka][(Xpoint - kletka / 2) / kletka] == 31204)||
                    (Mas_klet[(Ypoint - kletka / 2) / kletka][(Xpoint - kletka / 2) / kletka] == 31034)||
                    (Mas_klet[(Ypoint - kletka / 2) / kletka][(Xpoint - kletka / 2) / kletka] == 41234)) {
                Xpoint = Xpoint_start
                Ypoint = Ypoint_start
                Xpoint_old = Xpoint
                Ypoint_old = Ypoint
            }
            else {
                Xpoint_old = Xpoint
                Ypoint_old = Ypoint
                Ypoint -= kletka
            }
            background.invalidate()
        }

        down.setOnClickListener{
            if ((Ypoint > max_w - kletka)||
                    (Mas_klet[(Ypoint - kletka / 2) / kletka][(Xpoint - kletka / 2) / kletka] == 10030)||
                    (Mas_klet[(Ypoint - kletka / 2) / kletka][(Xpoint - kletka / 2) / kletka] == 21030)||
                    (Mas_klet[(Ypoint - kletka / 2) / kletka][(Xpoint - kletka / 2) / kletka] == 20230)||
                    (Mas_klet[(Ypoint - kletka / 2) / kletka][(Xpoint - kletka / 2) / kletka] == 20034)||
                    (Mas_klet[(Ypoint - kletka / 2) / kletka][(Xpoint - kletka / 2) / kletka] == 31230)||
                    (Mas_klet[(Ypoint - kletka / 2) / kletka][(Xpoint - kletka / 2) / kletka] == 30234)||
                    (Mas_klet[(Ypoint - kletka / 2) / kletka][(Xpoint - kletka / 2) / kletka] == 31034)||
                    (Mas_klet[(Ypoint - kletka / 2) / kletka][(Xpoint - kletka / 2) / kletka] == 41234)) {
                Xpoint = Xpoint_start
                Ypoint = Ypoint_start
                Xpoint_old = Xpoint
                Ypoint_old = Ypoint
            }
            else {
                Xpoint_old = Xpoint
                Ypoint_old = Ypoint
                Ypoint += kletka
            }
            background.invalidate()
        }

        right.setOnClickListener{
            if ((Xpoint > max_h - kletka)||
                    (Mas_klet[(Ypoint - kletka / 2) / kletka][(Xpoint - kletka / 2) / kletka] == 10200)||
                    (Mas_klet[(Ypoint - kletka / 2) / kletka][(Xpoint - kletka / 2) / kletka] == 21200)||
                    (Mas_klet[(Ypoint - kletka / 2) / kletka][(Xpoint - kletka / 2) / kletka] == 20230)||
                    (Mas_klet[(Ypoint - kletka / 2) / kletka][(Xpoint - kletka / 2) / kletka] == 20204)||
                    (Mas_klet[(Ypoint - kletka / 2) / kletka][(Xpoint - kletka / 2) / kletka] == 31230)||
                    (Mas_klet[(Ypoint - kletka / 2) / kletka][(Xpoint - kletka / 2) / kletka] == 31204)||
                    (Mas_klet[(Ypoint - kletka / 2) / kletka][(Xpoint - kletka / 2) / kletka] == 30234)||
                    (Mas_klet[(Ypoint - kletka / 2) / kletka][(Xpoint - kletka / 2) / kletka] == 41234)) {
                Xpoint = Xpoint_start
                Ypoint = Ypoint_start
                Xpoint_old = Xpoint
                Ypoint_old = Ypoint
            }
            else {
                Xpoint_old = Xpoint
                Ypoint_old = Ypoint
                Xpoint += kletka
            }
            background.invalidate()
        }

        left.setOnClickListener{
            if ((Xpoint < kletka)||
                    (Mas_klet[(Ypoint - kletka / 2) / kletka][(Xpoint - kletka / 2) / kletka] == 10004)||
                    (Mas_klet[(Ypoint - kletka / 2) / kletka][(Xpoint - kletka / 2) / kletka] == 21004)||
                    (Mas_klet[(Ypoint - kletka / 2) / kletka][(Xpoint - kletka / 2) / kletka] == 20034)||
                    (Mas_klet[(Ypoint - kletka / 2) / kletka][(Xpoint - kletka / 2) / kletka] == 20204)||
                    (Mas_klet[(Ypoint - kletka / 2) / kletka][(Xpoint - kletka / 2) / kletka] == 30234)||
                    (Mas_klet[(Ypoint - kletka / 2) / kletka][(Xpoint - kletka / 2) / kletka] == 31204)||
                    (Mas_klet[(Ypoint - kletka / 2) / kletka][(Xpoint - kletka / 2) / kletka] == 31034)||
                    (Mas_klet[(Ypoint - kletka / 2) / kletka][(Xpoint - kletka / 2) / kletka] == 41234)) {
                Xpoint = Xpoint_start
                Ypoint = Ypoint_start
                Xpoint_old = Xpoint
                Ypoint_old = Ypoint
            }
            else {
                Xpoint_old = Xpoint
                Ypoint_old = Ypoint
                Xpoint -= kletka
            }
            background.invalidate()
        }

        timer = findViewById(R.id.timer)
        updateCountDownText()
    }

    override fun onUserLeaveHint() {
        pauseTimer()

        val intent = Intent(this,MainActivity :: class.java )
        intent.putExtra("Xpoint", (Xpoint).toString())
        intent.putExtra("Ypoint", (Ypoint).toString())
        intent.putExtra("Xpoint_start", (Xpoint_start).toString())
        intent.putExtra("Ypoint_start", (Ypoint_start).toString())
        intent.putExtra("Xpoint_old", (Xpoint_old).toString())
        intent.putExtra("Ypoint_old", (Ypoint_old).toString())
        intent.putExtra("Left_time", (LeftInMillis).toString())
        intent.putExtra("But_home", ("yes").toString())
        finish()
        startActivity(intent)
    }


    fun ClosedRange<Int>.random() =
            Random().nextInt(endInclusive - start) + start

    init{
        paint.isFilterBitmap = true
        paint.isAntiAlias = true
    }

    inner class Canvass(context: Context) : View(context) {

        override fun onDraw(canvas: Canvas) {
            val kletka = 30
            val max_w = 480
            val max_h = 570
            val rad = 10
            paint.color = Color.GRAY
            canvas.drawRect((0).toFloat(),(0).toFloat(),(max_h).toFloat(),(max_w).toFloat(), paint)

            //paint.color = Color.WHITE
            //run {
            //    var i = kletka
            //    while (i <= max_h) {
            //        canvas.drawLine((i).toFloat(), (0).toFloat(), (i).toFloat(), (max_w).toFloat(), paint)
            //        i += kletka
            //    }
            //}
            var i = kletka
            while (i <= max_w) {
                canvas.drawLine((0).toFloat(), (i).toFloat(), (max_h).toFloat(), (i).toFloat(), paint)
                i += kletka
            }

            paint.color = Color.BLACK
            canvas.drawLine((0).toFloat(), (max_w).toFloat(), (max_h).toFloat(), (max_w).toFloat(), paint)
            canvas.drawLine((0).toFloat(), (0).toFloat(), (max_h).toFloat(), (0).toFloat(), paint)
            canvas.drawLine((max_h).toFloat(), (0).toFloat(), (max_h).toFloat(), (max_w).toFloat(), paint)
            canvas.drawLine((0).toFloat(), (0).toFloat(), (0).toFloat(), (max_w).toFloat(), paint)

            paint.color = Color.rgb(250, 231, 181)
            canvas.drawRect((Xpoint - kletka/2 + 1).toFloat(),(Ypoint - kletka/2 + 1).toFloat(),(Xpoint + kletka/2 - 1).toFloat(),(Ypoint + kletka/2 - 1).toFloat(), paint)
            canvas.drawRect((Xpoint_old - kletka/2 + 1).toFloat(),(Ypoint_old - kletka/2 + 1).toFloat(),(Xpoint_old + kletka/2 - 1).toFloat(),(Ypoint_old + kletka/2 - 1).toFloat(), paint)

            paint.color = Color.GREEN
            canvas.drawCircle((Xpoint).toFloat(),(Ypoint).toFloat(),(rad).toFloat(),paint)

            paint.color = Color.BLACK
            for (i in 0 until klet_height *(klet_width - 1)) {
                if (Mas_sten[i] == 1) {
                    canvas.drawLine(((i % (klet_width - 1)) * kletka + kletka).toFloat(), (0 + kletka * (i / (klet_width - 1))).toFloat(), ((i % (klet_width - 1)) * kletka + kletka).toFloat(), (kletka + kletka * (i / (klet_width - 1))).toFloat(), paint)
                }
            }
            for (i in klet_height *(klet_width - 1) + 1 until ((klet_height *(klet_width - 1)) + (klet_width *(klet_height - 1)))) {
                if (Mas_sten[i] == 1) {
                    canvas.drawLine((0 + kletka * ((i - klet_height *(klet_width - 1)) / (klet_height - 1))).toFloat(), ((i - klet_height *(klet_width - 1)) % (klet_height - 1) * kletka + kletka).toFloat(), (kletka + kletka * ((i - klet_height *(klet_width - 1)) / (klet_height - 1))).toFloat(), ((i - klet_height *(klet_width - 1)) % (klet_height - 1) * kletka + kletka).toFloat(), paint)
                }
            }
        }
    }



    fun startTimer() {
        CountDownTimer = object : CountDownTimer(LeftInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                LeftInMillis = millisUntilFinished
                updateCountDownText()
            }
            override fun onFinish() {
                Running = false
            }
        }.start()
        Running = true
    }

    fun pauseTimer() {
        CountDownTimer!!.cancel()
        Running = false
    }

    fun resetTimer() {
        LeftInMillis = all_time
        updateCountDownText()
    }

    fun updateCountDownText() {
        val minutes = (3600 - LeftInMillis / 1000).toInt() / 60
        val seconds = (3600 - LeftInMillis / 1000).toInt() % 60
        val timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
        timer!!.text = timeLeftFormatted
    }

    companion object {
        val all_time: Long = 3600000
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
                if (Mas_klet[i][j] % 10000 - Mas_klet[i][j]%1000 !=0) k = k+1
                Mas_klet[i][j] = 10000 * k + Mas_klet[i][j] % 10000
                k = 0
            }
        }
        /*for (i in 0 until klet_height) {
            for (j in 0 until klet_width) {
                print("$i $j ")
                println(Mas_klet[i][j])
            }
        }
        for (i in 0 until ((klet_height *(klet_width - 1)) + (klet_width *(klet_height - 1)))){
            print("$i ")
            println(Mas_sten[i])
        }*/
    }
}