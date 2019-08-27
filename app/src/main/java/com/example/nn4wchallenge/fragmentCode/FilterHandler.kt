package com.example.nn4wchallenge.fragmentCode

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.nn4wchallenge.AddActivitySpinners.AddActivityItem
import com.example.nn4wchallenge.AddActivitySpinners.AddSpinnerAdapter
import com.example.nn4wchallenge.R
import com.example.nn4wchallenge.clothesSpinnerData

class FilterHandler : Fragment()
{

    private val clothesData = clothesSpinnerData()
    private var selectedType : String = clothesData.getTypeList()[0].itemTitle
    private var selectedSeason : String = clothesData.getSeasonsList()[0].itemTitle

    private lateinit var callback : fromFragment
    private lateinit var appContext : Context

    override fun onAttach(context: Context) {
        super.onAttach(context)

        appContext = context

        val activity : Activity = context as Activity
        try {
            callback = activity as fromFragment
        }
        catch(e : Exception)
        {
            Toast.makeText(context, "error is ${e.toString()}", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.filter_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val helpBTN : Button = view.findViewById(R.id.helpBTN)
        helpBTN.setOnClickListener {
            userHelpDialog()
        }

        val exitBTN : Button = view.findViewById(R.id.exitBTN)
        exitBTN.setOnClickListener {
            fragmentManager!!.beginTransaction().remove(this).commit()
        }

        val applyBTN : Button = view.findViewById(R.id.applyBTN)
        applyBTN.setOnClickListener {

            callback.onFilterChange(selectedType, selectedSeason)
            fragmentManager!!.beginTransaction().remove(this).commit()
        }


        setUpTypeSpinner(clothesData.getTypeList(), view)
        setUpSeasonSpinner(clothesData.getSeasonsList(), view)
    }

    private fun setUpTypeSpinner(itemList : ArrayList<AddActivityItem>, view : View)
    {
        val typeSPN : Spinner = view.findViewById(R.id.typeSPN)

        val typeAdapter = AddSpinnerAdapter(view.context, itemList)

        typeSPN.adapter = typeAdapter

        typeSPN.onItemSelectedListener = object : AdapterView.OnItemClickListener,
            AdapterView.OnItemSelectedListener {
            override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                selectedType = itemList[position].itemTitle
            }

        }


    }

    private fun setUpSeasonSpinner(itemList : ArrayList<AddActivityItem>, view : View)
    {
        val seasonSPN : Spinner = view.findViewById(R.id.seasonSPN)

        val seasonAdapter = AddSpinnerAdapter(view.context, itemList)

        seasonSPN.adapter = seasonAdapter

        seasonSPN.onItemSelectedListener = object : AdapterView.OnItemClickListener,
            AdapterView.OnItemSelectedListener {
            override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {

                selectedSeason = itemList[position].itemTitle


            }

        }

    }

    private fun userHelpDialog()
    {
        AlertDialog.Builder(appContext)
            .setTitle("Help")
            .setMessage("Set the type and season you think match the item of clothing you have just taken the picture of.")
            .setNegativeButton("OK") { dialogInterface, i ->
                dialogInterface.dismiss()
            }.create().show()
    }

}