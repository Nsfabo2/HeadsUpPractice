package com.example.headsupprep

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpdateDeleteCelebrityActivity : AppCompatActivity() {

     lateinit var NameET: EditText
     lateinit var Taboo1ET: EditText
     lateinit var Taboo2ET: EditText
     lateinit var Taboo3ET: EditText
     lateinit var Deletebtn: Button
     lateinit var Updatebtn: Button
     lateinit var Backbtn: Button

     val apiInterface by lazy { APIClient().getClient().create(APIInterface::class.java) }

     var celebrityID = 0
     lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_delete_celebrity)

        celebrityID = intent.extras!!.getInt("celebrityID", 0)

        NameET = findViewById(R.id.UpDelName)
        Taboo1ET = findViewById(R.id.UpDelTaboo1)
        Taboo2ET = findViewById(R.id.UpDelTaboo2)
        Taboo3ET = findViewById(R.id.UpDelTaboo3)

        Deletebtn = findViewById(R.id.Deletebtn)
        Updatebtn = findViewById(R.id.Updatebtn)
        Backbtn = findViewById(R.id.UpDelBackbtn)

        Deletebtn.setOnClickListener {
            DeleteAlert()
        }
        Updatebtn.setOnClickListener {
            if(NameET.text.isNotEmpty() && Taboo1ET.text.isNotEmpty() &&
                Taboo2ET.text.isNotEmpty() && Taboo3ET.text.isNotEmpty()){
                UpdateAlert()
            }else{
                Toast.makeText(this, "One or more fields is empty", Toast.LENGTH_LONG).show()
            }
        }
        Backbtn.setOnClickListener {
            intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
        }

        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Please Wait")

        getCelebrity()

    }//end oncreate
    private fun getCelebrity(){
        progressDialog.show()

        apiInterface.getCelebrity(celebrityID).enqueue(object: Callback<Celebrity> {
            override fun onResponse(call: Call<Celebrity>, response: Response<Celebrity>) {
                progressDialog.dismiss()
                val celebrity = response.body()!!
                NameET.setText(celebrity.name)
                Taboo1ET.setText(celebrity.taboo1)
                Taboo2ET.setText(celebrity.taboo2)
                Taboo3ET.setText(celebrity.taboo3)
            }

            override fun onFailure(call: Call<Celebrity>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(this@UpdateDeleteCelebrityActivity, "Something went wrong", Toast.LENGTH_LONG).show()
            }

        })
    }

    private fun updateCelebrity(){
        progressDialog.show()

        apiInterface.updateCelebrity(
            celebrityID,
            Celebrity(
                NameET.text.toString(),
                Taboo1ET.text.toString(),
                Taboo2ET.text.toString(),
                Taboo3ET.text.toString(),
                celebrityID
            )).enqueue(object: Callback<Celebrity> {
            override fun onResponse(call: Call<Celebrity>, response: Response<Celebrity>) {
                progressDialog.dismiss()
                Toast.makeText(this@UpdateDeleteCelebrityActivity, "Celebrity Updated", Toast.LENGTH_LONG).show()
            }

            override fun onFailure(call: Call<Celebrity>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(this@UpdateDeleteCelebrityActivity, "Something went wrong", Toast.LENGTH_LONG).show()
            }

        })
    }

    private fun deleteCelebrity(){
        progressDialog.show()

        apiInterface.deleteCelebrity(celebrityID).enqueue(object: Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                progressDialog.dismiss()
                Toast.makeText(this@UpdateDeleteCelebrityActivity, "Celebrity Deleted", Toast.LENGTH_LONG).show()
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(this@UpdateDeleteCelebrityActivity, "Something went wrong", Toast.LENGTH_LONG).show()
            }

        })
    }

    private fun DeleteAlert(){

        val dialogBuilder = AlertDialog.Builder(this)

        dialogBuilder.setMessage("Are you sure that you want to delete this celebrity?")
            .setPositiveButton("Delete", DialogInterface.OnClickListener {
                    dialog, id -> deleteCelebrity()
            })

            .setNegativeButton("Cancel", DialogInterface.OnClickListener {
                    dialog, id -> dialog.cancel()
            })

        val alert = dialogBuilder.create()
        alert.setTitle("Caution")
        alert.show()
    }

    private fun UpdateAlert(){

        val dialogBuilder = AlertDialog.Builder(this)

        dialogBuilder.setMessage("Are you sure that you want to update this celebrity info?")
            .setPositiveButton("Update", DialogInterface.OnClickListener {
                    dialog, id -> updateCelebrity()
            })

            .setNegativeButton("Cancel", DialogInterface.OnClickListener {
                    dialog, id -> dialog.cancel()
            })

        val alert = dialogBuilder.create()
        alert.setTitle("Caution")
        alert.show()
    }

}//end class