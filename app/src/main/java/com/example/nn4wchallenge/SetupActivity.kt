package com.example.nn4wchallenge

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.example.nn4wchallenge.database.internal.SetupManager


class SetupActivity : AppCompatActivity() {

    /*The chest and waist icons exist in order to show the user
    were to measure.
     */

    private var setupM : SetupManager = SetupManager(applicationContext)

    private lateinit var genderSPN : Spinner
    private lateinit var ageSPN : Spinner
    private lateinit var chestSPN : Spinner
    private lateinit var waistSPN : Spinner
    private lateinit var shoeSizeSPN : Spinner

    private lateinit var saveBTN : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setup)

        genderSPN = findViewById(R.id.GenderSPN)
        setUpSpinner(genderSPN, setupM.getTitleList(setupM.GENDER))
        genderSPN.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {

                //val selectedItemText = adapterView.getItemAtPosition(i) as String
                setupM.setGender(i)

            }

            override fun onNothingSelected(adapterView: AdapterView<*>) {

            }
        }

        ageSPN = findViewById(R.id.ageSPN)
        setUpSpinner(ageSPN, setupM.getTitleList(setupM.AGE))
        ageSPN.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {

                //val selectedItemText = adapterView.getItemAtPosition(i) as String
                setupM.setAge(i)

            }

            override fun onNothingSelected(adapterView: AdapterView<*>) {

            }
        }

        chestSPN = findViewById(R.id.chestSizeSPN)
        setUpSpinner(chestSPN, setupM.getTitleList(setupM.CHEST))
        chestSPN.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {

                //val selectedItemText = adapterView.getItemAtPosition(i) as String
                setupM.setChest(i)

            }

            override fun onNothingSelected(adapterView: AdapterView<*>) {

            }
        }

        waistSPN = findViewById(R.id.waistSizeSPN)
        setUpSpinner(waistSPN, setupM.getTitleList(setupM.WAIST))
        waistSPN.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {

                //val selectedItemText = adapterView.getItemAtPosition(i) as String
                setupM.setWaist(i)

            }

            override fun onNothingSelected(adapterView: AdapterView<*>) {

            }
        }

        shoeSizeSPN = findViewById(R.id.shoeSizeSPN)
        setUpSpinner(shoeSizeSPN, setupM.getTitleList(setupM.SHOE))
        shoeSizeSPN.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {

                //val selectedItemText = adapterView.getItemAtPosition(i) as String
                setupM.setShoe(i)

            }

            override fun onNothingSelected(adapterView: AdapterView<*>) {

            }
        }

        saveBTN = findViewById(R.id.saveBTN)
        saveBTN.setOnClickListener {

            var error = setupM.saveUserData()

            if (error == "")
            {
                error = "Saved"
                goToAddClothingScreen()
            }

            Toast.makeText(applicationContext, error, Toast.LENGTH_SHORT).show()
        }

    }

    private fun goToAddClothingScreen()
    {
        val goTo = Intent(applicationContext, AddActivity::class.java)
        startActivity(goTo)
    }

    private fun setUpSpinner(currentSPN : Spinner, list : ArrayList<String>)
    {

        var adapter : ArrayAdapter<String> = ArrayAdapter(applicationContext, android.R.layout.simple_spinner_item, list)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        currentSPN.adapter = adapter

    }


}
