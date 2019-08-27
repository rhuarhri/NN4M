package com.example.nn4wchallenge.fragmentCode

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.ImageDecoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.ListFragment
import com.example.nn4wchallenge.R
import com.example.nn4wchallenge.database.external.DataTranslation

class colourPicker : Fragment() {


    private lateinit var callback : fromFragment

    override fun onAttach(context: Context) {
        super.onAttach(context)
        
        val activity : Activity = context as Activity
        try {
            callback = activity as fromFragment
        }
        catch(e : Exception)
        {
            Toast.makeText(context, "error is ${e.toString()}", Toast.LENGTH_LONG).show()
        }
    }

    private val converter : DataTranslation = DataTranslation()
    private var redAmount : Int = 0
    private var blueAmount : Int = 0
    private var greenAmount : Int = 0

    var hexColour : String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.colour_picker_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val colorR : SeekBar = view.findViewById(R.id.redSB)
        val colorG : SeekBar = view.findViewById(R.id.greenSB)
        val colorB : SeekBar = view.findViewById(R.id.blueSB)

        val colorPreview : TextView = view.findViewById(R.id.colourPreviewTXT)
        
        val cancelBTN : Button = view.findViewById(R.id.cancelBTN)
        cancelBTN.setOnClickListener {

            fragmentManager!!.beginTransaction().remove(this).commit()
        }
        val applyBTN : Button = view.findViewById(R.id.okBTN)
        applyBTN.setOnClickListener {

            callback.onColourSelected(hexColour)
            fragmentManager!!.beginTransaction().remove(this).commit()
        }




        colorR.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onProgressChanged(seekBar: SeekBar, progress: Int,
                                           fromUser: Boolean) {
                redAmount = progress
                hexColour = converter.rgbToHexString(redAmount, greenAmount, blueAmount)

                colorPreview.setBackgroundColor(Color.parseColor("#$hexColour"))

            }
        })

        colorG.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onProgressChanged(seekBar: SeekBar, progress: Int,
                                           fromUser: Boolean) {
                greenAmount = progress
                hexColour = converter.rgbToHexString(redAmount, greenAmount, blueAmount)

                colorPreview.setBackgroundColor(Color.parseColor("#$hexColour"))

            }
        })

        colorB.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onProgressChanged(seekBar: SeekBar, progress: Int,
                                           fromUser: Boolean) {
                blueAmount = progress
                hexColour = converter.rgbToHexString(redAmount, greenAmount, blueAmount)

                colorPreview.setBackgroundColor(Color.parseColor("#$hexColour"))

            }
        })
    }


}