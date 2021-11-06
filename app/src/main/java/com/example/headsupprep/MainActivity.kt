package com.example.headsupprep

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.Surface
import android.widget.*
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/*
the only topic will be celebrities. We will also include 3 phrases that players cannot use when describing the celebrity.

Create an application that starts with a GET request to pull data from the following API:
https://dojo-recipes.herokuapp.com/celebrities/
Use this data to populate a simple Recycler View.
Below the Recycler View, create a button to allow users to add a new celebrity.
Below the button, create a horizontal Linear Layout that holds and Entry Text and a Button,
which allow the user to enter a name. Use the entered name to search the existing celebrities once
the button is clicked.
If the celebrity is in our Recycler View, bring the user to a new activity,
that will allow the user to update or delete the selected celebrity.

Bonus:
Make use of coroutines to fetch API data in the background

Our version should have the following functionality:
- Use a start button to initiate the game
- Once the game starts, a 60 second timer should be displayed and count down
- The phone should display a Celebrity name and the three taboo words
- Rotating the device should move to the next celebrity

Bonus:
- Find a way to filter out 'bad data' (Nonsense posts that have not been removed using the Heads Up! Prep app)
- Add a 3 second countdown before the game starts
- If the game starts in portrait mode, wait until the user has rotated it to landscape mode to start the timer
- Make sure celebrities are never repeated
- Improve the UI to make the game more visually appealing
- Combine the Heads Up! app with the Heads Up! Prep app to allow users to modify the list of celebrities
 */

class MainActivity : AppCompatActivity() {

    private lateinit var dbHandler: DatabaseHandler

    private lateinit var First: LinearLayout
    private lateinit var Second: LinearLayout
    private lateinit var Third: LinearLayout

    private lateinit var Timer: TextView
    private lateinit var Name: TextView
    private lateinit var Taboo1: TextView
    private lateinit var Taboo2: TextView
    private lateinit var Taboo3: TextView
    private lateinit var GameName: TextView

    private lateinit var StartBtn: Button
    private lateinit var DataBtn: Button
    private lateinit var BackBtn: Button

    private var gameActive = false

    private lateinit var celebrities: ArrayList<Celebrity>

    private var celeb = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHandler = DatabaseHandler(this)

        First = findViewById(R.id.FLLO)
        Second = findViewById(R.id.SLLO)
        Third = findViewById(R.id.THLLO)

        Timer = findViewById(R.id.TimerTV)
        Name = findViewById(R.id.NameTV)
        Taboo1 = findViewById(R.id.Taboo1TV)
        Taboo2 = findViewById(R.id.Taboo2TV)
        Taboo3 = findViewById(R.id.Taboo3TV)
        GameName = findViewById(R.id.GameTV)

        StartBtn = findViewById(R.id.StartBtn)
        DataBtn = findViewById(R.id.DataBtn)
        BackBtn = findViewById(R.id.BackBtn)

        StartBtn.setOnClickListener {
            getCelebrities()
        }
        DataBtn.setOnClickListener {
            val intent = Intent(this, Data::class.java)
            startActivity(intent)
        }

        BackBtn.setOnClickListener {
            QuitAlert()
        }

        celebrities = arrayListOf()

    }//end oncraete

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        val rotation = windowManager.defaultDisplay.rotation
        if(rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_180){
            if(gameActive){
                celeb++
                newCelebrity(celeb)
                updateStatus(false)
            }else{
                updateStatus(false)
            }
        }else{
            if(gameActive){
                updateStatus(true)
            }else{
                updateStatus(false)
            }
        }
    }

    private fun newTimer(){
        if(!gameActive){
            gameActive = true
            GameName.text = "Please Rotate Device"
            StartBtn.isVisible = false
            DataBtn.isVisible = false
            val rotation = windowManager.defaultDisplay.rotation
            if(rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_180){
                updateStatus(false)
            }else{
                updateStatus(true)
            }

            object : CountDownTimer(60000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    Timer.text = "Time: ${millisUntilFinished / 1000}"
                }

                override fun onFinish() {
                    gameActive = false
                    Timer.text = "Time: --"
                    GameName.text = "Heads Up!"
                    StartBtn.isVisible = true
                    DataBtn.isVisible = true
                    updateStatus(false)
                }
            }.start()
        }
    }

    private fun newCelebrity(id: Int){
        if(id < celebrities.size){
            Name.text = celebrities[id].name
            Taboo1.text = celebrities[id].taboo1
            Taboo2.text = celebrities[id].taboo2
            Taboo3.text = celebrities[id].taboo3
        }
    }

    fun getCelebrities(){
        celebrities = dbHandler.getCelebrities()
        celebrities.shuffle()
        newCelebrity(0)
        newTimer()
    }

    private fun updateStatus(showCelebrity: Boolean){
        if(showCelebrity){
            Second.isVisible = true
            Third.isVisible = false
        }else{
            Second.isVisible = false
            Third.isVisible = true
        }
    }

    private fun QuitAtivity(){
        intent = Intent(applicationContext, MainActivity::class.java)
        startActivity(intent)

    }
    private fun QuitAlert(){

        val dialogBuilder = AlertDialog.Builder(this)

        dialogBuilder.setMessage("Are you sure that you want to quit the game?")
            .setPositiveButton("Quit", DialogInterface.OnClickListener {
                    dialog, id -> QuitAtivity()
            })

            .setNegativeButton("Cancel", DialogInterface.OnClickListener {
                    dialog, id -> dialog.cancel()
            })

        val alert = dialogBuilder.create()
        alert.setTitle("Caution")
        alert.show()
    }

}//end class