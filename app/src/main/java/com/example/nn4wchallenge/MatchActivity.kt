package com.example.nn4wchallenge

import android.app.Fragment
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.FrameLayout
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction

class MatchActivity : AppCompatActivity(), colourPicker.OnHeadlineSelectedListener {

    private lateinit var colourBTN : Button
    private lateinit var colourPickerDisplay : FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_match)

        //colourPickerDisplay = findViewById(R.id.colourPickerFRG)
        colourBTN = findViewById(R.id.colourBTN)
        colourBTN.setOnClickListener {


            val openFragment : FragmentTransaction = getSupportFragmentManager().beginTransaction()
            openFragment.replace(R.id.colourPickerFRG, colourPicker())
            openFragment.commit()

        }
    }


    override fun onArticleSelected(colourHex: String) {
        Toast.makeText(applicationContext, "Colour is $colourHex", Toast.LENGTH_LONG).show()
        colourBTN.setBackgroundColor(Color.parseColor(colourHex))
    }

/*
    override fun onAttachFragment(fragment: androidx.fragment.app.Fragment) {
        if (fragment is colourPicker)
        {
            try {
                fragment.callback = this
            }
            catch(e : Exception)
            {
                Toast.makeText(applicationContext, "error is ${e.toString()}", Toast.LENGTH_LONG).show()
            }
        }
    }
    */
}
