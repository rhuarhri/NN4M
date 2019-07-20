package com.example.nn4wchallenge.AddActivitySpinners

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.nn4wchallenge.R

class AddSpinnerAdapter(private var context : Context, private var itemList : ArrayList<AddActivityItem>) : BaseAdapter()
{

    private class ViewHolder(row : View?)
    {

        var itemIcon : ImageView

        var itemTitleTXT : TextView

        init {
            this.itemIcon = row?.findViewById(R.id.ItemIV) as ImageView
            this.itemTitleTXT = row.findViewById(R.id.TitleTXT) as TextView
        }

    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val itemView : View?
        val viewHolder : ViewHolder

        if (convertView == null)
        {
            val myLayout = LayoutInflater.from(context)
            itemView = myLayout.inflate(R.layout.add_spinner_layout, parent, false)
            viewHolder = ViewHolder(itemView)
            itemView.tag = viewHolder
        }
        else{
            itemView = convertView
            viewHolder = itemView.tag as ViewHolder
        }


        viewHolder.itemIcon.setImageResource(itemList[position].itemImage)
        viewHolder.itemTitleTXT.text = itemList[position].itemTitle


        return itemView as View
    }

    override fun getItem(position: Int): Any {
        return itemList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return itemList.size
    }

}
