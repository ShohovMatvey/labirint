package com.something.labirint

import android.content.Context
import android.content.Intent
import android.graphics.*
import android.media.MediaPlayer
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.support.constraint.ConstraintLayout
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_game.*
import java.util.*
import android.support.v7.app.AlertDialog
import android.text.TextUtils.substring
import android.util.DisplayMetrics
import android.view.*
import kotlinx.android.synthetic.main.dialog_main.view.*
import kotlinx.android.synthetic.main.dialog_main_timer.view.*



class GameActivity : AppCompatActivity() {

    val paint = Paint()
    var Xpoint: Int = 0
    var Ypoint: Int = 0
    var Xpoint_start: Int = 0
    var Ypoint_start: Int = 0
    var Xpoint_old: Int = 0
    var Ypoint_old: Int = 0
    var Kol_games_easy = 0
    var Kol_wins_easy = 0
    var All_time_easy : Long = 0
    var Min_time_easy = all_time
    var Kol_games_normal = 0
    var Kol_wins_normal = 0
    var All_time_normal : Long = 0
    var Min_time_normal = all_time
    var Kol_games_dif = 0
    var Kol_wins_dif = 0
    var All_time_dif : Long = 0
    var Min_time_dif = all_time
    var Kol_games_hard = 0
    var Kol_wins_hard = 0
    var All_time_hard : Long = 0
    var Min_time_hard = all_time

    var LeftInMillis: Long = 0
    var LeftInMillis2: Long = 525
    var l = 0
    var Left_time: Long = 0
    var klet_width: Int = 16
    var klet_height: Int = 16
    var game_over = "no"
    var win_game = false

    val dataBase = DBHelper(this)
    lateinit var background: Canvass
    var timer: TextView? = null
    var CountDownTimer: CountDownTimer? = null
    var CountDownTimer2: CountDownTimer? = null
    var CountDownTimer3: CountDownTimer? = null
    var Running: Boolean = false
    var Mas_klet: Array<Array<Int>> = Array(klet_height, { Array(klet_width, { 10000 }) })
    var Mas_sten: Array<Int> = Array(((klet_height * (klet_width - 1)) + (klet_width * (klet_height - 1))), { 0 })
    var Vozm_klet: Array<Array<Int>> = Array(klet_height, { Array(klet_width, { 0 }) })
    var Dostup_stenka: Array<Int> = Array((2 * klet_height + 2 * klet_width), { -1 })
    var k : Int = 0
    var n : Int = 0
    var i : Int = 0
    var Xkey : Int = 0
    var Ykey : Int = 0
    var win_sten: Int = 0
    var Mas_klet_str : String = ""
    var Mas_sten_str : String = ""
    var New_game : String = "no"
    var generate_sten = ((klet_height * (klet_width - 1)) + (klet_width * (klet_height - 1))) / 2
    var key_yes : Boolean = false
    var key_yes_was : Boolean = false
    var Dif : String = ""
    var runnin = false
    var fx : Int = 0
    var fy : Int = 0
    var fz : Int = 0
    var II : Int = 0
    var clach = false
    var printkey = false
    var key_yes1 = false
    //lateinit var swipe : GestureListener

