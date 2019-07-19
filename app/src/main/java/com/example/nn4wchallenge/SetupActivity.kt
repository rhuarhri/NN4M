package com.example.nn4wchallenge

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.*
import androidx.lifecycle.Observer
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.example.nn4wchallenge.database.internal.SetupManager


class SetupActivity : AppCompatActivity() {

    /*The chest and waist icons exist in order to show the user
    were to measure.
     */

    private lateinit var setupM : SetupManager

    private lateinit var genderSPN : Spinner
    private lateinit var ageSPN : Spinner
    private lateinit var chestSPN : Spinner
    private lateinit var waistSPN : Spinner
    private lateinit var shoeSizeSPN : Spinner

    private lateinit var saveBTN : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setup)

        val function : String = intent.getStringExtra("function")

        setupM = SetupManager()

        genderSPN = findViewById(R.id.GenderSPN)
        setUpSpinner(genderSPN, setupM.getTitleList(setupM.GENDER))
        genderSPN.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {

                setupM.setGender(i)

            }

            override fun onNothingSelected(adapterView: AdapterView<*>) {

            }
        }

        ageSPN = findViewById(R.id.ageSPN)
        setUpSpinner(ageSPN, setupM.getTitleList(setupM.AGE))
        ageSPN.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {

                setupM.setAge(i)

            }

            override fun onNothingSelected(adapterView: AdapterView<*>) {

            }
        }

        chestSPN = findViewById(R.id.chestSizeSPN)
        setUpSpinner(chestSPN, setupM.getTitleList(setupM.CHEST))
        chestSPN.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {

                setupM.setChest(i)

            }

            override fun onNothingSelected(adapterView: AdapterView<*>) {

            }
        }

        waistSPN = findViewById(R.id.waistSizeSPN)
        setUpSpinner(waistSPN, setupM.getTitleList(setupM.WAIST))
        waistSPN.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {


                setupM.setWaist(i)

            }

            override fun onNothingSelected(adapterView: AdapterView<*>) {

            }
        }

        shoeSizeSPN = findViewById(R.id.shoeSizeSPN)
        setUpSpinner(shoeSizeSPN, setupM.getTitleList(setupM.SHOE))
        shoeSizeSPN.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {

                setupM.setShoe(i)

            }

            override fun onNothingSelected(adapterView: AdapterView<*>) {

            }
        }

        if (function == "update")
        {
            val threadId = setupM.getExistingUserData()

            WorkManager.getInstance().getWorkInfoByIdLiveData(threadId).observe(this, Observer {

                workInfo ->

                if (workInfo != null && workInfo.state == WorkInfo.State.SUCCEEDED)
                {

                    setupM.displayExistingData(workInfo)

                    genderSPN.setSelection(setupM.genderPosition)
                    ageSPN.setSelection(setupM.agePosition)
                    chestSPN.setSelection(setupM.chestPosition)
                    waistSPN.setSelection(setupM.waistPosition)
                    shoeSizeSPN.setSelection(setupM.shoeSizePosition)

                }


            })


        }

        saveBTN = findViewById(R.id.saveBTN)
        saveBTN.setOnClickListener {

            if (function == "update")
            {
                setupM.updateUserData()
                goToHomeScreen()
            }
            else {

                var error = setupM.saveUserData()

                if (error == "") {
                    error = "Saved"
                    goToAddClothingScreen()
                }

                Toast.makeText(applicationContext, error, Toast.LENGTH_SHORT).show()
            }

        }

        if (savedInstanceState != null)
        {
            genderSPN.setSelection(with(savedInstanceState) { getInt(genderKey) })
            ageSPN.setSelection(with(savedInstanceState) { getInt(ageKey) })
            chestSPN.setSelection(with(savedInstanceState) { getInt(chestKey) })
            waistSPN.setSelection(with(savedInstanceState) { getInt(waistKey) })
            shoeSizeSPN.setSelection(with(savedInstanceState) { getInt(shoeKey) })
        }

    }


    private fun goToHomeScreen()
    {
        val goTo = Intent(applicationContext, MainActivity::class.java)
        startActivity(goTo)
    }

    private fun goToAddClothingScreen()
    {
        val goTo = Intent(applicationContext, AddActivity::class.java)
        startActivity(goTo)
    }

    private fun setUpSpinner(currentSPN : Spinner, list : ArrayList<String>)
    {

        val adapter : ArrayAdapter<String> = ArrayAdapter(applicationContext, android.R.layout.simple_spinner_item, list)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        adapter.notifyDataSetChanged()

        currentSPN.adapter = adapter

    }

    //code related to save state
    private val genderKey : String = "gender"
    private val ageKey : String = "age"
    private val chestKey : String = "chest"
    private val waistKey : String = "waist"
    private val shoeKey : String = "shoe"

    override fun onSaveInstanceState(outState: Bundle?, outPersistentState: PersistableBundle?) {

        if (outState != null) {
            outState.putInt(genderKey, setupM.genderPosition)
            outState.putInt(ageKey, setupM.agePosition)
            outState.putInt(chestKey, setupM.chestPosition)
            outState.putInt(waistKey, setupM.waistPosition)
            outState.putInt(shoeKey, setupM.shoeSizePosition)
        }

        super.onSaveInstanceState(outState, outPersistentState)
    }

}
