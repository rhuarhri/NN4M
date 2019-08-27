package com.example.nn4wchallenge.fragmentCode

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.example.nn4wchallenge.R
import com.example.nn4wchallenge.imageHandling.RetrieveImageHandler

class CapturedImagePreview : Fragment() {

    private var fileLocation : String = ""
    private lateinit var previewImage : ImageView

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (arguments != null)
        {
            fileLocation = arguments!!.getString("file")!!
            displayImage()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.image_preview_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val exitBTN : Button = view.findViewById(R.id.exitBTN)
        exitBTN.setOnClickListener {
            fragmentManager!!.beginTransaction().remove(this).commit()
        }

        previewImage = view.findViewById(R.id.imagePreviewIV)

    }

    private fun displayImage()
    {
        if (context != null) {
                val getImage = RetrieveImageHandler(context!!)
                getImage.recyclerViewImageHandler(previewImage, fileLocation, true)
            }
    }

}