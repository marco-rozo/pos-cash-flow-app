package com.marcodev.cashflow.adapter

import android.content.Context
import android.database.Cursor
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.marcodev.cashflow.entity.Flow
import com.marcodev.cashflow.R
import com.marcodev.cashflow.utils.NumberFormatUtils

class FlowAdapter(val context: Context, val cursor: Cursor) : BaseAdapter() {
    override fun getCount(): Int {
        return cursor.count
    }

    override fun getItem(id: Int): Any {
        cursor.moveToPosition(id)

        val flow = Flow(
            cursor.getInt(Flow.INDEX_KEY_ID),
            cursor.getDouble(Flow.INDEX_KEY_VALUE),
            cursor.getString(Flow.INDEX_KEY_TYPE),
            cursor.getString(Flow.INDEX_KEY_DETAIL),
            cursor.getString(Flow.INDEX_KEY_DTA_LCTO)
        )

        return flow
    }

    override fun getItemId(id: Int): Long {
        cursor.moveToPosition(id)
        return cursor.getInt(0).toLong()
    }

    override fun getView(id: Int, convertView: View?, parent: ViewGroup?): View {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val itemList = inflater.inflate(R.layout.item_list, null)

        val tvTitleItem = itemList.findViewById<TextView>(R.id.tvTitleItem)
        val tvDetailItem = itemList.findViewById<TextView>(R.id.tvDetailItem)
        val tvValueItem = itemList.findViewById<TextView>(R.id.tvValueItem)
        val tvCashSimbol = itemList.findViewById<TextView>(R.id.tvCashSimbol)

        cursor.moveToPosition(id)

        tvTitleItem.text = cursor.getString(Flow.INDEX_KEY_TYPE)
        tvDetailItem.text =
            cursor.getString(Flow.INDEX_KEY_DTA_LCTO) + " - " + cursor.getString(Flow.INDEX_KEY_DETAIL)

        tvValueItem.text =
            NumberFormatUtils.formatDoubleToCurrency(cursor.getDouble(Flow.INDEX_KEY_VALUE))

        if (tvTitleItem.text == "Crédito") {
            itemList.setBackgroundColor(context.resources.getColor(R.color.success))
        } else {
            itemList.setBackgroundColor(context.resources.getColor(R.color.danger))
            tvTitleItem.setTextColor(Color.WHITE)
            tvCashSimbol.setTextColor(Color.WHITE)
            tvDetailItem.setTextColor(Color.WHITE)
            tvValueItem.setTextColor(Color.WHITE)
        }


        return itemList
    }
}