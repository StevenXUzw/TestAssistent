package com.steven.testassistant.common.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.steven.testassistant.R

// references to our images
private val mIconList = arrayOf<Int>(
        R.drawable.ic_person_outline_white_48dp,
        R.drawable.ic_list_white_48dp,
        R.drawable.ic_comment_white_48dp,
        R.drawable.ic_memory_white_48dp,
        R.drawable.ic_phone_android_white_48dp,
        R.drawable.ic_slideshow_white_48dp,
        R.drawable.ic_bug_report_white_48dp
        )
private val mTitleList = arrayOf<Int>(
        R.string.contact_tool,
        R.string.call_log_tool,
        R.string.message_tool,
        R.string.device_tool,
        R.string.hardware_tool,
        R.string.screen_tool,
        R.string.log_tool
        )
class ImageAdapter(private val mContext: Context) : BaseAdapter() {
    override fun getCount(): Int = mIconList.size

    override fun getItem(position: Int): Any? = null

    override fun getItemId(position: Int): Long = 0L

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
//        val imageView: ImageView
        val itemView: View
        val titleView: TextView
        val iconView: ImageView
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
//            imageView = ImageView(mContext)

            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image_text, parent, false)
            titleView = itemView.findViewById(R.id.item_title)
            iconView = itemView.findViewById(R.id.item_img)
//            iconView.layoutParams = ViewGroup.LayoutParams(150, 150)
            iconView.scaleType = ImageView.ScaleType.CENTER_CROP
            iconView.setPadding(8, 8, 8, 8)
            titleView.setText(mTitleList[position])
            iconView.setImageResource(mIconList[position])
        } else {
            itemView = convertView as View
        }
        return itemView
    }

}
