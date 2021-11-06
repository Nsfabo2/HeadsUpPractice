package com.example.headsupprep

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class Data : AppCompatActivity() {

    private lateinit var dbHandler: DatabaseHandler

    private lateinit var MainRV: RecyclerView
    private lateinit var AdapterRV: RcyclerViewAdapter

    private lateinit var NameET: EditText
    private lateinit var Taboo1ET: EditText
    private lateinit var Taboo2ET: EditText
    private lateinit var Taboo3ET: EditText
    private lateinit var AddBtn: Button
    private lateinit var UpdateBtn: Button
    private lateinit var DataBackBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data)

        dbHandler = DatabaseHandler(this)

        MainRV = findViewById(R.id.MainRV)
        AdapterRV = RcyclerViewAdapter(this, dbHandler.getCelebrities())
        MainRV.adapter = AdapterRV
        MainRV.layoutManager = LinearLayoutManager(this)

        NameET = findViewById(R.id.NameET)
        Taboo1ET = findViewById(R.id.Taboo1ET)
        Taboo2ET = findViewById(R.id.Taboo2ET)
        Taboo3ET = findViewById(R.id.Taboo3ET)
        AddBtn = findViewById(R.id.AddBtn)
        UpdateBtn = findViewById(R.id.UpdateBtn)
        DataBackBtn = findViewById(R.id.DataBackBtn)
        AddBtn.setOnClickListener {
            addCelebrity()
        }
        DataBackBtn.setOnClickListener{
            intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
        }
    }//end oncreate

    private fun addCelebrity(){
        if(NameET.text.isNotBlank() && Taboo1ET.text.isNotBlank() && Taboo2ET.text.isNotBlank() &&
            Taboo3ET.text.isNotBlank()){
            dbHandler.addCelebrity(NameET.text.toString(), Taboo1ET.text.toString(), Taboo2ET.text.toString(), Taboo3ET.text.toString())
            Toast.makeText(this, "Celebrity added", Toast.LENGTH_LONG).show()
            AdapterRV.update(dbHandler.getCelebrities())
        }else{
            Toast.makeText(this, "All fields are required", Toast.LENGTH_LONG).show()
        }
    }

    fun display(update: Boolean){
        if(update){
            AddBtn.isVisible = false
            UpdateBtn.isVisible = true
        }else{
            AddBtn.isVisible = true
            UpdateBtn.isVisible = false
        }
    }

    fun updateCelebrity(celebrity: Celebrity){
        display(true)
        NameET.setText(celebrity.name)
        Taboo1ET.setText(celebrity.taboo1)
        Taboo2ET.setText(celebrity.taboo2)
        Taboo3ET.setText(celebrity.taboo3)
        UpdateBtn.setOnClickListener {
            if(NameET.text.isNotBlank() && Taboo1ET.text.isNotBlank() && Taboo2ET.text.isNotBlank() &&
                Taboo3ET.text.isNotBlank()){
                dbHandler.updateCelebrity(
                    Celebrity(celebrity.id,
                        NameET.text.toString(),
                        Taboo1ET.text.toString(),
                        Taboo2ET.text.toString(),
                        Taboo3ET.text.toString()))
                Toast.makeText(this, "Celebrity updated", Toast.LENGTH_LONG).show()
                display(false)
                this@Data.recreate()
            }else{
                Toast.makeText(this, "All fields are required", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun deleteCelebrity(celebrity: Celebrity){
        dbHandler.deleteCelebrity(celebrity)
        Toast.makeText(this, "Celebrity Deleted, bye :(", Toast.LENGTH_LONG).show()
        this@Data.recreate()
    }
}//end class