package com.example.mobcompapp

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.mobcompapp.database.ReminderInfo
import kotlinx.android.synthetic.main.reminder_item.view.*

class ReminderHistoryAdaptor(context: Context, private  val list:List<ReminderInfo>): BaseAdapter() {

    private val inflater: LayoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    @SuppressLint("SetTextI18n")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val row = inflater.inflate(R.layout.reminder_item, parent, false)

        row.txtReminderName.text=list[position].title
        row.txtReminderDate.text=list[position].date
        row.txtReminderTime.text=list[position].hours + ":" + list[position].minutes

        return row
    }
    override fun getItem(position: Int): Any {
        return list[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return list.size
    }

}