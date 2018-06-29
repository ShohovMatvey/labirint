package com.something.labirint


import android.content.Intent
import android.media.MediaPlayer
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.service.chooser.ChooserTargetService
import android.support.v7.app.AlertDialog
import android.util.DisplayMetrics
import android.view.*
import android.widget.*
import com.something.labirint.GameActivity.Companion.all_time
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog_main.view.*
import java.util.*

class MainActivity : AppCompatActivity() {

    var klet_width : Int = 16
    var klet_height : Int = 16
    var k : Int = 0
    var n : Int = 0
    var Xkey = 0
    var Ykey = 0
    var key_yes_was = false
    var key_yes = false
    var dif : String = ""
    var game_over = "yes"

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
    var was_dif = ""
    lateinit var Sound : MediaPlayer
    var start = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_main)

        val display = windowManager.defaultDisplay
        val metricsB = DisplayMetrics()
        display.getMetrics(metricsB)

        val max_height = metricsB.heightPixels
        val kletka = max_height / klet_height

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

        val scores4 = dataBase.read_dif()
        if (scores4 != null) {
            dif = (scores4.dif)
            was_dif = (scores4.dif)
            game_over = (scores4.game_over)
        }

        if (dif == "ЛЕГКО") spinner.setSelection(0)
        else if (dif == "НОРМАЛЬНО") spinner.setSelection(1)
        else if (dif == "СЛОЖНО") spinner.setSelection(2)
        else if (dif == "ХАРД") spinner.setSelection(3)

        val but_home = intent.getStringExtra("But_home")
        if (but_home != null) But_home = (but_home).toString()
        if (But_home == "yes") {
            finish()
            super.onUserLeaveHint()
        }

        Sound = MediaPlayer.create(this, R.raw.sound1)
        if (start == false) {
            Sound.start()
            println("start1")
            Sound.setOnCompletionListener(MediaPlayer.OnCompletionListener {
                Sound.start()
            })
            start = true
        }


        contin.setOnClickListener {
            Sound.stop()
            start = false
            println("stop3")
            val intent = Intent(this, GameActivity::class.java)
            if (game_over == "yes"){
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
                game_over = "no"
                dataBase.clear_dif()
                dataBase.add_dif(Dif_GameOverActivity(
                        (dif),
                        (game_over)))


                intent.putExtra("New_game", ("yes").toString())
            }
            else intent.putExtra("New_game", ("no").toString())
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            overridePendingTransition(R.anim.left,R.anim.alpha)
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

            if (game_over == "no") statistic()
            game_over = "no"
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

            dataBase.clear_dif()
            dataBase.add_dif(Dif_GameOverActivity(
                    (dif),
                    (game_over)))

            intent.putExtra("New_game", ("yes").toString())
            Sound.stop()
            start = false
            println("stop1")
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            overridePendingTransition(R.anim.left,R.anim.alpha)
        }

        exit.setOnClickListener {
            val dial_view = LayoutInflater.from(this).inflate(R.layout.dialog_main_exit, null)
            val builder = AlertDialog.Builder(this)
                    .setView(dial_view)
            val AlertDialog = builder.show()
            dial_view.yes.setOnClickListener {
                AlertDialog.dismiss()
                Sound.stop()
                finish()
            }
            dial_view.no.setOnClickListener {
                AlertDialog.dismiss()
            }
            AlertDialog.setCancelable(false)
        }

        stat.setOnClickListener {
            Sound.stop()
            start = false
            println("stop2")
            val intent = Intent(this, StatActivity::class.java)
            intent.putExtra("Dif", dif)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            overridePendingTransition(R.anim.left,R.anim.alpha)
        }
    }

    override fun onUserLeaveHint() {
        Sound.stop()
        finish()
        super.onUserLeaveHint()
    }

    override fun onBackPressed() {
        Sound.stop()
        super.onBackPressed()
    }

    fun ClosedRange<Int>.random() =
            Random().nextInt(endInclusive - start) +  start

    fun statistic() {
        val dataBase = DBHelper(this)
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
        if (was_dif == "ЛЕГКО") Kol_games_easy++
        else if (was_dif == "НОРМАЛЬНО") Kol_games_normal++
        else if (was_dif == "СЛОЖНО") Kol_games_dif++
        else if (was_dif == "ХАРД") Kol_games_hard++

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
}