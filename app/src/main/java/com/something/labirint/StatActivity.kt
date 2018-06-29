package com.something.labirint

import android.content.Intent
import android.content.pm.ActivityInfo
import android.media.MediaPlayer
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import com.something.labirint.GameActivity.Companion.all_time
import kotlinx.android.synthetic.main.activity_stat.*
import java.util.*


class StatActivity : AppCompatActivity() {


    val dataBase = DBHelper(this)

    var Kol_games_easy = "0"
    var Kol_wins_easy = "0"
    var All_time_easy = "0"
    var Min_time_easy = (all_time).toString()
    var Kol_games_normal = "0"
    var Kol_wins_normal = "0"
    var All_time_normal = "0"
    var Min_time_normal = (all_time).toString()
    var Kol_games_dif = "0"
    var Kol_wins_dif = "0"
    var All_time_dif = "0"
    var Min_time_dif = (all_time).toString()
    var Kol_games_hard = "0"
    var Kol_wins_hard = "0"
    var All_time_hard = "0"
    var Min_time_hard = (all_time).toString()
    var Dif = ""
    lateinit var Sound : MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_stat)

        val dif = intent.getStringExtra("Dif")
        if (dif != null) Dif = (dif).toString()

        Sound = MediaPlayer.create(this, R.raw.sound1)
        Sound.start()
        Sound.setOnCompletionListener( MediaPlayer.OnCompletionListener {
            Sound.start()
        })

        menu2.setOnClickListener{
            val intent = Intent(this,MainActivity :: class.java )
            intent.putExtra("But_home", ("no").toString())
            Sound.stop()
            finish()
            startActivity(intent)
            overridePendingTransition(R.anim.right,R.anim.alpha)
        }

        val scores3 = dataBase.read_stat()
        if (scores3 != null) {
            Kol_games_easy = (scores3.Kol_games_easy)
            Kol_wins_easy = (scores3.Kol_wins_easy)
            All_time_easy = (scores3.Sred_time_easy)
            Min_time_easy = (scores3.Min_time_easy)
            Kol_games_normal = (scores3.Kol_games_normal)
            Kol_wins_normal = (scores3.Kol_wins_normal)
            All_time_normal = (scores3.Sred_time_normal)
            Min_time_normal = (scores3.Min_time_normal)
            Kol_games_dif = (scores3.Kol_games_dif)
            Kol_wins_dif = (scores3.Kol_wins_dif)
            All_time_dif = (scores3.Sred_time_dif)
            Min_time_dif = (scores3.Min_time_dif)
            Kol_games_hard = (scores3.Kol_games_hard)
            Kol_wins_hard = (scores3.Kol_wins_hard)
            All_time_hard = (scores3.Sred_time_hard)
            Min_time_hard = (scores3.Min_time_hard)
        }

        diffic.text = Dif
        if (Dif == "ЛЕГКО") {
            kol_gam.text = Kol_games_easy
            kol_wi.text = Kol_wins_easy
            if (Kol_wins_easy != "0") {
                val minutes = ((All_time_easy.toInt())/(Kol_wins_easy).toInt()) / 60
                val seconds = ((All_time_easy.toInt())/(Kol_wins_easy).toInt()) % 60
                val timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
                sred_tim!!.text = timeLeftFormatted
            }
            else sred_tim.text = "нет"
            if (Kol_wins_easy != "0") {
                val minutes = (Min_time_easy).toInt() / 60
                val seconds = (Min_time_easy).toInt() % 60
                val timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
                min_tim!!.text = timeLeftFormatted
            }
            else min_tim.text = "нет"
        }
        else if (Dif == "НОРМАЛЬНО") {
            kol_gam.text = Kol_games_normal
            kol_wi.text = Kol_wins_normal
            if (Kol_wins_normal != "0") {
                val minutes = ((All_time_normal.toInt())/(Kol_wins_normal).toInt()) / 60
                val seconds = ((All_time_normal.toInt())/(Kol_wins_normal).toInt()) % 60
                val timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
                sred_tim!!.text = timeLeftFormatted
            }
            else sred_tim.text = "нет"
            if (Kol_wins_normal != "0") {
                val minutes = (Min_time_normal).toInt() / 60
                val seconds = (Min_time_normal).toInt() % 60
                val timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
                min_tim!!.text = timeLeftFormatted
            }
            else min_tim.text = "нет"
        }
        else if (Dif == "СЛОЖНО") {
            kol_gam.text = Kol_games_dif
            kol_wi.text = Kol_wins_dif
            if (Kol_wins_dif != "0") {
                val minutes = ((All_time_dif.toInt())/(Kol_wins_dif).toInt()) / 60
                val seconds = ((All_time_dif.toInt())/(Kol_wins_dif).toInt()) % 60
                val timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
                sred_tim!!.text = timeLeftFormatted
            }
            else sred_tim.text = "нет"
            if (Kol_wins_dif != "0") {
                val minutes = (Min_time_dif).toInt() / 60
                val seconds = (Min_time_dif).toInt() % 60
                val timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
                min_tim!!.text = timeLeftFormatted
            }
            else min_tim.text = "нет"

        }
        else if (Dif == "ХАРД") {
            kol_gam.text = Kol_games_hard
            kol_wi.text = Kol_wins_hard
            if (Kol_wins_hard != "0") {
                val minutes = ((All_time_hard.toInt())/(Kol_wins_hard).toInt()) / 60
                val seconds = ((All_time_hard.toInt())/(Kol_wins_hard).toInt()) % 60
                val timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
                sred_tim!!.text = timeLeftFormatted
            }
            else sred_tim.text = "нет"
            if (Kol_wins_hard != "0") {
                val minutes = (Min_time_hard).toInt() / 60
                val seconds = (Min_time_hard).toInt() % 60
                val timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
                min_tim!!.text = timeLeftFormatted
            }
        }
    }

    override fun onUserLeaveHint() {

        val intent = Intent(this,MainActivity :: class.java )
        intent.putExtra("But_home", ("yes").toString())
        Sound.stop()
        finish()
        startActivity(intent)
    }
}
