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

    lateinit var background: Canvass
    var timer: TextView? = null
    var CountDownTimer: CountDownTimer? = null
    var Running: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_game)
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
        LeftInMillis = Left_time

        startTimer()

        val layout1 = findViewById(R.id.layout1) as ConstraintLayout
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
            intent.putExtra("but_home", ("no").toString())
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
                background.invalidate()
            }
            dial_view.no.setOnClickListener{
                AlertDialog.dismiss()
                startTimer()
            }
        }

        top.setOnClickListener{
            if (Ypoint >= kletka) {
                Xpoint_old = Xpoint
                Ypoint_old = Ypoint
                Ypoint -= kletka
            }
            else {
                Xpoint = Xpoint_start
                Ypoint = Ypoint_start
                Xpoint_old = Xpoint
                Ypoint_old = Ypoint
            }
            background.invalidate()
        }

        down.setOnClickListener{
            if (Ypoint <= max_w - kletka) {
                Xpoint_old = Xpoint
                Ypoint_old = Ypoint
                Ypoint += kletka
            }
            else {
                Xpoint = Xpoint_start
                Ypoint = Ypoint_start
                Xpoint_old = Xpoint
                Ypoint_old = Ypoint
            }
            background.invalidate()
        }

        right.setOnClickListener{
            if (Xpoint <= max_h - kletka) {
                Xpoint_old = Xpoint
                Ypoint_old = Ypoint
                Xpoint += kletka
            }
            else {
                Xpoint = Xpoint_start
                Ypoint = Ypoint_start
                Xpoint_old = Xpoint
                Ypoint_old = Ypoint
            }
            background.invalidate()
        }

        left.setOnClickListener{
            if (Xpoint >= kletka) {
                Xpoint_old = Xpoint
                Ypoint_old = Ypoint
                Xpoint -= kletka
            }
            else {
                Xpoint = Xpoint_start
                Ypoint = Ypoint_start
                Xpoint_old = Xpoint
                Ypoint_old = Ypoint
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
        intent.putExtra("but_home", ("yes").toString())
        finish()
        startActivity(intent)
    }


    fun ClosedRange<Int>.random() =
            Random().nextInt(endInclusive - start) + start

    val paint = Paint()
    var Xpoint : Int = 0
    var Ypoint : Int = 0
    var Xpoint_start : Int = 0
    var Ypoint_start : Int = 0
    var Xpoint_old : Int = 0
    var Ypoint_old : Int = 0
    var LeftInMillis : Long = 0
    var Left_time : Long = 0
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

            paint.color = Color.BLACK
            run {
                var i = kletka
                while (i <= max_h) {
                    canvas.drawLine((i).toFloat(), (0).toFloat(), (i).toFloat(), (max_w).toFloat(), paint)
                    i += kletka
                }
            }
            var i = kletka
            while (i <= max_w) {
                canvas.drawLine((0).toFloat(), (i).toFloat(), (max_h).toFloat(), (i).toFloat(), paint)
                i += kletka
            }

            paint.color = Color.RED
            canvas.drawLine((0).toFloat(), (max_w).toFloat(), (max_h).toFloat(), (max_w).toFloat(), paint)
            canvas.drawLine((0).toFloat(), (0).toFloat(), (max_h).toFloat(), (0).toFloat(), paint)
            canvas.drawLine((max_h).toFloat(), (0).toFloat(), (max_h).toFloat(), (max_w).toFloat(), paint)
            canvas.drawLine((0).toFloat(), (0).toFloat(), (0).toFloat(), (max_w).toFloat(), paint)

            paint.color = Color.YELLOW
            canvas.drawRect((Xpoint - kletka/2 + 1).toFloat(),(Ypoint - kletka/2 + 1).toFloat(),(Xpoint + kletka/2 - 1).toFloat(),(Ypoint + kletka/2 - 1).toFloat(), paint)
            canvas.drawRect((Xpoint_old - kletka/2 + 1).toFloat(),(Ypoint_old - kletka/2 + 1).toFloat(),(Xpoint_old + kletka/2 - 1).toFloat(),(Ypoint_old + kletka/2 - 1).toFloat(), paint)

            paint.color = Color.GREEN
            canvas.drawCircle((Xpoint).toFloat(),(Ypoint).toFloat(),(rad).toFloat(),paint)
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
}