package com.example.headsupprep

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddCelebrityActivity : AppCompatActivity() {

     lateinit var NameET: EditText
     lateinit var Taboo1ET: EditText
     lateinit var Taboo2ET: EditText
     lateinit var Taboo3ET: EditText
     lateinit var Addbtn: Button
     lateinit var Backbtn: Button

     val apiInterface by lazy { APIClient().getClient().create(APIInterface::class.java) }

     lateinit var progressDialog: ProgressDialog
     lateinit var existingCelebrities: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_celebrity)
        existingCelebrities = intent.extras!!.getStringArrayList("celebrityNames")!!

        NameET = findViewById(R.id.AddName)
        Taboo1ET = findViewById(R.id.AddTaboo1)
        Taboo2ET = findViewById(R.id.AddTaboo2)
        Taboo3ET = findViewById(R.id.AddTaboo3)

        Addbtn = findViewById(R.id.Addbtn)
        Backbtn = findViewById(R.id.AddBackbtn)

        Addbtn.setOnClickListener {
            if(NameET.text.isNotEmpty() && Taboo1ET.text.isNotEmpty() &&
                Taboo2ET.text.isNotEmpty() && Taboo3ET.text.isNotEmpty()){
                addCelebrity()
            }else{
                Toast.makeText(this, "One or more fields is empty", Toast.LENGTH_LONG).show()
            }
        }

        Backbtn.setOnClickListener {
            intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
        }
}//end oncreate
    private fun addCelebrity(){
        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Please Wait")
        progressDialog.show()

        apiInterface.addCelebrity(

            Celebrity(
                NameET.text.toString().capitalize(),
                Taboo1ET.text.toString(),
                Taboo2ET.text.toString(),
                Taboo3ET.text.toString(),
                0
            )
        ).enqueue(object: Callback<Celebrity> {
            override fun onResponse(call: Call<Celebrity>, response: Response<Celebrity>) {
                progressDialog.dismiss()
                if(!existingCelebrities.contains(NameET.text.toString().lowercase())){
                    intent = Intent(applicationContext, MainActivity::class.java)
                    startActivity(intent)
                }else{
                    Toast.makeText(this@AddCelebrityActivity, "Celebrity Already Exists", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<Celebrity>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(this@AddCelebrityActivity, "Unable to get data", Toast.LENGTH_LONG).show()
            }
        })
    }

}//end class