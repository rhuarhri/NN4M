package com.example.nn4wchallenge

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.example.nn4wchallenge.database.internal.DatabaseCommands
import com.example.nn4wchallenge.database.internal.DatabaseManager

class PurchaseActivity : AppCompatActivity() {

    private val commands : DatabaseCommands = DatabaseCommands()

    private lateinit var costTXT : TextView
    private lateinit var reviewTXT : TextView
    private lateinit var cardNumberET : EditText
    private lateinit var pinNumberET : EditText
    private lateinit var enterBTN : Button

    private var function : String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_purchase)

        costTXT = findViewById(R.id.totalPriceTXT)

        reviewTXT = findViewById(R.id.reviewTXT)

        cardNumberET = findViewById(R.id.cardNumberET)
        pinNumberET = findViewById(R.id.pinNumberET)
        enterBTN = findViewById(R.id.enterBTN)

        enterBTN.setOnClickListener {

            termsWarning()
        }

        function = intent.getStringExtra("function")
        if (function != null)
        {
            if (function == "buyAll")
            {
                setupForBuyAll()
            }
            else
            {
                val id : Int = intent.getIntExtra("id", 0)
                setupForSingleItem(id)
            }
        }
    }

    private fun setupForBuyAll()
    {
        var inputData = Data.Builder()
            .putString(commands.Cart_DB, commands.Cart_DB)
            .putString(commands.Cart_Get, commands.Cart_Get)
            .build()

        val getCartItemWorker = OneTimeWorkRequestBuilder<DatabaseManager>().setInputData(inputData).build()

        WorkManager.getInstance().enqueue(getCartItemWorker)

        WorkManager.getInstance().getWorkInfoByIdLiveData(getCartItemWorker.id).observe(this, Observer {

            workInfo ->

            if (workInfo != null && workInfo.state == WorkInfo.State.SUCCEEDED)
            {
                val names = workInfo.outputData.getStringArray(commands.Cart_name)

                if (names != null)
                {
                    var displayNames = ""
                    for (item in names)
                    {
                        displayNames += "$item\n"
                    }

                    reviewTXT.text = displayNames
                }
                else
                {
                    reviewTXT.text = "null"
                }
            }

        })

        inputData = Data.Builder()
            .putString(commands.Cart_DB, commands.Cart_DB)
            .putString(commands.Cart_Get_Prices, commands.Cart_Get_Prices)
            .build()

        val getTotalCostWorker = OneTimeWorkRequestBuilder<DatabaseManager>().setInputData(inputData).build()

        WorkManager.getInstance().enqueue(getTotalCostWorker)

        WorkManager.getInstance().getWorkInfoByIdLiveData(getTotalCostWorker.id).observe(this, Observer {

            workInfo ->

            if (workInfo != null && workInfo.state == WorkInfo.State.SUCCEEDED)
            {
                val cost : Double = workInfo.outputData.getDouble(commands.Cart_total_price, 0.00)

                costTXT.text = cost.toString()
            }
        })

    }

    private fun setupForSingleItem(id : Int)
    {
        val inputData = Data.Builder()
            .putString(commands.Cart_DB, commands.Cart_DB)
            .putString(commands.Cart_Get_Single_Item, commands.Cart_Get_Single_Item)
            .putInt(commands.Cart_ID, id)
            .build()

        val getCartItemWorker = OneTimeWorkRequestBuilder<DatabaseManager>().setInputData(inputData).build()

        WorkManager.getInstance().enqueue(getCartItemWorker)

        WorkManager.getInstance().getWorkInfoByIdLiveData(getCartItemWorker.id).observe(this, Observer {

                workInfo ->

            if (workInfo != null && workInfo.state == WorkInfo.State.SUCCEEDED)
            {
                val cost : Double = workInfo.outputData.getDouble(commands.Cart_price, 0.00)

                costTXT.text = cost.toString()

                val name : String? = workInfo.outputData.getString(commands.Cart_name)

                if (name != null) {
                    reviewTXT.text = name
                }
                else
                {
                    reviewTXT.text = "null"
                }
            }
            })

    }

    private fun termsWarning()
    {
        AlertDialog.Builder(this)
            .setTitle("Terms and conditions")
            .setMessage("Do you agree to the terms set out on www.somewebsite.com")
            .setPositiveButton("agree") { dialogInterface, i ->

            }
            .setNegativeButton("cancel") { dialogInterface, i ->
                dialogInterface.dismiss()
            }.create().show()
    }
}
