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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_main)
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        var Xpoint : Int = ((0..19).random())*30 + 15
        var Ypoint : Int = ((0..16).random())*30 + 15
        var Xpoint_start = Xpoint
        var Ypoint_start = Ypoint
        var Xpoint_old = Xpoint
        var Ypoint_old = Ypoint
        var Left_time : Long = all_time
        var But_home : String = "no"
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

        button1.setOnClickListener{
            val intent = Intent(this,GameActivity :: class.java )
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

        button2.setOnClickListener{
            val intent = Intent(this,GameActivity :: class.java )
            Xpoint = ((0..19).random())*30 + 15
            Ypoint = ((0..16).random())*30 + 15
            Xpoint_start = Xpoint
            Ypoint_start = Ypoint
            Xpoint_old = Xpoint
            Ypoint_old = Ypoint
            Left_time  = all_time
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

        button3.setOnClickListener{
            val dial_view = LayoutInflater.from(this).inflate(R.layout.dialog_main_exit, null)
            val builder = AlertDialog.Builder(this)
                    .setView(dial_view)
            val AlertDialog = builder.show()
            dial_view.yes.setOnClickListener{
                AlertDialog.dismiss()
                finish()
            }
            dial_view.no.setOnClickListener{
                AlertDialog.dismiss()
            }
        }
        var i: Int
        var j:Int
        var a:Array<Array<Int>> = Array(10,{Array(10,{10000})})
        var b:Array<Int> = Array(180,{0})
        var k:Int=0
        var n: Int = 0
        for (i in 0..70)
        {
            n=(0..180).random()
            if (n<90) {
                a[n/9][n%9]=a[n/9][n%9]/1000*1000+a[n/9][n%9]%100+200
                a[n/9][n%9+1]=a[n/9][n%9+1]/10*10+4
            }
            else {n=n-90
                a[n%9][n/9]=a[n%9][n/9]/100*100+a[n%9][n/9]%10+30
                a[n%9+1][n/9]=a[n%9+1][n/9]/10000*10000+a[n%9+1][n/9]%1000+1000
            }
            b[n]=1;

        }
        for (i in 0..10){for (j in 0..10){
            if (a[i][j]%10!=0) k=k+1;
            if(a[i][j]%100-a[i][j]%10!=0)k=k+1;
            if(a[i][j]%1000-a[i][j]%100!=0) k=k+1;
            a[i][j]=10000*k+a[i][j]%10000
            k=0}
        }

    }

    override fun onUserLeaveHint() {
        finish()
        super.onUserLeaveHint()
    }

    fun ClosedRange<Int>.random() =
            Random().nextInt(endInclusive - start) +  start
}

/*<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context="Labirint"
    android:background="@drawable/drangleic_castle">


    <Button
        android:id="@+id/button"
        android:layout_width="164dp"
        android:layout_height="33dp"
        android:layout_alignParentTop="true"
        android:layout_centerHorizontal="true"
        android:layout_marginTop="16dp"
        android:text="@string/con"
        android:textColor="@color/colorPrimary"
        android:textSize="12sp" />

    <Button
        android:id="@+id/button2"
        android:layout_width="172dp"
        android:layout_height="33dp"
        android:layout_alignParentBottom="true"
        android:layout_centerHorizontal="true"
        android:layout_marginBottom="16dp"
        android:text="@string/maxi"
        android:textColor="@color/colorPrimary"
        android:textSize="12sp" />

    <Button
        android:id="@+id/button3"
        android:layout_width="170dp"
        android:layout_height="33dp"
        android:layout_centerInParent="true"
        android:text="@string/max"
        android:textColor="@color/colorPrimary"
        android:textSize="12sp" />

    <Button
        android:id="@+id/button4"
        android:layout_width="166dp"
        android:layout_height="33dp"
        android:layout_alignParentTop="true"
        android:layout_centerHorizontal="true"
        android:layout_marginTop="59dp"
        android:text="@string/continiu"
        android:textColor="@color/colorPrimary"
        android:textSize="12sp" />


</RelativeLayout>






















<?xml version="1.0" encoding="utf-8"?>
<android.support.constraint.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context="statistica"
    android:background="@color/colorPrimary">

    <TextView
        android:id="@+id/textView"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Количество игр="
        android:textColor="@color/colorPrimaryDark"
        android:textSize="18sp"
        tools:layout_editor_absoluteX="40dp"
        tools:layout_editor_absoluteY="40dp" />

    <TextView
        android:id="@+id/textView2"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Минимальное время="
        android:textColor="@color/colorPrimaryDark"
        android:textSize="18sp"
        tools:layout_editor_absoluteX="40dp"
        tools:layout_editor_absoluteY="160dp" />

    <TextView
        android:id="@+id/textView3"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Количество побед="
        android:textColor="@color/colorPrimaryDark"
        android:textSize="18sp"
        tools:layout_editor_absoluteX="40dp"
        tools:layout_editor_absoluteY="80dp" />

    <TextView
        android:id="@+id/textView4"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Среднее время="
        android:textColor="@color/colorPrimaryDark"
        android:textSize="18sp"
        tools:layout_editor_absoluteX="40dp"
        tools:layout_editor_absoluteY="120dp" />

</android.support.constraint.ConstraintLayout>*/