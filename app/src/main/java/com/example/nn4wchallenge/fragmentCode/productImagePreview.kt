package com.example.nn4wchallenge.fragmentCode

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.nn4wchallenge.R
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.example.nn4wchallenge.imageHandling.RetrieveImageHandler

class productImagePreview : Fragment() {

    private var imageURL : String = "No URL"
    private lateinit var previewIV : ImageView

    private lateinit var callback : fromFragment

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        try {
            callback = activity as fromFragment
        }
        catch(e : Exception)
        {
            Toast.makeText(context, "error is ${e.toString()}", Toast.LENGTH_LONG).show()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (arguments != null)
        {
            imageURL = arguments!!.getString("position")!!
            displayImage()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.product_preview_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val exitBTN : Button = view.findViewById(R.id.exitBTN)
        exitBTN.setOnClickListener {
            fragmentManager!!.beginTransaction().remove(this).commit()
        }

        val likeBTN : Button = view.findViewById(R.id.likeBTN)
        likeBTN.setOnClickListener {
            callback.onProductSelected()
        }

        val dislikeBTN : Button = view.findViewById(R.id.disLikeBTN)
        dislikeBTN.setOnClickListener {
            //could possibly more to the next product in the list
        }

        previewIV = view.findViewById(R.id.imagePreviewIV)
    }

    private fun displayImage()
    {
        if (context != null) {
            val getImage = RetrieveImageHandler(context!!)
            getImage.recyclerViewImageHandler(previewIV, imageURL, false)
        }
    }
}