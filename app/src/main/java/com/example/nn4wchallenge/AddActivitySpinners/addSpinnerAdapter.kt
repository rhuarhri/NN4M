package com.example.nn4wchallenge.AddActivitySpinners

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.nn4wchallenge.R

class addSpinnerAdapter(var context : Context, var itemList : ArrayList<addActivityItem>) : BaseAdapter()
{

    private class viewHolder(row : View?)
    {

        var ItemIcon : ImageView

        var ItemTitleTXT : TextView

        init {
            this.ItemIcon = row?.findViewById(R.id.ItemIV) as ImageView
            this.ItemTitleTXT = row.findViewById(R.id.TitleTXT) as TextView
        }

    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var ItemView : View?
        var ViewHolder : viewHolder

        if (convertView == null)
        {
            var myLayout = LayoutInflater.from(context)
            ItemView = myLayout.inflate(R.layout.add_spinner_layout, parent, false)
            ViewHolder = viewHolder(ItemView)
            ItemView.tag = ViewHolder
        }
        else{
            ItemView = convertView
            ViewHolder = ItemView.tag as viewHolder
        }


        ViewHolder.ItemIcon.setImageResource(itemList.get(position).itemImage)
        ViewHolder.ItemTitleTXT.text = itemList.get(position).itemTitle


        return ItemView as View
    }

    override fun getItem(position: Int): Any {
        return itemList.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return itemList.size
    }

}