    lateinit var Sound : MediaPlayer
    lateinit var playerBitmap : Bitmap
    lateinit var playerBitmap2 : Bitmap
    lateinit var playerBitmap3 : Bitmap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_game)
        val display = windowManager.defaultDisplay
        val metricsB = DisplayMetrics()
        display.getMetrics(metricsB)

        val max_height = metricsB.heightPixels
        val kletka = max_height / klet_height
        val max_width = kletka * klet_width
        val realy_max_width = metricsB.widthPixels

        playerBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(resources,R.mipmap.ic_launcher_smile1),kletka - 2,kletka - 2,false)
        playerBitmap2 = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(resources,R.mipmap.ic_launcher_smile2),kletka - 2,kletka - 2,false)
        playerBitmap3 = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(resources,R.mipmap.ic_launcher_key),kletka - 2,kletka - 2,false)
        top.animate().rotation(90F)
        right.animate().rotation(180F)
        down.animate().rotation(270F)

        control.translationX = ((max_width + control.layoutParams.width - realy_max_width)/2).toFloat()

        val new_game = intent.getStringExtra("New_game")
        if (new_game != null) New_game = (new_game).toString()

        val scores = dataBase.read()
        if (scores != null) {
            Xpoint = (scores.Xpoint).toInt()
            Ypoint = (scores.Ypoint).toInt()
            Xpoint_start = (scores.Xpoint_start).toInt()
            Ypoint_start = (scores.Ypoint_start).toInt()
            Xpoint_old = (scores.Xpoint_old).toInt()
            Ypoint_old = (scores.Ypoint_old).toInt()
            Xkey = (scores.Xkey).toInt()
            Ykey = (scores.Ykey).toInt()
            key_yes_was = (scores.key_yes_was).toBoolean()
            key_yes = (scores.key_yes).toBoolean()
            Left_time = (scores.Left_time).toLong()
        }

        val scores4 = dataBase.read_dif()
        if (scores4 != null) {
            Dif = (scores4.dif)
            game_over = (scores4.game_over)
        }

        if (New_game == "yes") {
            gener_all()
        }
        else {
            val scores2 = dataBase.read_mas()
            if (scores2 != null) {
                Mas_klet_str = (scores2.Mas_klet_str)
                Mas_sten_str = (scores2.Mas_sten_str)
                win_sten = (scores2.win_sten).toInt()
            }
            var i = 0
            var j = 0
            for (a in Mas_klet_str.split(" ")) {
                Mas_klet[i][j] = a.toInt()
                j++
                if (j == klet_width){
                    i++
                    j = 0
                }
            }

            var f = -1
            for (a in Mas_sten_str.split(" ")) {
                f += 1
                Mas_sten[f] = a.toInt()
            }
        }

        LeftInMillis = Left_time
        startTimer()

        val layout1 = findViewById<ConstraintLayout>(R.id.layout1)
        val background = Canvass(this)
        layout1.addView(background)

        Sound = MediaPlayer.create(this, R.raw.drop)
        Sound.start()
        Sound.setOnCompletionListener( MediaPlayer.OnCompletionListener {
            Sound.start()
        })


        /*val SWIPE_MIN_DISTANCE = 120
        val SWIPE_THRESHOLD_VELOCITY = 200
        val game_layout = findViewById<RelativeLayout>(R.id.game_layout)

        game_layout.setOnTouchListener(object : View.OnTouchListener{
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                var x = event?.getX()
                var y = event?.getY()
                println("$x $y")
                //onFling()
                println("touch")
                return true
            }
            fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
                if (e1.x - e2.x > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    //return false // справа налево
                    println("left")
                } else if (e2.x - e1.x > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    //return false // слева направо
                    println("right")
                }

                if (e1.y - e2.y > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                    //return false // снизу вверх
                    println("top")
                } else if (e2.y - e1.y > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                    //return false // сверху вниз
                    println("down")
                }
                return false
            }
        })*/


        menu.setOnClickListener {
            pauseTimer()

            val intent = Intent(this, MainActivity::class.java)
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
                    (LeftInMillis).toString()))

            Mas_sten_str = ""
            for (i in 0 until (klet_height * (klet_width - 1)) + (klet_width * (klet_height - 1))) {
                Mas_sten_str += Mas_sten[i]
                Mas_sten_str += " "
            }
            Mas_sten_str = substring(Mas_sten_str, 0, Mas_sten_str.length - 1)

            dataBase.clear_mas()
            dataBase.add_mas(GameMassivActivity(
                    (Mas_klet_str),
                    (Mas_sten_str),
                    (win_sten).toString()))

            dataBase.clear_dif()
            dataBase.add_dif(Dif_GameOverActivity(
                    (Dif),
                    (game_over)))

            intent.putExtra("But_home", ("no").toString())
            Sound.stop()
            finish()
            startActivity(intent)
            overridePendingTransition(R.anim.right,R.anim.alpha)
        }

        restart.setOnClickListener {
            var run = true
            if (Running == false) run = false
            pauseTimer()

            val dial_view = LayoutInflater.from(this).inflate(R.layout.dialog_main, null)
            val builder = AlertDialog.Builder(this)
                    .setView(dial_view)
            val AlertDialog = builder.show()
            dial_view.yes.setOnClickListener {
                AlertDialog.dismiss()
                resetTimer()
                startTimer()
                Xpoint = ((0..klet_width).random()) * kletka + kletka / 2
                Ypoint = ((0..klet_height).random()) * kletka + kletka / 2
                Xpoint_start = Xpoint
                Ypoint_start = Ypoint
                Xpoint_old = Xpoint
                Ypoint_old = Ypoint
                gener_all()
                background.invalidate()
                if (run == true) {
                    win_game = false
                    statistic()
                }
                game_over = "no"
                printkey = false
                Sound.stop()
                Sound = MediaPlayer.create(this, R.raw.drop)
                Sound.start()
                Sound.setOnCompletionListener( MediaPlayer.OnCompletionListener {
                    Sound.start()
                })
            }
            dial_view.no.setOnClickListener {
                AlertDialog.dismiss()
                if (run == true) startTimer()
                if (run == false) {
                    game_over = "yes"
                }
                else game_over = "no"
            }
            AlertDialog.setCancelable(false)
        }

        surrender.setOnClickListener{
            var run = true
            if (Running == false) run = false
            pauseTimer()
            if (run == true) {
                val dial_view = LayoutInflater.from(this).inflate(R.layout.dialog_main_surr, null)
                val builder = AlertDialog.Builder(this)
                        .setView(dial_view)
                val AlertDialog = builder.show()
                dial_view.yes.setOnClickListener {
                    AlertDialog.dismiss()
                    for (i in 0 until ((klet_height * (klet_width - 1)) + (klet_width * (klet_height - 1)))) {
                        if (Mas_sten[i] == 1) Mas_sten[i] = 2
                    }
                    Xpoint = Xpoint_start
                    Ypoint = Ypoint_start
                    Xpoint_old = Xpoint_start
                    Ypoint_old = Ypoint_start
                    key_yes = true
                    if (key_yes_was == false) {
                        printkey = true
                    }
                    background.invalidate()
                    game_over = "yes"
                    win_game = false
                    statistic()
                    Sound.stop()
                    Sound = MediaPlayer.create(this, R.raw.lose)
                    Sound.start()
                }
                dial_view.no.setOnClickListener {
                    AlertDialog.dismiss()
                    startTimer()
                }
                AlertDialog.setCancelable(false)
            }
            else {
                val dial_view = LayoutInflater.from(this).inflate(R.layout.dialog_main_surr2, null)
                val builder = AlertDialog.Builder(this)
                        .setView(dial_view)
                val AlertDialog = builder.show()
                dial_view.ok.setOnClickListener {
                    AlertDialog.dismiss()
                }
                AlertDialog.setCancelable(false)
            }
        }

        top.setOnClickListener {
            if ((game_over != "yes")&&(clach == false)) {
                if (((Ypoint - kletka / 2) / kletka == 0) && ((Xpoint - kletka / 2) / kletka == win_sten) && (win_sten in (0..klet_width)) && (key_yes == true)) {
                    pauseTimer()
                    for (i in 0 until ((klet_height * (klet_width - 1)) + (klet_width * (klet_height - 1)))) {
                        if (Mas_sten[i] == 1) Mas_sten[i] = 2
                    }
                    Xpoint = Xpoint_start
                    Ypoint = Ypoint_start
                    Xpoint_old = Xpoint_start
                    Ypoint_old = Ypoint_start
                    game_over = "yes"
                    win_game = true
                    Sound.stop()
                    Sound = MediaPlayer.create(this, R.raw.win)
                    Sound.start()
                    statistic()
                    background.invalidate()
                } else if ((Ypoint < kletka) ||
                        (Mas_klet[(Ypoint - kletka / 2) / kletka][(Xpoint - kletka / 2) / kletka] == 11000) ||
                        (Mas_klet[(Ypoint - kletka / 2) / kletka][(Xpoint - kletka / 2) / kletka] == 21200) ||
                        (Mas_klet[(Ypoint - kletka / 2) / kletka][(Xpoint - kletka / 2) / kletka] == 21030) ||
                        (Mas_klet[(Ypoint - kletka / 2) / kletka][(Xpoint - kletka / 2) / kletka] == 21004) ||
                        (Mas_klet[(Ypoint - kletka / 2) / kletka][(Xpoint - kletka / 2) / kletka] == 31230) ||
                        (Mas_klet[(Ypoint - kletka / 2) / kletka][(Xpoint - kletka / 2) / kletka] == 31204) ||
                        (Mas_klet[(Ypoint - kletka / 2) / kletka][(Xpoint - kletka / 2) / kletka] == 31034) ||
                        (Mas_klet[(Ypoint - kletka / 2) / kletka][(Xpoint - kletka / 2) / kletka] == 41234)) {

                    if (Ypoint >= kletka) {
                        Mas_sten[klet_height * (klet_width - 1) + (Xpoint - kletka / 2) / kletka * (klet_width - 1) + (Ypoint - kletka / 2) / kletka - 1] = 2
                        clach = true
                        run(klet_height * (klet_width - 1) + (Xpoint - kletka / 2) / kletka * (klet_width - 1) + (Ypoint - kletka / 2) / kletka - 1)
                    }

                    if (key_yes == true) {
                        Xkey = Xpoint
                        Ykey = Ypoint
                    }
                    Xpoint = Xpoint_start
                    Ypoint = Ypoint_start
                    Xpoint_old = Xpoint
                    Ypoint_old = Ypoint
                    key_yes = false
                } else {
                    Xpoint_old = Xpoint
                    Ypoint_old = Ypoint
                    Ypoint -= kletka
                    key_was()
                    key_be()
                }
                background.invalidate()
            }
        }

        down.setOnClickListener {
            if ((game_over != "yes")&&(clach == false)) {
                if (((Ypoint - kletka / 2) / kletka == klet_height - 1) && ((Xpoint - kletka / 2) / kletka == win_sten - klet_width) && (win_sten in (klet_width..(2 * klet_width))) && (key_yes == true)) {
                    pauseTimer()
                    for (i in 0 until ((klet_height * (klet_width - 1)) + (klet_width * (klet_height - 1)))) {
                        if (Mas_sten[i] == 1) Mas_sten[i] = 2
                    }
                    Xpoint = Xpoint_start
                    Ypoint = Ypoint_start
                    Xpoint_old = Xpoint_start
                    Ypoint_old = Ypoint_start
                    game_over = "yes"
                    win_game = true
                    Sound.stop()
                    Sound = MediaPlayer.create(this, R.raw.win)
                    Sound.start()
                    statistic()
                    background.invalidate()
                } else if ((Ypoint > max_height - kletka) ||
                        (Mas_klet[(Ypoint - kletka / 2) / kletka][(Xpoint - kletka / 2) / kletka] == 10030) ||
                        (Mas_klet[(Ypoint - kletka / 2) / kletka][(Xpoint - kletka / 2) / kletka] == 21030) ||
                        (Mas_klet[(Ypoint - kletka / 2) / kletka][(Xpoint - kletka / 2) / kletka] == 20230) ||
                        (Mas_klet[(Ypoint - kletka / 2) / kletka][(Xpoint - kletka / 2) / kletka] == 20034) ||
                        (Mas_klet[(Ypoint - kletka / 2) / kletka][(Xpoint - kletka / 2) / kletka] == 31230) ||
                        (Mas_klet[(Ypoint - kletka / 2) / kletka][(Xpoint - kletka / 2) / kletka] == 30234) ||
                        (Mas_klet[(Ypoint - kletka / 2) / kletka][(Xpoint - kletka / 2) / kletka] == 31034) ||
                        (Mas_klet[(Ypoint - kletka / 2) / kletka][(Xpoint - kletka / 2) / kletka] == 41234)) {

                    if (Ypoint <= max_height - kletka) {
                        Mas_sten[klet_height * (klet_width - 1) + (Xpoint - kletka / 2) / kletka * (klet_width - 1) + (Ypoint - kletka / 2) / kletka] = 2
                        clach = true
                        run(klet_height * (klet_width - 1) + (Xpoint - kletka / 2) / kletka * (klet_width - 1) + (Ypoint - kletka / 2) / kletka)
                    }

                    if (key_yes == true) {
                        Xkey = Xpoint
                        Ykey = Ypoint
                    }
                    Xpoint = Xpoint_start
                    Ypoint = Ypoint_start
                    Xpoint_old = Xpoint
                    Ypoint_old = Ypoint
                    key_yes = false
                } else {
                    Xpoint_old = Xpoint
                    Ypoint_old = Ypoint
                    Ypoint += kletka
                    key_was()
                    key_be()
                }
                background.invalidate()
            }
        }

        right.setOnClickListener {
            if ((game_over != "yes")&&(clach == false)) {
                if (((Xpoint - kletka / 2) / kletka == klet_width - 1) && ((Ypoint - kletka / 2) / kletka == win_sten - 2 * klet_width - klet_height) && (win_sten in ((2 * klet_width + klet_height)..(2 * (klet_width + klet_height)))) && (key_yes == true)) {
                    pauseTimer()
                    for (i in 0 until ((klet_height * (klet_width - 1)) + (klet_width * (klet_height - 1)))) {
                        if (Mas_sten[i] == 1) Mas_sten[i] = 2
                    }
                    Xpoint = Xpoint_start
                    Ypoint = Ypoint_start
                    Xpoint_old = Xpoint_start
                    Ypoint_old = Ypoint_start
                    game_over = "yes"
                    win_game = true
                    Sound.stop()
                    Sound = MediaPlayer.create(this, R.raw.win)
                    Sound.start()
                    statistic()
                    background.invalidate()
                } else if ((Xpoint > max_width - kletka) ||
                        (Mas_klet[(Ypoint - kletka / 2) / kletka][(Xpoint - kletka / 2) / kletka] == 10200) ||
                        (Mas_klet[(Ypoint - kletka / 2) / kletka][(Xpoint - kletka / 2) / kletka] == 21200) ||
                        (Mas_klet[(Ypoint - kletka / 2) / kletka][(Xpoint - kletka / 2) / kletka] == 20230) ||
                        (Mas_klet[(Ypoint - kletka / 2) / kletka][(Xpoint - kletka / 2) / kletka] == 20204) ||
                        (Mas_klet[(Ypoint - kletka / 2) / kletka][(Xpoint - kletka / 2) / kletka] == 31230) ||
                        (Mas_klet[(Ypoint - kletka / 2) / kletka][(Xpoint - kletka / 2) / kletka] == 31204) ||
                        (Mas_klet[(Ypoint - kletka / 2) / kletka][(Xpoint - kletka / 2) / kletka] == 30234) ||
                        (Mas_klet[(Ypoint - kletka / 2) / kletka][(Xpoint - kletka / 2) / kletka] == 41234)) {

                    if (Xpoint <= max_width - kletka) {
                        Mas_sten[(Ypoint - kletka / 2) / kletka * (klet_width - 1) + (Xpoint - kletka / 2) / kletka] = 2
                        clach = true
                        run((Ypoint - kletka / 2) / kletka * (klet_width - 1) + (Xpoint - kletka / 2) / kletka)
                    }

                    if (key_yes == true) {
                        Xkey = Xpoint
                        Ykey = Ypoint
                    }
                    Xpoint = Xpoint_start
                    Ypoint = Ypoint_start
                    Xpoint_old = Xpoint
                    Ypoint_old = Ypoint
                    key_yes = false
                } else {
                    Xpoint_old = Xpoint
                    Ypoint_old = Ypoint
                    Xpoint += kletka
                    key_was()
                    key_be()
                }
                background.invalidate()
            }
        }

        left.setOnClickListener {
            if ((game_over != "yes")&&(clach == false)) {
                if (((Xpoint - kletka / 2) / kletka == 0) && ((Ypoint - kletka / 2) / kletka == win_sten - 2 * klet_width) && (win_sten in (2 * klet_width..(2 * klet_width + klet_height))) && (key_yes == true)) {
                    pauseTimer()
                    for (i in 0 until ((klet_height * (klet_width - 1)) + (klet_width * (klet_height - 1)))) {
                        if (Mas_sten[i] == 1) Mas_sten[i] = 2
                    }
                    Xpoint = Xpoint_start
                    Ypoint = Ypoint_start
                    Xpoint_old = Xpoint_start
                    Ypoint_old = Ypoint_start
                    game_over = "yes"
                    win_game = true
                    Sound.stop()
                    Sound = MediaPlayer.create(this, R.raw.win)
                    Sound.start()
                    statistic()
                    background.invalidate()
                } else if ((Xpoint < kletka) ||
                        (Mas_klet[(Ypoint - kletka / 2) / kletka][(Xpoint - kletka / 2) / kletka] == 10004) ||
                        (Mas_klet[(Ypoint - kletka / 2) / kletka][(Xpoint - kletka / 2) / kletka] == 21004) ||
                        (Mas_klet[(Ypoint - kletka / 2) / kletka][(Xpoint - kletka / 2) / kletka] == 20034) ||
                        (Mas_klet[(Ypoint - kletka / 2) / kletka][(Xpoint - kletka / 2) / kletka] == 20204) ||
                        (Mas_klet[(Ypoint - kletka / 2) / kletka][(Xpoint - kletka / 2) / kletka] == 30234) ||
                        (Mas_klet[(Ypoint - kletka / 2) / kletka][(Xpoint - kletka / 2) / kletka] == 31204) ||
                        (Mas_klet[(Ypoint - kletka / 2) / kletka][(Xpoint - kletka / 2) / kletka] == 31034) ||
                        (Mas_klet[(Ypoint - kletka / 2) / kletka][(Xpoint - kletka / 2) / kletka] == 41234)) {

                    if (Xpoint >= kletka) {
                        Mas_sten[(Ypoint - kletka / 2) / kletka * (klet_width - 1) + (Xpoint - kletka / 2) / kletka - 1] = 2
                        clach = true
                        run((Ypoint - kletka / 2) / kletka * (klet_width - 1) + (Xpoint - kletka / 2) / kletka - 1)
                    }

                    if (key_yes == true) {
                        Xkey = Xpoint
                        Ykey = Ypoint
                    }
                    Xpoint = Xpoint_start
                    Ypoint = Ypoint_start
                    Xpoint_old = Xpoint
                    Ypoint_old = Ypoint
                    key_yes = false
                } else {
                    Xpoint_old = Xpoint
                    Ypoint_old = Ypoint
                    Xpoint -= kletka
                    key_was()
                    key_be()
                }
                background.invalidate()
            }
        }

        timer = findViewById(R.id.timer)
        updateCountDownText()
    }

    fun run(I: Int) {
        val background2 = Canvasss(this)
        layout1.addView(background2)
        II = I
        if ((Dif == "ЛЕГКО") || (Dif == "НОРМАЛЬНО")) {
            fx = 266
            fy = 2
            fz = 2
            CountDownTimer2 = object : CountDownTimer(LeftInMillis2, 50) {
                override fun onTick(millisUntilFinished: Long) {
                    LeftInMillis2 = millisUntilFinished
                    updateCountDownText()
                    fx = fx - 17
                    fy = fy + 7
                    fz = fz + 7
                    background2.invalidate()
                }

                override fun onFinish() {
                    runnin = false
                    rerun()
                    background2.invalidate()
                    clach = false
                }
            }.start()
            runnin = true
        } else {
            fx = 258
            fy = -6
            fz = -6
            CountDownTimer3 = object : CountDownTimer(LeftInMillis2, 50) {
                override fun onTick(millisUntilFinished: Long) {
                    LeftInMillis2 = millisUntilFinished
                    updateCountDownText()
                    fx = fx - 9
                    fy = fy + 15
                    fz = fz + 15
                    background2.invalidate()
                }

                override fun onFinish() {
                    runnin = false
                    rerun()
                    background2.invalidate()
                    clach = false
                }
            }.start()
            runnin = true
        }
    }

    fun rerun(){
        if ((Dif == "ЛЕГКО")||(Dif == "НОРМАЛЬНО")) CountDownTimer2!!.cancel()
        else CountDownTimer3!!.cancel()
        LeftInMillis2 = 525
    }

    override fun onUserLeaveHint() {
        pauseTimer()

        val intent = Intent(this, MainActivity::class.java)
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
                (LeftInMillis).toString()))

        var Mas_sten_str : String =""
        for (i in 0 until (klet_height * (klet_width - 1)) + (klet_width * (klet_height - 1))) {
            Mas_sten_str += Mas_sten[i]
            Mas_sten_str += " "
        }
        Mas_sten_str = substring(Mas_sten_str, 0, Mas_sten_str.length - 1)

        dataBase.clear_mas()
        dataBase.add_mas(GameMassivActivity(
                (Mas_klet_str),
                (Mas_sten_str),
                (win_sten).toString()))

        dataBase.clear_dif()
        dataBase.add_dif(Dif_GameOverActivity(
                (Dif),
                (game_over)))

        intent.putExtra("But_home", ("yes").toString())
        Sound.stop()
        finish()
        startActivity(intent)
    }

    override fun onBackPressed() {
        Sound.stop()
        super.onBackPressed()
    }

    fun ClosedRange<Int>.random() =
            Random().nextInt(endInclusive - start) + start

    init {
        paint.isFilterBitmap = true
        paint.isAntiAlias = true
    }

    inner class Canvass(context: Context) : View(context) {

        override fun onDraw(canvas: Canvas) {

            val display = windowManager.defaultDisplay
            val metricsB = DisplayMetrics()
            display.getMetrics(metricsB)
            val max_height = metricsB.heightPixels
            val kletka = max_height / klet_height
            val max_width = kletka * klet_width


            paint.color = Color.rgb(128,128,128)
            canvas.drawRect((0).toFloat(), (0).toFloat(), (max_width).toFloat(), (max_height).toFloat(), paint)
            paint.color = Color.rgb(169, 169, 169)
            run {
                var i = kletka
                while (i <= max_width) {
                    canvas.drawLine((i).toFloat(), (0).toFloat(), (i).toFloat(), (max_height).toFloat(), paint)
                    i += kletka
                }
            }
            var i = kletka
            while (i <= max_height) {
                canvas.drawLine((0).toFloat(), (i).toFloat(), (max_width).toFloat(), (i).toFloat(), paint)
                i += kletka
            }

            paint.color = Color.rgb(0,0,0)
            canvas.drawLine((0).toFloat(), (max_height).toFloat(), (max_width).toFloat(), (max_height).toFloat(), paint)
            canvas.drawLine((0).toFloat(), (0).toFloat(), (max_width).toFloat(), (0).toFloat(), paint)
            canvas.drawLine((max_width).toFloat(), (0).toFloat(), (max_width).toFloat(), (max_height).toFloat(), paint)
            canvas.drawLine((0).toFloat(), (0).toFloat(), (0).toFloat(), (max_height).toFloat(), paint)

            paint.color = Color.rgb(250, 231, 181)
            //canvas.drawRect((Xpoint - kletka / 2 + 1).toFloat(), (Ypoint - kletka / 2 + 1).toFloat(), (Xpoint + kletka / 2 - 1).toFloat(), (Ypoint + kletka / 2 - 1).toFloat(), paint)
            canvas.drawRect((Xpoint_old - kletka / 2 + 1).toFloat(), (Ypoint_old - kletka / 2 + 1).toFloat(), (Xpoint_old + kletka / 2 - 1).toFloat(), (Ypoint_old + kletka / 2 - 1).toFloat(), paint)

                if ((key_yes == false)||((game_over == "yes")&&(win_game == false)))
                canvas.drawBitmap(playerBitmap, (Xpoint - kletka / 2 + 1).toFloat(), (Ypoint - kletka / 2 + 1).toFloat(), paint)
            else canvas.drawBitmap(playerBitmap2, (Xpoint - kletka / 2 + 1).toFloat(), (Ypoint - kletka / 2 + 1).toFloat(), paint)

            /*paint.color = Color.RED
            for (i in 0 until klet_height * (klet_width - 1)) {
                if (Mas_sten[i] == 1) {
                    canvas.drawLine(((i % (klet_width - 1)) * kletka + kletka).toFloat(), (0 + kletka * (i / (klet_width - 1))).toFloat(), ((i % (klet_width - 1)) * kletka + kletka).toFloat(), (kletka + kletka * (i / (klet_width - 1))).toFloat(), paint)
                }
            }
            for (i in klet_height * (klet_width - 1) until ((klet_height * (klet_width - 1)) + (klet_width * (klet_height - 1)))) {
                if (Mas_sten[i] == 1) {
                    canvas.drawLine((0 + kletka * ((i - klet_height * (klet_width - 1)) / (klet_height - 1))).toFloat(), ((i - klet_height * (klet_width - 1)) % (klet_height - 1) * kletka + kletka).toFloat(), (kletka + kletka * ((i - klet_height * (klet_width - 1)) / (klet_height - 1))).toFloat(), ((i - klet_height * (klet_width - 1)) % (klet_height - 1) * kletka + kletka).toFloat(), paint)
                }
            }*/

            if ((Dif == "ЛЕГКО")||(Dif == "НОРМАЛЬНО")||(game_over == "yes")) {
                paint.color = Color.rgb(0,0,0)
                for (i in 0 until klet_height * (klet_width - 1)) {
                    if (Mas_sten[i] == 2) {
                        canvas.drawLine(((i % (klet_width - 1)) * kletka + kletka).toFloat(), (0 + kletka * (i / (klet_width - 1))).toFloat(), ((i % (klet_width - 1)) * kletka + kletka).toFloat(), (kletka + kletka * (i / (klet_width - 1))).toFloat(), paint)
                    }
                }
                for (i in klet_height * (klet_width - 1) until ((klet_height * (klet_width - 1)) + (klet_width * (klet_height - 1)))) {
                    if (Mas_sten[i] == 2) {
                        canvas.drawLine((0 + kletka * ((i - klet_height * (klet_width - 1)) / (klet_height - 1))).toFloat(), ((i - klet_height * (klet_width - 1)) % (klet_height - 1) * kletka + kletka).toFloat(), (kletka + kletka * ((i - klet_height * (klet_width - 1)) / (klet_height - 1))).toFloat(), ((i - klet_height * (klet_width - 1)) % (klet_height - 1) * kletka + kletka).toFloat(), paint)
                    }
                }
            }

            if (key_yes == true) {
                paint.color = Color.GREEN
                if (win_sten < klet_width) canvas.drawRect((0 + kletka * win_sten).toFloat(), (0).toFloat(), (kletka + kletka * win_sten).toFloat(), (5).toFloat(), paint)
                else if (win_sten < klet_width * 2) canvas.drawRect((0 + kletka * (win_sten - klet_width)).toFloat(), (max_height - 5).toFloat(), (kletka + kletka * (win_sten - klet_width)).toFloat(), (max_height).toFloat(), paint)
                else if (win_sten < klet_height + klet_width * 2) canvas.drawRect((0).toFloat(), (0 + kletka * (win_sten - klet_width * 2)).toFloat(), (5).toFloat(), (kletka + kletka * (win_sten - klet_width * 2)).toFloat(), paint)
                else if (win_sten < (klet_height + klet_width) * 2) canvas.drawRect((max_width - 5).toFloat(), (0 + kletka * (win_sten - klet_width * 2 - klet_height)).toFloat(), (max_width).toFloat(), (kletka + kletka * (win_sten - klet_width * 2 - klet_height)).toFloat(), paint)
            }

            //paint.color = Color.RED
            //for (i in 0 until klet_height) {
            //    for (j in 0 until klet_width) {
            //        if (Vozm_klet[i][j] == 1) canvas.drawCircle((j*kletka + kletka / 2).toFloat(), (i*kletka  + kletka / 2).toFloat(), (3).toFloat(), paint)
            //    }
            //}

            if ((((key_yes_was == true)||(Dif == "ЛЕГКО"))&&(key_yes == false))||(printkey == true)) {
                canvas.drawBitmap(playerBitmap3, (Xkey - kletka / 2 + 1).toFloat(), (Ykey - kletka / 2 + 1).toFloat(), paint)
            }

            /*else if (key_yes_was == false) {
                paint.color = Color.MAGENTA
                canvas.drawCircle((Xkey).toFloat(), (Ykey).toFloat(), (10).toFloat(), paint)
            }*/
        }
    }


    inner class Canvasss(context: Context) : View(context) {
        override fun onDraw(canvas: Canvas) {

            val display = windowManager.defaultDisplay
            val metricsB = DisplayMetrics()
            display.getMetrics(metricsB)
            val max_height = metricsB.heightPixels
            val kletka = max_height / klet_height
            val max_width = kletka * klet_width
            if (runnin == true) {
                paint.color = Color.rgb(fx, fy, fz)
                if (II in 0 until klet_height * (klet_width - 1))
                    canvas.drawLine(((II % (klet_width - 1)) * kletka + kletka).toFloat(), (0 + kletka * (II / (klet_width - 1))).toFloat(), ((II % (klet_width - 1)) * kletka + kletka).toFloat(), (kletka + kletka * (II / (klet_width - 1))).toFloat(), paint)
                else canvas.drawLine((0 + kletka * ((II - klet_height * (klet_width - 1)) / (klet_height - 1))).toFloat(), ((II - klet_height * (klet_width - 1)) % (klet_height - 1) * kletka + kletka).toFloat(), (kletka + kletka * ((II - klet_height * (klet_width - 1)) / (klet_height - 1))).toFloat(), ((II - klet_height * (klet_width - 1)) % (klet_height - 1) * kletka + kletka).toFloat(), paint)
            }
            else {
                paint.color = Color.argb(0, 0, 0, 0)
                canvas.drawRect((0).toFloat(), (0).toFloat(), (max_width).toFloat(), (max_height).toFloat(), paint)
            }
        }
    }

    fun startTimer() {
        CountDownTimer = object : CountDownTimer(LeftInMillis, 100) {
            override fun onTick(millisUntilFinished: Long) {
                LeftInMillis = millisUntilFinished
                updateCountDownText()
            }

            override fun onFinish() {
                for (i in 0 until ((klet_height * (klet_width - 1)) + (klet_width * (klet_height - 1)))) {
                    if (Mas_sten[i] == 1) Mas_sten[i] = 2
                }
                Xpoint = Xpoint_start
                Ypoint = Ypoint_start
                Xpoint_old = Xpoint_start
                Ypoint_old = Ypoint_start
                game_over = "yes"
                Running = false
                win_game = false
                key_yes = true
                if (key_yes_was == false) {
                    printkey = true
                }
                statistic()
                runtime()

                Sound.stop()
                Sound = MediaPlayer.create(this@GameActivity, R.raw.lose)
                Sound.start()
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
        val minutes = ((all_time - LeftInMillis) / 1000).toInt() / 60
        val seconds = ((all_time - LeftInMillis) / 1000).toInt() % 60
        val timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
        timer!!.text = timeLeftFormatted
    }

    companion object {
        val all_time: Long = 3600000
    }


    fun generate() {
        Mas_klet_str = ""
        Mas_sten_str = ""
        Mas_klet = Array(klet_height, { Array(klet_width, { 10000 }) })
        Mas_sten = Array(((klet_height * (klet_width - 1)) + (klet_width * (klet_height - 1))), { 0 })
        for (i in 0 until generate_sten) {
            n = (0..((klet_height * (klet_width - 1)) + (klet_width * (klet_height - 1)))).random()
            if (n < (klet_height * (klet_width - 1))) {
                Mas_klet[n / (klet_width - 1)][n % (klet_width - 1)] = Mas_klet[n / (klet_width - 1)][n % (klet_width - 1)] / 1000 * 1000 + Mas_klet[n / (klet_width - 1)][n % (klet_width - 1)] % 100 + 200
                Mas_klet[n / (klet_width - 1)][n % (klet_width - 1) + 1] = Mas_klet[n / (klet_width - 1)][n % (klet_width - 1) + 1] / 10 * 10 + 4
                Mas_sten[n] = 1
            } else {
                n = n - (klet_height * (klet_width - 1))
                Mas_klet[n % (klet_height - 1)][n / (klet_height - 1)] = Mas_klet[n % (klet_height - 1)][n / (klet_height - 1)] / 100 * 100 + Mas_klet[n % (klet_height - 1)][n / (klet_height - 1)] % 10 + 30
                Mas_klet[n % (klet_height - 1) + 1][n / (klet_height - 1)] = Mas_klet[n % (klet_height - 1) + 1][n / (klet_height - 1)] / 10000 * 10000 + Mas_klet[n % (klet_height - 1) + 1][n / (klet_height - 1)] % 1000 + 1000
                Mas_sten[n + (klet_height * (klet_width - 1))] = 1
            }
        }

        for (i in 0 until klet_height) {
            for (j in 0 until klet_width) {
                if (Mas_klet[i][j] % 10 != 0) k = k + 1
                if (Mas_klet[i][j] % 100 - Mas_klet[i][j] % 10 != 0) k = k + 1
                if (Mas_klet[i][j] % 1000 - Mas_klet[i][j] % 100 != 0) k = k + 1
                if (Mas_klet[i][j] % 10000 - Mas_klet[i][j] % 1000 != 0) k = k + 1
                Mas_klet[i][j] = 10000 * k + Mas_klet[i][j] % 10000
                k = 0
                Mas_klet_str += Mas_klet[i][j]
                Mas_klet_str += " "
            }
        }
        Mas_klet_str = substring(Mas_klet_str, 0, Mas_klet_str.length - 1)

        for (i in 0 until (klet_height * (klet_width - 1)) + (klet_width * (klet_height - 1))) {
            Mas_sten_str += Mas_sten[i]
            Mas_sten_str += " "
        }
        Mas_sten_str = substring(Mas_sten_str, 0, Mas_sten_str.length - 1)
    }


    fun Wave(i: Int, j: Int): Unit {
        l = 0
        if (Vozm_klet[i][j] == 0) l = 1
        Vozm_klet[i][j] = 1
        if (l == 1){
            if (Mas_klet[i][j] % 10 == 0){
                if (j > 0){

                    Wave(i, j - 1)
                }
            }
            if (Mas_klet[i][j] % 100 - Mas_klet[i][j] % 10 == 0){
                if (i < klet_height - 1){
                    Wave(i + 1,j)
                }
            }
            if (Mas_klet[i][j] % 1000 - Mas_klet[i][j] % 100 == 0){
                if (j < klet_width - 1){
                    Wave(i,j + 1)
                }
            }
            if (Mas_klet[i][j] % 10000 - Mas_klet[i][j] % 1000 == 0){
                if (i > 0){
                    Wave(i - 1,j)
                }
            }
        }
    }

    fun Dostup_stenka(){
        i = 0
        for (k in 0 until klet_width){
            if (Vozm_klet[0][k] == 1) {
                Dostup_stenka[i] = k
                i += 1
            }
        }
        for (k in klet_width until klet_width * 2){
            if (Vozm_klet[klet_height - 1][k - klet_width] == 1) {
                Dostup_stenka[i] = k
                i += 1
            }
        }
        for (k in klet_width * 2 until klet_width * 2 + klet_height){
            if (Vozm_klet[k - klet_width * 2][0] == 1) {
                Dostup_stenka[i] = k
                i += 1
            }
        }
        for (k in klet_width * 2 + klet_height until (klet_height + klet_width) * 2){
            if (Vozm_klet[k - klet_width * 2 - klet_height][klet_width - 1] == 1) {
                Dostup_stenka[i] = k
                i += 1
            }
        }
        if (i == 0) gener_all()
        else {
            println(i)
            println(Dostup_stenka.lastIndex)
            var do_win_sten: Int = (0..i).random()
            while (Dostup_stenka[do_win_sten] < 0) do_win_sten = (0..i).random()
            win_sten = Dostup_stenka[do_win_sten]
        }
    }

    fun gener_all(){
        val display = windowManager.defaultDisplay
        val metricsB = DisplayMetrics()
        display.getMetrics(metricsB)

        val max_height = metricsB.heightPixels
        val kletka = max_height / klet_height
        var k = 0
        while(k < 20) {
            generate()
            Vozm_klet = Array(klet_height, { Array(klet_width, { 0 }) })
            Wave((Ypoint_start - kletka / 2) / kletka, (Xpoint_start - kletka / 2) / kletka)
            k = 0
            for (i in 0 until klet_height) {
                for (j in 0 until klet_width) {
                    if (Vozm_klet[i][j] == 1) k += 1
                }
            }
        }
        Dostup_stenka()
        var key_width = (0..klet_width - 1).random()
        var key_height = (0..klet_height - 1).random()
        while (Vozm_klet[key_height][key_width] == 0) {
            key_width = (0..klet_width - 1).random()
            key_height = (0..klet_height - 1).random()
        }
        Xkey = key_width * kletka + kletka / 2
        Ykey = key_height * kletka + kletka / 2
        key_yes = false
        key_yes_was = false
        key_was()
        key_be()
    }

    fun key_be(){
        val display = windowManager.defaultDisplay
        val metricsB = DisplayMetrics()
        display.getMetrics(metricsB)

        val max_height = metricsB.heightPixels
        val kletka = max_height / klet_height
        key_yes1 = key_yes
        if ((Xpoint == Xkey)&&(Ypoint == Ykey)) key_yes = true
        if ((Dif == "ХАРД")&&(key_yes == true)&&(key_yes1 == false)) {
            Wave((Ypoint_start - kletka / 2) / kletka, (Xpoint_start - kletka / 2) / kletka)
            Dostup_stenka()
        }
    }

    fun key_was(){
        if ((Xpoint == Xkey)&&(Ypoint == Ykey)) key_yes_was = true
    }

    fun runtime(){
        val dial_view = LayoutInflater.from(this).inflate(R.layout.dialog_main_timer, null)
        val builder = AlertDialog.Builder(this)
                .setView(dial_view)
        val AlertDialog = builder.show()
        dial_view.ok.setOnClickListener {
            AlertDialog.dismiss()
        }
        AlertDialog.setCancelable(false)
        val layout1 = findViewById<ConstraintLayout>(R.id.layout1)
        val background = Canvass(this)
        layout1.addView(background)
        background.invalidate()
    }

    fun statistic() {
        val scores3 = dataBase.read_stat()
        if (scores3 != null) {
            Kol_games_easy = (scores3.Kol_games_easy).toInt()
            Kol_wins_easy = (scores3.Kol_wins_easy).toInt()
            All_time_easy = (scores3.Sred_time_easy).toLong()
            Min_time_easy = (scores3.Min_time_easy).toLong()
            Kol_games_normal = (scores3.Kol_games_normal).toInt()
            Kol_wins_normal = (scores3.Kol_wins_normal).toInt()
            All_time_normal = (scores3.Sred_time_normal).toLong()
            Min_time_normal = (scores3.Min_time_normal).toLong()
            Kol_games_dif = (scores3.Kol_games_dif).toInt()
            Kol_wins_dif = (scores3.Kol_wins_dif).toInt()
            All_time_dif = (scores3.Sred_time_dif).toLong()
            Min_time_dif = (scores3.Min_time_dif).toLong()
            Kol_games_hard = (scores3.Kol_games_hard).toInt()
            Kol_wins_hard = (scores3.Kol_wins_hard).toInt()
            All_time_hard = (scores3.Sred_time_hard).toLong()
            Min_time_hard = (scores3.Min_time_hard).toLong()
        }
        dataBase.clear_stat()
        if (Dif == "ЛЕГКО") {
            Kol_games_easy++
            if (win_game == true) {
                Kol_wins_easy++
                All_time_easy = All_time_easy + (all_time - LeftInMillis) / 1000
                if (Min_time_easy > (all_time - LeftInMillis) / 1000) Min_time_easy = (all_time - LeftInMillis) / 1000
            }
        }
        else if (Dif == "НОРМАЛЬНО") {
            Kol_games_normal++
            if (win_game == true) {
                Kol_wins_normal++
                All_time_normal = All_time_normal + (all_time - LeftInMillis) / 1000
                if (Min_time_normal > (all_time - LeftInMillis) / 1000) Min_time_normal = (all_time - LeftInMillis) / 1000
            }
        }
        else if (Dif == "СЛОЖНО") {
            Kol_games_dif++
            if (win_game == true) {
                Kol_wins_dif++
                All_time_dif = All_time_dif + (all_time - LeftInMillis) / 1000
                if (Min_time_dif > (all_time - LeftInMillis) / 1000) Min_time_dif = (all_time - LeftInMillis) / 1000
            }
        }
        else if (Dif == "ХАРД") {
            Kol_games_hard++
            if (win_game == true) {
                Kol_wins_hard++
                All_time_hard = All_time_hard + (all_time - LeftInMillis) / 1000
                if (Min_time_hard > (all_time - LeftInMillis) / 1000) Min_time_hard = (all_time - LeftInMillis) / 1000
            }
        }

        dataBase.add_stat(GameStatisticActivity(
                (Kol_games_easy).toString(),
                (Kol_wins_easy).toString(),
                (All_time_easy).toString(),
                (Min_time_easy).toString(),
                (Kol_games_normal).toString(),
                (Kol_wins_normal).toString(),
                (All_time_normal).toString(),
                (Min_time_normal).toString(),
                (Kol_games_dif).toString(),
                (Kol_wins_dif).toString(),
                (All_time_dif).toString(),
                (Min_time_dif).toString(),
                (Kol_games_hard).toString(),
                (Kol_wins_hard).toString(),
                (All_time_hard).toString(),
                (Min_time_hard).toString()))
    }

    /*val SWIPE_MIN_DISTANCE = 120
    val SWIPE_THRESHOLD_VELOCITY = 200

    inner class GestureListener : SimpleOnGestureListener() {
        override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
            if (e1.x - e2.x > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                //return false // справа налево
                println("left")
            } else if (e2.x - e1.x > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                //return false // слева направо
                println("right")
            }

            if (e1.y - e2.y > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                //return false // снизу вверх
                println("top")
            } else if (e2.y - e1.y > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                //return false // сверху вниз
                println("down")
            }
            return false
        }
    }*/
}
