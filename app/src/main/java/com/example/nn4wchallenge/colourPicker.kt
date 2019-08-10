package com.example.nn4wchallenge

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
import com.example.nn4wchallenge.database.external.DataTranslation

class colourPicker : Fragment() {


    lateinit var callback : OnHeadlineSelectedListener

    /*
    fun setOnHeadlineSelectedListener(callback: OnHeadlineSelectedListener) {
        this.callback = callback
    }

*/

    override fun onAttach(context: Context) {
        super.onAttach(context)
        
        var activity : Activity = context as Activity
        try {
            callback = activity as OnHeadlineSelectedListener
        }
        catch(e : Exception)
        {
            Toast.makeText(context, "error is ${e.toString()}", Toast.LENGTH_LONG).show()
        }
    }

    // This interface can be implemented by the Activity, parent Fragment,
    // or a separate test implementation.
    interface OnHeadlineSelectedListener {
        fun onArticleSelected(colourHex : String)
    }


    private val convertor : DataTranslation = DataTranslation()
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
            //()!!.onBackPressed()
            getFragmentManager()!!.beginTransaction().remove(this).commit()
        }
        val applyBTN : Button = view.findViewById(R.id.okBTN)
        applyBTN.setOnClickListener {

            callback.onArticleSelected(hexColour)
            getFragmentManager()!!.beginTransaction().remove(this).commit()
        }




        colorR.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onProgressChanged(seekBar: SeekBar, progress: Int,
                                           fromUser: Boolean) {
                redAmount = progress
                val colorStr = convertor.rgbToHexString(redAmount, greenAmount, blueAmount)

                hexColour = "#$colorStr"
                    colorPreview.setBackgroundColor(Color.parseColor(hexColour))

            }
        })

        colorG.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onProgressChanged(seekBar: SeekBar, progress: Int,
                                           fromUser: Boolean) {
                greenAmount = progress
                val colorStr = convertor.rgbToHexString(redAmount, greenAmount, blueAmount)

                hexColour = "#$colorStr"
                colorPreview.setBackgroundColor(Color.parseColor(hexColour))

            }
        })

        colorB.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onProgressChanged(seekBar: SeekBar, progress: Int,
                                           fromUser: Boolean) {
                blueAmount = progress
                val colorStr = convertor.rgbToHexString(redAmount, greenAmount, blueAmount)

                hexColour = "#$colorStr"
                colorPreview.setBackgroundColor(Color.parseColor(hexColour))

            }
        })
    }


}