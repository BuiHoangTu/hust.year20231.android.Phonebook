package com.bhtu.phonebook

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView

class PhoneAdapter(
    private val phoneItems: List<PhoneItem>,
    private val context: Context
): BaseAdapter() {
    override fun getCount(): Int {
        return phoneItems.size
    }

    override fun getItem(position: Int): Any {
        return phoneItems[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = convertView
            ?: LayoutInflater
                .from(context)
                .inflate(R.layout.phone_item_in_list, parent, false)

        val selected = phoneItems[position]
        view.findViewById<ImageView>(R.id.imageView).setImageDrawable(selected.avatar)
        view.findViewById<TextView>(R.id.textView).text = selected.fullName

        return view
    }
}