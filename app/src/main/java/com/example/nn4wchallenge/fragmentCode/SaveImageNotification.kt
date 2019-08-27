package com.example.nn4wchallenge.fragmentCode

import android.app.Activity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.example.nn4wchallenge.R

class SaveImageNotification : Fragment() {

    private lateinit var callback : fromFragment
    private lateinit var timer : CountDownTimer

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        callback = activity as fromFragment
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.save_notifaction_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val saveBTN : Button = view.findViewById(R.id.saveImageBTN)
        saveBTN.setOnClickListener {
            callback.onSaveImageGranted()
            timer.cancel()
            fragmentManager!!.beginTransaction().remove(this).commit()
        }

        val helpLayout : ConstraintLayout = view.findViewById(R.id.helpSaveInfoLayout)

        val helpBTN : Button = view.findViewById(R.id.saveHelpBTN)
        helpBTN.setOnClickListener {
            helpLayout.visibility = View.VISIBLE
            timer.cancel()
        }

        val cancelBTN : Button = view.findViewById(R.id.cancelSaveBTN)
        cancelBTN.setOnClickListener {
            timer.cancel()
            fragmentManager!!.beginTransaction().remove(this).commit()
        }

        val cancelHelpBTN : Button = view.findViewById(R.id.cancelSaveInfoBTN)
        cancelHelpBTN.setOnClickListener {
            helpLayout.visibility = View.GONE
        }

        timer = object : CountDownTimer(10000, 1000){
            override fun onFinish() {
                fragmentManager!!.beginTransaction().remove(this@SaveImageNotification).commit()
            }

            override fun onTick(p0: Long) {
               //do nothing
            }

        }

        timer.start()
    }
}