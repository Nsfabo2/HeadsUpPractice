package com.example.headsupprep

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
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

 */

class MainActivity : AppCompatActivity() {

     lateinit var RV: RecyclerView
     lateinit var RVAdapter: RcyclerViewAdapter

     val apiInterface by lazy { APIClient().getClient().create(APIInterface::class.java) }

     lateinit var progressDialog: ProgressDialog
     lateinit var AddButton: Button
     lateinit var CelebrityET: EditText
     lateinit var EditButton: Button

     lateinit var celebrities: ArrayList<Celebrity>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        celebrities = arrayListOf()

        RV = findViewById(R.id.RV)
        RVAdapter = RcyclerViewAdapter(celebrities)
        RV.adapter = RVAdapter
        RV.layoutManager = LinearLayoutManager(this)
        AddButton = findViewById(R.id.AddButton)
        EditButton = findViewById(R.id.EditButton)
        CelebrityET = findViewById(R.id.CelebrityET)

        AddButton.setOnClickListener {
            intent = Intent(applicationContext, AddCelebrityActivity::class.java)
            val celebrityNames = arrayListOf<String>()
            for(c in celebrities){
                celebrityNames.add(c.name.lowercase())
            }
            intent.putExtra("celebrityNames", celebrityNames)
            startActivity(intent)
        }

        EditButton.setOnClickListener {
            if(CelebrityET.text.isNotEmpty()){
                updateCelebrity()
            }else{
                Toast.makeText(this, "Please enter a name", Toast.LENGTH_LONG).show()
            }
        }

        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Please Wait")
        progressDialog.show()

        getCelebrities()

    }//end oncraete
    private fun getCelebrities(){
        apiInterface.getCelebrities().enqueue(object: Callback<ArrayList<Celebrity>> {
            override fun onResponse(
                call: Call<ArrayList<Celebrity>>,
                response: Response<ArrayList<Celebrity>>
            ) {
                progressDialog.dismiss()
                celebrities = response.body()!!
                RVAdapter.update(celebrities)
            }

            override fun onFailure(call: Call<ArrayList<Celebrity>>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(this@MainActivity, "Unable to get data", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun updateCelebrity(){
        var celebrityID = 0
        for(celebrity in celebrities){
            if(CelebrityET.text.toString().capitalize() == celebrity.name){
                celebrityID = celebrity.pk
                intent = Intent(applicationContext, UpdateDeleteCelebrityActivity::class.java)
                intent.putExtra("celebrityID", celebrityID)
                startActivity(intent)
            }else{
                Toast.makeText(this, "${CelebrityET.text.toString().capitalize()} not found", Toast.LENGTH_LONG).show()
            }
        }
    }
}//end class